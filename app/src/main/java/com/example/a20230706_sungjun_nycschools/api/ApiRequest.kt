package com.example.a20230706_sungjun_nycschools.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequest {

    @GET("resource/s3k6-pzi2")
    suspend fun getSchoolsList(): Response<SchoolsResponse>

    @GET("resource/f9bf-2cp4")
    suspend fun getScores(@Query("dbn") bdn: String = "02326"): Response<SatScoresResponse>

}