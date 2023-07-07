package com.example.a20230706_sungjun_nycschools

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a20230706_sungjun_nycschools.api.SchoolData
import com.example.a20230706_sungjun_nycschools.api.SchoolsResponse
import com.example.a20230706_sungjun_nycschools.databinding.SchoolLayoutBinding

class SchoolsAdapter(private val schools: SchoolsResponse) :
    RecyclerView.Adapter<SchoolsAdapter.AdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AdapterViewHolder(layoutInflater.inflate(R.layout.school_layout, parent, false))
    }

    override fun getItemCount(): Int = schools.size

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.bind(schools[position])
    }

    inner class AdapterViewHolder(schoolView: View) : RecyclerView.ViewHolder(schoolView) {
        private val binding = SchoolLayoutBinding.bind(schoolView)

        init {
            schoolView.setOnClickListener {
                (binding.root.context as MainActivity).getSatScore(schools[adapterPosition].dbn)
            }
        }

        fun bind(school: SchoolData) {
            binding.schoolunit = school
        }
    }
}