package com.example.a20230706_sungjun_nycschools

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a20230706_sungjun_nycschools.api.APIClient
import com.example.a20230706_sungjun_nycschools.api.ApiRequest
import com.example.a20230706_sungjun_nycschools.api.SatScore
import com.example.a20230706_sungjun_nycschools.api.SatScoresResponse
import com.example.a20230706_sungjun_nycschools.api.SchoolsResponse
import com.example.a20230706_sungjun_nycschools.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var schoolsList = SchoolsResponse()
    private var satScores = SatScoresResponse()
    private lateinit var schoolsAdapter: SchoolsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getSchools()
        binding.root.setOnClickListener { schoolListVisible() }
    }

    private fun getSchools() {
        binding.recyclerVIew.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<SchoolsResponse> =
                APIClient.retrofit.create(ApiRequest::class.java).getSchoolsList()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    schoolsList = response.body()!!
                    openRecyclerView()

                } else {
                    binding.tvTextView.text = getString(R.string.school_list_data_error)
                    messageVisible()
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun openRecyclerView() {
        if (schoolsList.isEmpty()) return
        schoolsAdapter = SchoolsAdapter(schoolsList)
        binding.recyclerVIew.setHasFixedSize(true)
        binding.recyclerVIew.layoutManager = LinearLayoutManager(this)
        binding.recyclerVIew.adapter = schoolsAdapter
        schoolsAdapter.notifyDataSetChanged()
        schoolListVisible()
    }

    fun getSatScore(dbn: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerVIew.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<SatScoresResponse> =
                APIClient.retrofit.create(ApiRequest::class.java).getScores(dbn)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    satScores = response.body()!!
                    showSatScoreData()
                } else {
                    binding.tvTextView.text = getString(R.string.sat_score_data_error)
                    messageVisible()
                }
            }
        }

    }

    private fun showSatScoreData() {
        if (satScores.isEmpty()) {
            binding.tvTextView.text = getString(R.string.no_sat_data_from_api)
        } else {
            val satScore: SatScore = satScores[0]
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

    private fun schoolListVisible() {
        binding.progressBar.visibility = View.GONE
        binding.tvTextView.visibility = View.GONE
        binding.recyclerVIew.visibility = View.VISIBLE
    }

    private fun messageVisible() {
        binding.recyclerVIew.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.tvTextView.visibility = View.VISIBLE
    }
}