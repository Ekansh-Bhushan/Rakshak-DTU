package eu.ekansh.rakshakdtu

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AllAPIClient {
    private const val BASE_URL = "http://93.127.172.217:2006/api/v1/"

    private val httpClient = OkHttpClient.Builder().build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Define the service here
    val vehicleApiService: VehicleAPIServices by lazy {
        retrofit.create(VehicleAPIServices::class.java)
    }

    val cameraApiService: CameraAPIServices by lazy {
        retrofit.create(CameraAPIServices::class.java)
    }

}