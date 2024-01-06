package com.example.scandia.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.scandia.data.local.database.AppDatabase
import com.example.scandia.data.local.datastore.Session
import com.example.scandia.data.network.ApiConfig
import com.example.scandia.data.repository.ScandiaRepository
import com.example.scandia.databinding.FragmentSplashScreenBinding
import com.example.scandia.ui.viewmodel.SharedVM
import com.example.scandia.viewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
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
        _binding = FragmentSplashScreenBinding.inflate(layoutInflater, container, false)
        Handler().postDelayed({
            lifecycleScope.launchWhenCreated {
                checkSession()
            }
        }, 2000L)
        return binding.root
    }

    private fun checkSession(){
        viewModel.getSession.observe(viewLifecycleOwner){
            if (it?.email.isNullOrEmpty()){
                Navigation.findNavController(requireView())
                    .navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment())
            } else {
                Navigation.findNavController(requireView())
                    .navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}