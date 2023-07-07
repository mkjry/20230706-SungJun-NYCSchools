package com.example.a20230706_sungjun_nycschools

import com.example.a20230706_sungjun_nycschools.api.APIClient
import com.example.a20230706_sungjun_nycschools.api.ApiActionStatus
import com.example.a20230706_sungjun_nycschools.api.ApiRequest
import com.example.a20230706_sungjun_nycschools.api.SchoolsResponse
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class RetrofigClientTest {

    lateinit var apiClient: Retrofit
    lateinit var mockServer: MockWebServer
    var mockedResponse = MockResponse()

    @Before
    fun setup() {
        mockServer = MockWebServer()
        apiClient = Retrofit.Builder()
            .baseUrl(APIClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Test
    fun testRetrofitBaseUrl() {
        assertThat(apiClient.baseUrl().toString() == APIClient.BASE_URL).isEqualTo(true)
    }

    @Test
    fun testResponseCode() {
        mockedResponse.setResponseCode(200)
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<SchoolsResponse> =
                APIClient.retrofit.create(ApiRequest::class.java).getSchoolsList()
            if (response.isSuccessful) {
                assertThat(response.code()).isEqualTo(mockedResponse.hashCode())
            }
        }
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }
}