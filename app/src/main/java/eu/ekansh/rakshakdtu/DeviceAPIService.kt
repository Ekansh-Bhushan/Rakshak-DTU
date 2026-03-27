package eu.ekansh.rakshakdtu

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// Add to AllAPIClient's services or create a new one
interface DeviceAPIService {
    @POST("devices/token")
    suspend fun registerDeviceToken(
        @Header("Authorization") accessToken: String,
        @Body request: DeviceTokenRequest
    ): Response<DeviceTokenResponse>
}

// Request/Response models
data class DeviceTokenRequest(val token: String)
data class DeviceTokenResponse(val statusCode: Int, val message: String, val success: Boolean)