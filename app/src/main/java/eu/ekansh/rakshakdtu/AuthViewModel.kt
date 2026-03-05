package eu.ekansh.rakshakdtu

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    var email = mutableStateOf("")
    var password = mutableStateOf("")

    var otpSent = mutableStateOf(false)
    var accessToken = mutableStateOf<String?>(null)
    var errorMessage = mutableStateOf<String?>(null)

    // ==================== SIGNUP ====================
    fun signup(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.signup(email, password)
                if (response.isSuccessful) {
                    this@AuthViewModel.email.value = email
                    this@AuthViewModel.password.value = password
                    otpSent.value = true
                    errorMessage.value = null
                } else {
                    errorMessage.value = response.message() ?: "Signup failed"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "An error occurred during signup"
            }
        }
    }

    // ==================== VERIFY SIGNUP OTP ====================
    fun verifySignupOtp(email: String, otp: String) {
        viewModelScope.launch {
            try {
                val response = repository.verifySignupOtp(email, otp)
                if (response.isSuccessful) {
                    accessToken.value = response.body()?.data?.accessToken
                    errorMessage.value = null
                } else {
                    errorMessage.value = "Invalid or expired OTP"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "An error occurred during OTP verification"
            }
        }
    }

    // ==================== SIGNIN ====================
    fun signIn() {
        viewModelScope.launch {
            try {
                val response = repository.signin(email.value, password.value)
                if (response.isSuccessful) {
                    otpSent.value = true
                    errorMessage.value = null
                } else {
                    errorMessage.value = response.message() ?: "Invalid credentials"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "An error occurred during signin"
            }
        }
    }

    // ==================== VERIFY SIGNIN OTP ====================
    fun verifySigninOtp(email: String, otp: String) {
        viewModelScope.launch {
            try {
                val response = repository.verifySigninOtp(email, otp)
                if (response.isSuccessful) {
                    accessToken.value = response.body()?.data?.accessToken
                    errorMessage.value = null
                } else {
                    errorMessage.value = "Invalid or expired OTP"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "An error occurred during OTP verification"
            }
        }
    }

    // ==================== HELPER ====================
    fun clearError() {
        errorMessage.value = null
    }

    fun reset() {
        email.value = ""
        password.value = ""
        otpSent.value = false
        accessToken.value = null
        errorMessage.value = null
    }
}