package com.example.scandia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.scandia.R
import com.example.scandia.data.local.database.AppDatabase
import com.example.scandia.data.local.datastore.Session
import com.example.scandia.data.network.ApiConfig
import com.example.scandia.data.repository.ScandiaRepository
import com.example.scandia.databinding.FragmentHistoryBinding
import com.example.scandia.databinding.FragmentProfileBinding
import com.example.scandia.model.generateDummyArticle
import com.example.scandia.ui.adapter.ArticleAdapter
import com.example.scandia.ui.adapter.PatientAdapter
import com.example.scandia.ui.viewmodel.SharedVM
import com.example.scandia.viewModelFactory

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
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
    private val patientAdapter: PatientAdapter by lazy {
        PatientAdapter {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpContentHistory()
        viewModel.getPatient()
        viewModel.patient.observe(viewLifecycleOwner){
            if (it.isNullOrEmpty()){
                binding.rvHistory.isVisible = false
                binding.txtEmpty.isVisible = true
            }else{
                binding.rvHistory.isVisible = true
                binding.txtEmpty.isVisible = false
                patientAdapter.setItems(it)
            }
        }
    }

    private fun setUpContentHistory() = binding.rvHistory.apply {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = patientAdapter
        setHasFixedSize(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}