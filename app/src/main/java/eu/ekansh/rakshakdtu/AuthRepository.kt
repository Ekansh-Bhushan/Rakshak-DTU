package eu.ekansh.rakshakdtu

import retrofit2.Response

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
}