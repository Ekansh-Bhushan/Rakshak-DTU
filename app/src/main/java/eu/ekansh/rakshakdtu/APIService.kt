package eu.ekansh.rakshakdtu

import eu.ekansh.rakshakdtu.ApiClient.apiService
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

val rakshakService = apiService
interface APIService{

    @POST("signin")
    suspend fun signin(
        @Body request: SigninRequest
    ): Response<SigninResponse>
}