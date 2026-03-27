package eu.ekansh.rakshakdtu

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
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
    var forgotPasswordEmail    = mutableStateOf("")
    var forgotPasswordOtpSent  = mutableStateOf(false)
    private val _navigateToLogin = MutableSharedFlow<Unit>()
    val navigateToLogin = _navigateToLogin.asSharedFlow()


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
                    android.util.Log.d("FCM", "✅ Token registered: ${response.body()}")
                    val body = response.body()?.data
                    body?.let {
                        tokenManager.saveToken(it.accessToken)
                        tokenManager.saveRefreshToken(it.refreshToken)
                        tokenManager.saveEmail(email)
                        accessToken.value  = it.accessToken
                        storedEmail.value  = email
                        registerFcmToken(it.accessToken)
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
                    android.util.Log.d("FCM", "✅ Token registered: ${response.body()}")
                    val body = response.body()?.data
                    body?.let {
                        tokenManager.saveToken(it.accessToken)
                        tokenManager.saveRefreshToken(it.refreshToken)
                        tokenManager.saveEmail(email)
                        accessToken.value  = it.accessToken
                        storedEmail.value  = email
                        registerFcmToken(it.accessToken)
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

    // ── FORGOT PASSWORD — step 1 ──────────────────────────────────────────
    fun forgotPassword(email: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val response = AuthRepository().forgotPassword(email)
                if (response.isSuccessful) {
                    forgotPasswordEmail.value   = email
                    forgotPasswordOtpSent.value = true
                    _toastEvent.emit("OTP sent to $email ✓")
                } else {
                    errorMessage.value = response.errorMessage()
                }
            } catch (e: Exception) {
                errorMessage.value = "Network error. Check your connection."
            } finally {
                isLoading.value = false
            }
        }
    }

    // ── FORGOT PASSWORD — step 2 ──────────────────────────────────────────
    fun verifyForgotPasswordOtp(email: String, otp: String, newPassword: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val response = AuthRepository().verifyForgotPasswordOtp(email, otp, newPassword)
                if (response.isSuccessful) {
                    forgotPasswordOtpSent.value = false
                    forgotPasswordEmail.value   = ""
                    _toastEvent.emit("Password reset! Please sign in ✓")
                    _navigateToLogin.emit(Unit)   // ← this line must be present
                } else {
                    errorMessage.value = response.errorMessage()
                }
            } catch (e: Exception) {
                errorMessage.value = "Network error. Check your connection."
            } finally {
                isLoading.value = false
            }
        }
    }
    // ── UPDATE PASSWORD ───────────────────────────────────────────────────
    fun updatePassword(
        currentPassword: String,
        newPassword: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                val token = tokenManager.getToken()
                if (token == null) {
                    errorMessage.value = "Session expired. Please sign in again."
                    return@launch
                }
                val response = AuthRepository().updatePassword(token, currentPassword, newPassword)
                if (response.isSuccessful) {
                    tokenManager.clearAll()
                    storedEmail.value  = null
                    accessToken.value  = null
                    email.value        = ""
                    password.value     = ""
                    _toastEvent.emit("Password updated! Please sign in again ✓")
                    onSuccess()
                } else {
                    errorMessage.value = response.errorMessage()
                }
            } catch (e: Exception) {
                errorMessage.value = "Network error. Check your connection."
            } finally {
                isLoading.value = false
            }
        }
    }

    fun resetForgotPassword() {
        forgotPasswordEmail.value   = ""
        forgotPasswordOtpSent.value = false
        errorMessage.value          = null
    }

    fun getAccessToken(refreshToken : String){
        viewModelScope.launch {
            try {
                val response = AuthRepository().getAccessToken(refreshToken)
                if (response.isSuccessful){

                }
            } catch (e: Exception){
                errorMessage.value = e.message ?: "Did not found the token"
            }
        }
    }

    private fun registerFcmToken(accessToken: String) {
        viewModelScope.launch {
            try {
                // Check if Firebase is actually ready
                val messaging = FirebaseMessaging.getInstance()

                messaging.token.addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        android.util.Log.e("FCM", "❌ Token fetch failed", task.exception)
                        return@addOnCompleteListener
                    }

                    val fcmToken = task.result
                    android.util.Log.d("FCM", "🚀 Token fetched: ${fcmToken.take(10)}...")

                    viewModelScope.launch {
                        try {
                            val response = AllAPIClient.deviceApiService.registerDeviceToken(
                                accessToken = "Bearer $accessToken",
                                request = DeviceTokenRequest(fcmToken)
                            )
                            if (response.isSuccessful) {
                                android.util.Log.d("FCM", "✅ DB Updated Successfully")
                            } else {
                                android.util.Log.e("FCM", "❌ Server Error: ${response.code()} ${response.errorBody()?.string()}")
                            }
                        } catch (e: Exception) {
                            android.util.Log.e("FCM", "❌ Network Error: ${e.message}")
                        }
                    }
                }
            } catch (e: IllegalStateException) {
                android.util.Log.e("FCM", "❌ Firebase not ready yet: ${e.message}")
            }
        }
    }
}

