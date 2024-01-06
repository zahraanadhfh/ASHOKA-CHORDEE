package com.example.scandia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.scandia.data.local.database.AppDatabase
import com.example.scandia.data.local.database.entity.DoctorEntity
import com.example.scandia.data.local.datastore.Session
import com.example.scandia.data.network.ApiConfig
import com.example.scandia.data.repository.ScandiaRepository
import com.example.scandia.databinding.FragmentRegisterBinding
import com.example.scandia.ui.viewmodel.SharedVM
import com.example.scandia.viewModelFactory


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register()
    }

    private fun register() = binding.run {
        btnRegister.setOnClickListener { view ->
            when {
                validate() -> {
                    viewModel.isDoctorExist(inputEmail.text.toString().trim())
                    if (inputPassword.text.toString().trim() != inputConfirmPassword.text.toString().trim()) {
                        SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Password tidak sama!")
                            .show()
                    } else {
                        viewModel.doctorExistsStatus.observe(viewLifecycleOwner) {
                            if (it) {
                                view.findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentSelf())
                                SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Email telah terpakai!")
                                    .show()
                            } else {
                                viewModel.registerDoctor(
                                    DoctorEntity(
                                        name = inputName.text.toString().trim(),
                                        specialist = inputSpesialis.text.toString().trim(),
                                        email = inputEmail.text.toString().trim(),
                                        password = inputPassword.text.toString().trim(),
                                    )
                                )
                                view.findNavController()
                                    .navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                                SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Register Sukses")
                                    .show()
                                }
                        }
                    }
                }

                else -> {
                    SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Semua Field harus terisi!")
                        .show()
                }
            }
        }
    }

    private fun validate(): Boolean {
        var validate = true
        with(binding) {
            if (inputName.text.isNullOrEmpty()) validate = false
            if (inputEmail.text.isNullOrEmpty()) validate = false
            if (inputSpesialis.text.isNullOrEmpty()) validate = false
            if (inputPassword.text.isNullOrEmpty()) validate = false
            if (inputConfirmPassword.text.isNullOrEmpty()) validate = false
        }
        return validate
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}