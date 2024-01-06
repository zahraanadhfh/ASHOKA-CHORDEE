package com.example.scandia.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.scandia.R
import com.example.scandia.data.local.database.AppDatabase
import com.example.scandia.data.local.datastore.Session
import com.example.scandia.data.network.ApiConfig
import com.example.scandia.data.repository.ScandiaRepository
import com.example.scandia.databinding.FragmentProfileBinding
import com.example.scandia.databinding.FragmentResultScanBinding
import com.example.scandia.ui.viewmodel.SharedVM
import com.example.scandia.viewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
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
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toAbout()
        setUpProfile()
        logout()
    }

    private fun setUpProfile() = binding.run {
        viewModel.getSession.observe(viewLifecycleOwner){
            it?.let {
                txtNameDoctor.text = it.name
                txtEmailDoctor.text = it.email
                txtSpecialistDoctor.text = it.specialist
            }
        }
    }

    private fun logout() = binding.run {
        btnLogout.setOnClickListener {
            viewModel.clearSession()
            it.findNavController()
                .navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
        }
    }

    private fun toAbout() = binding.run {
        icAbout.setOnClickListener {
            it.findNavController()
                .navigate(ProfileFragmentDirections.actionProfileFragmentToAboutFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}