package com.example.a20230706_sungjun_nycschools.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a20230706_sungjun_nycschools.api.APIClient
import com.example.a20230706_sungjun_nycschools.api.ApiActionStatus
import com.example.a20230706_sungjun_nycschools.api.ApiRequest
import com.example.a20230706_sungjun_nycschools.api.SatScoresResponse
import com.example.a20230706_sungjun_nycschools.api.SchoolsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class SchoolViewModel : ViewModel() {
    val schools = MutableLiveData<SchoolsResponse>()
    val satScores = MutableLiveData<SatScoresResponse>()
    val apiStatus = MutableLiveData<ApiActionStatus>()

    fun initViewModel() {
        apiStatus.value = ApiActionStatus.IDLE_INITIALIZED
        getSchoolsData()
    }

    private fun getSchoolsData() {

        apiStatus.value = ApiActionStatus.GETTING_SCHOOLS
        viewModelScope.launch {
            Dispatchers.IO
            val response: Response<SchoolsResponse> =
                APIClient.retrofit.create(ApiRequest::class.java).getSchoolsList()

            if (response.isSuccessful) {
                apiStatus.value = ApiActionStatus.SUCCESS_GET_SCHOOLS
                schools.postValue((response.body()!!))

            } else {
                apiStatus.value = ApiActionStatus.FAIL_GET_SCHOOLS
            }
            return@launch
        }
    }

    fun getSatScores(dbn: String) {

        apiStatus.value = ApiActionStatus.GETTING_SATS
        viewModelScope.launch {
            Dispatchers.IO
            val response: Response<SatScoresResponse> =
                APIClient.retrofit.create(ApiRequest::class.java).getScores(dbn)

            if (response.isSuccessful) {
                satScores.postValue(response.body())
                apiStatus.value = ApiActionStatus.SUCCESS_GET_SATS
            } else {
                apiStatus.value = ApiActionStatus.FAIL_GET_SATS
            }
            return@launch
        }
    }
}