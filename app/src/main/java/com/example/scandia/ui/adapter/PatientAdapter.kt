package com.example.scandia.ui.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scandia.data.local.database.entity.PatientEntity
import com.example.scandia.databinding.ItemHistoryBinding


class PatientAdapter(private val itemClick: (PatientEntity) -> Unit) :
    RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    private var items: MutableList<PatientEntity> = mutableListOf()

    fun setItems(items: List<PatientEntity>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size


    class PatientViewHolder(
        private val binding: ItemHistoryBinding,
        val itemClick: (PatientEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: PatientEntity) {
            with(item) {
                itemView.setOnClickListener { itemClick(this) }
                with(binding){
                    txtAgePatient.text = age
                    txtNamePatient.text = name
                    txtDatePatient.text = date
                    txtDegreePatient.text = degree
                    etDesc.setText(desc)
                }
            }

        }
    }

}