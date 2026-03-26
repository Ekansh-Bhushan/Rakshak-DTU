package eu.ekansh.rakshakdtu

import org.json.JSONObject
import retrofit2.Response

fun <T> Response<T>.errorMessage(): String {
    return try {
        val json = JSONObject(errorBody()?.string() ?: "")
        json.optString("message", "Something went wrong")
    } catch (e: Exception) {
        message() ?: "Something went wrong"
    }
}

class AuthRepository {

    suspend fun signup(email: String, password: String): Response<SignupResponse> {
        val request = SignupRequest(email, password)
        return rakshakService.signup(request)
    }

    suspend fun verifySignupOtp(email: String, otp: String): Response<VerifyOtpResponse> {
        val request = VerifyOtpRequest(email, otp)
        return rakshakService.verifySignupOtp(request)
    }

    suspend fun signin(email: String, password: String): Response<SigninResponse> {
        val request = SigninRequest(email, password)
        return rakshakService.signin(request)
    }

    suspend fun verifySigninOtp(email: String, otp: String): Response<VerifyOtpResponse> {
        val request = VerifyOtpRequest(email, otp)
        return rakshakService.verifySigninOtp(request)
    }

    suspend fun forgotPassword(email: String): Response<ForgotPasswordResponse> =
        rakshakService.forgotPassword(ForgotPasswordRequest(email))

    suspend fun verifyForgotPasswordOtp(
        email: String,
        otp: String,
        newPassword: String
    ): Response<VerifyForgotPasswordResponse> =
        rakshakService.verifyForgotPasswordOtp(
            VerifyForgotPasswordRequest(email, otp, newPassword)
        )

    suspend fun updatePassword(
        token: String,
        currentPassword: String,
        newPassword: String
    ): Response<UpdatePasswordResponse> =
        rakshakService.updatePassword(
            bearerToken = "Bearer $token",
            request = UpdatePasswordRequest(currentPassword, newPassword)
        )
}