package eu.ekansh.rakshakdtu

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import eu.ekansh.rakshakdtu.data.TokenManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val tokenManager = TokenManager(application)

    var email        = mutableStateOf("")
    var password     = mutableStateOf("")
    var otpSent      = mutableStateOf(false)
    var accessToken  = mutableStateOf<String?>(null)
    var errorMessage = mutableStateOf<String?>(null)
    var isLoading    = mutableStateOf(false)

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    // ✅ storedEmail is the single source of truth for the drawer.
    //    Set immediately on successful OTP verify (in-memory, instant).
    //    Also loaded from DataStore on cold start via init{}.
    var storedEmail = mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            storedEmail.value = tokenManager.getEmail()
        }
    }

    // ── SIGNUP ────────────────────────────────────────────────────────────
    fun signup(email: String, password: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val response = AuthRepository().signup(email, password)
                if (response.isSuccessful) {
                    this@AuthViewModel.email.value = email
                    this@AuthViewModel.password.value = password
                    otpSent.value = true
                    _toastEvent.emit("OTP sent to $email ✓")
                } else {
                    errorMessage.value = response.message() ?: "Signup failed"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "An error occurred"
            } finally {
                isLoading.value = false
            }
        }
    }

    // ── VERIFY SIGNUP OTP ─────────────────────────────────────────────────
    fun verifySignupOtp(email: String, otp: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val response = AuthRepository().verifySignupOtp(email, otp)
                if (response.isSuccessful) {
                    val token = response.body()?.data?.accessToken
                    accessToken.value = token
                    token?.let {
                        tokenManager.saveToken(it)
                        tokenManager.saveEmail(email)
                        storedEmail.value = email   // ✅ instant in-memory update
                    }
                    _toastEvent.emit("Account verified! Welcome 🎉")
                } else {
                    errorMessage.value = "Invalid or expired OTP"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "An error occurred"
            } finally {
                isLoading.value = false
            }
        }
    }

    // ── SIGNIN ────────────────────────────────────────────────────────────
    fun signIn() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val response = AuthRepository().signin(email.value, password.value)
                if (response.isSuccessful) {
                    otpSent.value = true
                    _toastEvent.emit("OTP sent to ${email.value} ✓")
                } else {
                    errorMessage.value = response.message() ?: "Invalid credentials"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "An error occurred"
            } finally {
                isLoading.value = false
            }
        }
    }

    // ── VERIFY SIGNIN OTP ─────────────────────────────────────────────────
    fun verifySigninOtp(email: String, otp: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val response = AuthRepository().verifySigninOtp(email, otp)
                if (response.isSuccessful) {
                    val token = response.body()?.data?.accessToken
                    accessToken.value = token
                    token?.let {
                        tokenManager.saveToken(it)
                        tokenManager.saveEmail(email)
                        storedEmail.value = email   // ✅ instant in-memory update
                    }
                    _toastEvent.emit("Welcome back! ✓")
                } else {
                    errorMessage.value = "Invalid or expired OTP"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "An error occurred"
            } finally {
                isLoading.value = false
            }
        }
    }

    // ── SIGN OUT ──────────────────────────────────────────────────────────
    // suspend so the drawer's scope.launch{} awaits clearAll() before navigating
    suspend fun signOut() {
        tokenManager.clearAll()        // waits for DataStore write to complete
        storedEmail.value  = null
        accessToken.value  = null
        email.value        = ""
        password.value     = ""
        otpSent.value      = false
        errorMessage.value = null
    }

    fun clearError() { errorMessage.value = null }

    fun reset() {
        email.value = ""; password.value = ""
        otpSent.value = false; accessToken.value = null; errorMessage.value = null
    }
}