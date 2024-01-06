package com.example.scandia.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.example.scandia.createCustomTempFile
import com.example.scandia.data.local.database.AppDatabase
import com.example.scandia.data.local.database.entity.PatientEntity
import com.example.scandia.data.local.datastore.Session
import com.example.scandia.data.network.ApiConfig
import com.example.scandia.data.repository.ScandiaRepository
import com.example.scandia.databinding.FragmentAddPatientBinding
import com.example.scandia.imageMultipart
import com.example.scandia.showDatePicker
import com.example.scandia.ui.viewmodel.SharedVM
import com.example.scandia.uriToFile
import com.example.scandia.viewModelFactory
import com.google.android.material.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class AddPatientFragment : Fragment() {

    private var _binding: FragmentAddPatientBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedVM by viewModelFactory {
        SharedVM(
            ScandiaRepository(
                AppDatabase.getInstance(requireContext()).doctorDao(),
                AppDatabase.getInstance(requireContext()).patientDao(),
                ApiConfig()
            ), Session(requireContext())
        )
    }
    private var getFile: File? = null
    private lateinit var pathPhoto: String
    private var fileUri: Uri? = null
    private var fileBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPatientBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doDetection()
        selectedPhoto()
        binding.icBack.setOnClickListener {
            it.findNavController().popBackStack()
        }
        binding.etDate.setOnClickListener {
            showDatePicker(parentFragmentManager, binding.etDate)
        }
    }

    private fun validate(): Boolean {
        var validate = true
        with(binding) {
            if (etName.text.isNullOrEmpty()) validate = false
            if (etDate.text.isNullOrEmpty()) validate = false
            if (etAge.text.isNullOrEmpty()) validate = false
            if (etDesc.text.isNullOrEmpty()) validate = false
            if (getFile == null) validate = false
        }
        return validate
    }

    private fun doDetection() = binding.run {
        btnDetection.setOnClickListener {
            if (validate()) {
                getFile?.let { getFile -> imageMultipart(getFile) }?.let { fileMultipart ->
                    viewModel.predict(fileMultipart)
                }
                observeData()
            } else {
                SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Deteksi Gagal, Semua Field harus terisi!")
                    .show()
            }
        }
    }

    private fun observeData() {
        with(viewModel) {
            with(binding) {
                isLoading.observe(viewLifecycleOwner) {
                    if (it == true) {
                        pb.visibility = View.VISIBLE
                        btnDetection.visibility = View.GONE
                        btnDetection.isEnabled = false
                    } else {
                        pb.visibility = View.GONE
                        btnDetection.isEnabled = true
                        btnDetection.visibility = View.VISIBLE
                    }
                }
                isError.observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { msg ->
                        activity?.window?.decorView?.rootView?.let { rootView ->
                            SweetAlertDialog(rootView.context, SweetAlertDialog.WARNING_TYPE)
                                .setContentText(msg)
                                .show()
                        }
                    }
                }
                response.observe(viewLifecycleOwner) { predict ->
                    if (predict != null) {
                        SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Berhasil Mendeteksi")
                            .setContentText(
                                "Derajat kelengkugan ${
                                    predict.regression_result?.get(0)?.get(0)?.toString().orEmpty()
                                }"
                            ).show()
                        if (fileUri != null || fileBitmap != null) {
                            Navigation.findNavController(requireView())
                                .navigate(
                                    AddPatientFragmentDirections
                                        .actionAddPatientFragmentToResultScanFragment(
                                            fileUri,
                                            fileBitmap,
                                            etName.text.toString().trim(),
                                            etAge.text.toString().trim() + " Tahun",
                                            etDate.text.toString().trim(),
                                            predict.regression_result?.get(0)?.get(0)?.toString()
                                                .orEmpty() + " °",
                                            etDesc.text.toString().trim(),
                                        )
                                )
                            viewModel.addPatient(
                                name = etName.text.toString().trim(),
                                age = etAge.text.toString().trim(),
                                date = etDate.text.toString().trim(),
                                desc = etDesc.text.toString().trim(),
                                degree = predict.regression_result?.get(0)?.get(0)?.toString()
                                    .orEmpty()
                            )
                        }
                    }
                    fileUri = null
                    fileBitmap = null
                    etName.text?.clear()
                    etAge.text?.clear()
                    etDate.text?.clear()
                    etDesc.text?.clear()
                }
            }
        }
    }

    private fun selectedPhoto() = binding.run {
        icGallery.setOnClickListener {
            launchGallery()
        }
        icCamera.setOnClickListener {
            launchCamera()
        }
    }

    private fun launchGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, "Choose a picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)

        createCustomTempFile(requireActivity().application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.scandia",
                it
            )
            pathPhoto = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }


    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val file = File(pathPhoto).also { file -> getFile = file }
            val os: OutputStream

            // Rotate image to correct orientation
            val bitmap = BitmapFactory.decodeFile(getFile?.path)
            val exif = ExifInterface(pathPhoto)
            val orientation: Int = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            val rotatedBitmap: Bitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(bitmap, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(bitmap, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(bitmap, 270)
                ExifInterface.ORIENTATION_NORMAL -> bitmap
                else -> bitmap
            }

            // Convert rotated image to file
            try {
                os = FileOutputStream(file)
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                os.flush()
                os.close()

                getFile = file
            } catch (e: Exception) {
                e.printStackTrace()
            }
            binding.previewImage.isVisible = true
            binding.previewImage.setImageBitmap(rotatedBitmap)
            fileBitmap = rotatedBitmap
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())

            getFile = myFile
            binding.previewImage.isVisible = true
            binding.previewImage.setImageURI(selectedImg)
            fileUri = selectedImg
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}