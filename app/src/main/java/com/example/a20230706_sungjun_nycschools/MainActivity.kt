package com.example.a20230706_sungjun_nycschools

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a20230706_sungjun_nycschools.api.ApiActionStatus.FAIL_GET_SATS
import com.example.a20230706_sungjun_nycschools.api.ApiActionStatus.FAIL_GET_SCHOOLS
import com.example.a20230706_sungjun_nycschools.api.ApiActionStatus.GETTING_SATS
import com.example.a20230706_sungjun_nycschools.api.ApiActionStatus.GETTING_SCHOOLS
import com.example.a20230706_sungjun_nycschools.api.ApiActionStatus.SUCCESS_GET_SATS
import com.example.a20230706_sungjun_nycschools.api.ApiActionStatus.SUCCESS_GET_SCHOOLS
import com.example.a20230706_sungjun_nycschools.api.SatScore
import com.example.a20230706_sungjun_nycschools.databinding.ActivityMainBinding
import com.example.a20230706_sungjun_nycschools.viewmodel.SchoolViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SchoolViewModel
    private lateinit var schoolsAdapter: SchoolsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SchoolViewModel::class.java]
        apiDataMonitor()

        // This make visible Recyclerview, when SAT data is visible
        // Click anywhere of SAT score screen, then return to Recyclerview
        // then can click for other school's SAT Score data, without another API call
        binding.root.setOnClickListener { schoolListVisible() }
    }

    private fun apiDataMonitor() {
        viewModel.initViewModel()
        binding.progressBar.visibility = GONE

        viewModel.apiStatus.observe(this) {
            when (it) {
                GETTING_SCHOOLS, GETTING_SATS -> {
                    binding.recyclerVIew.visibility = GONE
                    binding.progressBar.visibility = VISIBLE
                }

                SUCCESS_GET_SCHOOLS -> schoolsListVisible()

                FAIL_GET_SCHOOLS -> {
                    binding.tvTextView.text = getString(R.string.school_list_data_error)
                    messageVisible()
                }

                SUCCESS_GET_SATS -> showSatScoreData()

                FAIL_GET_SATS -> {
                    binding.tvTextView.text = getString(R.string.sat_score_data_error)
                    messageVisible()
                }

                else -> {}
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    private fun schoolsListVisible() {
        viewModel.schools.observe(this) {
            if(!it.isNullOrEmpty()) {
                schoolsAdapter = SchoolsAdapter(it)
                binding.recyclerVIew.setHasFixedSize(true)
                binding.recyclerVIew.layoutManager = LinearLayoutManager(this)
                binding.recyclerVIew.adapter = schoolsAdapter
                schoolListVisible()
            }
        }
    }

    fun getSatScore(dbn: String) {
        binding.progressBar.visibility = VISIBLE
        binding.recyclerVIew.visibility = GONE
        viewModel.getSatScores(dbn)
    }

    private fun showSatScoreData() {

        viewModel.satScores.observe(this) {
            if (it.isEmpty()) {
                binding.tvTextView.text = getString(R.string.no_sat_data_from_api)
            } else {
                val satScore: SatScore = it[0]
                val satStringBuilder = StringBuilder()
                satStringBuilder.append("SAT SCORE by School\n\n\n")
                    .append("School Name :\n").append(satScore.school_name)
                    .append("\n\n")
                    .append("Math Average Score :\t").append(satScore.sat_math_avg_score)
                    .append("\n\n")
                    .append("Reading Average Score :\t").append(satScore.sat_writing_avg_score)
                    .append("\n\n")
                    .append("Writing Average Score :\t").append(satScore.sat_writing_avg_score)
                    .append("\n\n")
                    .append("Test takers :\t").append(satScore.num_of_sat_test_takers)
                    .append("\n\n")
                    .append("dbn :\t").append(satScore.dbn)
                binding.tvTextView.text = satStringBuilder.toString()
            }
            messageVisible()
        }
    }

    private fun schoolListVisible() {
        binding.progressBar.visibility = GONE
        binding.tvTextView.visibility = GONE
        binding.recyclerVIew.visibility = VISIBLE
    }

    private fun messageVisible() {
        binding.recyclerVIew.visibility = GONE
        binding.progressBar.visibility = GONE
        binding.tvTextView.visibility = VISIBLE
    }
}