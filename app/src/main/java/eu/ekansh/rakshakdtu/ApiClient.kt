package eu.ekansh.rakshakdtu

import android.content.Context
import eu.ekansh.rakshakdtu.data.TokenManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://93.127.172.217:2006/api/v1/auth/"

    private val _forceLogout = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val forceLogout = _forceLogout.asSharedFlow()

    fun triggerForceLogout() {
        _forceLogout.tryEmit(Unit)
    }

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

        apiService = retrofit.create(APIService::class.java)
    }

    lateinit var apiService: APIService
}