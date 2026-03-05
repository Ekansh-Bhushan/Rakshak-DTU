package eu.ekansh.rakshakdtu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {

    var email = mutableStateOf("")
    var password = mutableStateOf("")

    var otpSent = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun signIn() {

        viewModelScope.launch {

            try {

                val request = SigninRequest(
                    email = email.value,
                    password = password.value
                )

                val response = rakshakService.signin(request)

                if (response.isSuccessful) {
                    otpSent.value = true
                } else {
                    errorMessage.value = "Invalid credentials"
                }

            } catch (e: Exception) {
                errorMessage.value = e.message
            }

        }

    }
}