package eu.ekansh.rakshakdtu

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

val rakshakService = ApiClient.apiService

interface APIService {

    @POST("signup")
    suspend fun signup(
        @Body request: SignupRequest
    ): Response<SignupResponse>

    @POST("signup/verify-otp")
    suspend fun verifySignupOtp(
        @Body request: VerifyOtpRequest
    ): Response<VerifyOtpResponse>

    @POST("signin")
    suspend fun signin(
        @Body request: SigninRequest
    ): Response<SigninResponse>

    @POST("signin/verify-otp")
    suspend fun verifySigninOtp(
        @Body request: VerifyOtpRequest
    ): Response<VerifyOtpResponse>

    @POST("forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Response<ForgotPasswordResponse>

    @POST("forgot-password/verify-otp")
    suspend fun verifyForgotPasswordOtp(
        @Body request: VerifyForgotPasswordRequest
    ): Response<VerifyForgotPasswordResponse>

    @POST("update-password")
    suspend fun updatePassword(
        @Header("Authorization") bearerToken: String,
        @Body request: UpdatePasswordRequest
    ): Response<UpdatePasswordResponse>
}