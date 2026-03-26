package eu.ekansh.rakshakdtu

import eu.ekansh.rakshakdtu.data.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Attach current access token
        val accessToken = runBlocking { tokenManager.getToken() }
        val requestWithToken = originalRequest.newBuilder()
            .apply {
                if (accessToken != null) {
                    header("Authorization", "Bearer $accessToken")
                }
            }
            .build()

        val response = chain.proceed(requestWithToken)

        // If 401, try refreshing
        if (response.code == 401) {
            response.close()

            val refreshToken = runBlocking { tokenManager.getRefreshToken() }

            // No refresh token stored → force logout
            if (refreshToken == null) {
                runBlocking { ApiClient.triggerForceLogout() }
                return response
            }

            // Attempt token refresh (synchronous)
            val newAccessToken = runBlocking {
                try {
                    val refreshResponse = AuthRepository().getAccessToken(refreshToken)
                    if (refreshResponse.isSuccessful) {
                        val token = refreshResponse.body()?.data?.accessToken
                        if (token != null) {
                            tokenManager.saveToken(token)
                        }
                        token
                    } else {
                        // Refresh token is also expired/invalid → logout
                        ApiClient.triggerForceLogout()
                        null
                    }
                } catch (e: Exception) {
                    ApiClient.triggerForceLogout()
                    null
                }
            }

            // If we got a new token, retry the original request
            if (newAccessToken != null) {
                val retryRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
                return chain.proceed(retryRequest)
            }
        }

        return response
    }
}