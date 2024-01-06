package com.example.scandia.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.scandia.R
import com.example.scandia.data.local.database.AppDatabase
import com.example.scandia.data.local.datastore.Session
import com.example.scandia.data.network.ApiConfig
import com.example.scandia.data.repository.ScandiaRepository
import com.example.scandia.databinding.FragmentHomeBinding
import com.example.scandia.databinding.FragmentLoginBinding
import com.example.scandia.ui.viewmodel.SharedVM
import com.example.scandia.viewModelFactory

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
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
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doLogin()
        doRegister()
    }

    private fun doLogin() = binding.run {
        btnLogin.setOnClickListener {
            if (validate()) {
                viewModel.getCurrentDoctorInfo(
                    inputEmail.text.toString().trim(),
                    inputPassword.text.toString().trim()
                )
                viewModel.doctorLoggedIn.observe(viewLifecycleOwner) { doctor ->
                    if (doctor != null) {
                        SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Login Sukses")
                            .show()
                        it.findNavController()
                            .navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    } else {
                        SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Login Gagal, Email atau Password salah!")
                            .show()
                        it.findNavController()
                            .navigate(LoginFragmentDirections.actionLoginFragmentSelf())
                    }
                }
            } else {
                SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Login Gagal, Semua Field harus terisi!")
                    .show()
            }
        }
    }

    private fun doRegister() = binding.run {
        tvRegister.setOnClickListener {
            it.findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    private fun validate(): Boolean {
        var validate = true
        with(binding) {
            if (inputEmail.text.isNullOrEmpty()) validate = false
            if (inputPassword.text.isNullOrEmpty()) validate = false
        }
        return validate
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}