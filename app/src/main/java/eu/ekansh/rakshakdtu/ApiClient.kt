package eu.ekansh.rakshakdtu

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://93.127.172.217:2006/api/v1/auth/"

    val apiService: APIService by lazy {

        val httpClient = OkHttpClient.Builder()
            // Add any interceptors here if needed for headers, logging, etc.
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }
}