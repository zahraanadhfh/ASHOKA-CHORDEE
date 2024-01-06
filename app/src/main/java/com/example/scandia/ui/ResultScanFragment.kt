package com.example.scandia.ui

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.scandia.R
import com.example.scandia.data.local.database.AppDatabase
import com.example.scandia.data.repository.ScandiaRepository
import com.example.scandia.databinding.FragmentResultScanBinding
import com.example.scandia.databinding.FragmentSplashScreenBinding
import com.example.scandia.ui.viewmodel.SharedVM
import com.example.scandia.viewModelFactory

class ResultScanFragment : Fragment() {

    private var _binding: FragmentResultScanBinding? = null
    private val binding get() = _binding!!
    private val navArgs: ResultScanFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultScanBinding.inflate(layoutInflater, container, false)
        val callbacks: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.homeFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callbacks)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpResultScan()
        binding.btnBackHome.setOnClickListener {
            it.findNavController()
                .navigate(ResultScanFragmentDirections.actionResultScanFragmentToHomeFragment())
        }
        binding.btnHistory.setOnClickListener {
            it.findNavController()
                .navigate(ResultScanFragmentDirections.actionResultScanFragmentToHistoryFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpResultScan() {
        with(binding) {
            if (navArgs.bitmap != null) {
                icPict.setImageBitmap(navArgs.bitmap)
            }
            if (navArgs.uri != null) {
                icPict.setImageURI(navArgs.uri)
            }
            txtNamePatient.text = navArgs.name
            txtAgePatient.text = navArgs.umur
            txtDatePatient.text = navArgs.date
            txtDegreePatient.text = navArgs.degree
            etDesc.isEnabled = false
            etDesc.isSingleLine = false
            etDesc.setText(navArgs.desc)
        }
    }
}