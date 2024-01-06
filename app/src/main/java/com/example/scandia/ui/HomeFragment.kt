package com.example.scandia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.scandia.data.local.database.AppDatabase
import com.example.scandia.data.repository.ScandiaRepository
import com.example.scandia.databinding.FragmentHomeBinding
import com.example.scandia.model.generateDummyArticle
import com.example.scandia.ui.adapter.ArticleAdapter
import com.example.scandia.ui.viewmodel.SharedVM
import com.example.scandia.viewModelFactory


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val articleAdapter: ArticleAdapter by lazy {
        ArticleAdapter {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doAddPatient()
        setUpContentArticle()
    }

    private fun setUpContentArticle() = binding.rvArticle.apply {
        layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        adapter = articleAdapter
        setHasFixedSize(false)
        articleAdapter.setItems(generateDummyArticle())
    }

    private fun doAddPatient() = binding.run {
        fabAddPatient.setOnClickListener {
            it.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToAddPatientFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}