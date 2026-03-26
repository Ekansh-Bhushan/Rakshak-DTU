package eu.ekansh.rakshakdtu

import android.content.Context
import eu.ekansh.rakshakdtu.data.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AllAPIClient {
    private const val BASE_URL = "http://93.127.172.217:2006/api/v1/"

    lateinit var vehicleApiService: VehicleAPIServices
        private set
    lateinit var cameraApiService: CameraAPIServices
        private set
    lateinit var logApiService: LogAPIServices
        private set

    fun init(context: Context) {
        val tokenManager = TokenManager(context)
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        vehicleApiService = retrofit.create(VehicleAPIServices::class.java)
        cameraApiService  = retrofit.create(CameraAPIServices::class.java)
        logApiService     = retrofit.create(LogAPIServices::class.java)
    }
}