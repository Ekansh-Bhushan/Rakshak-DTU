package eu.ekansh.rakshakdtu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordOTPScreen(
    email: String,
    navController: NavHostController
) {
    val authViewModel: AuthViewModel = viewModel()

    var otp                by remember { mutableStateOf("") }
    var newPassword        by remember { mutableStateOf("") }
    var passwordVisible    by remember { mutableStateOf(false) }

    // On success the VM resets forgotPasswordOtpSent → false and emits a toast.
    // Navigate back to LoginScreen so the user signs in with the new password.
    LaunchedEffect(authViewModel.forgotPasswordOtpSent.value) {
        if (!authViewModel.forgotPasswordOtpSent.value
            && otp.isNotEmpty()         // guard against initial false trigger
        ) {
            navController.navigate(Screen.LoginScreen.route) {
                popUpTo(Screen.ForgotPasswordScreen.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) { authViewModel.clearError() }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter           = painterResource(R.drawable.logo_dtu),
                contentDescription = "DTU logo",
                modifier          = Modifier.size(120.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text("DTU Rakshak", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
            Spacer(Modifier.height(8.dp))
            Text("Campus Vehicle Monitoring System")
            Spacer(Modifier.height(16.dp))
            Text("OTP sent to $email", fontSize = 12.sp)
            Spacer(Modifier.height(24.dp))

            // ── OTP input (reuses your existing OTPInput composable) ──────
            OTPInput(otpLength = 6) { otp = it }
            Spacer(Modifier.height(16.dp))

            // ── New password field ────────────────────────────────────────
            OutlinedTextField(
                value         = newPassword,
                onValueChange = {
                    newPassword = it
                    authViewModel.clearError()
                },
                label         = { Text("New Password", color = Color.Black) },
                modifier      = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                textStyle     = TextStyle(color = Color.Black),
                singleLine    = true,
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction    = ImeAction.Done
                ),
                trailingIcon  = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector        = if (passwordVisible)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor   = colorResource(R.color.black),
                    unfocusedBorderColor = colorResource(R.color.grey),
                    cursorColor          = colorResource(R.color.black)
                )
            )
            Spacer(Modifier.height(8.dp))

            authViewModel.errorMessage.value?.let {
                Text(
                    it,
                    color    = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    when {
                        otp.length != 6         ->
                            authViewModel.errorMessage.value = "Please enter the 6-digit OTP"
                        newPassword.length < 8  ->
                            authViewModel.errorMessage.value = "Password must be at least 8 characters"
                        else                    ->
                            authViewModel.verifyForgotPasswordOtp(email, otp, newPassword)
                    }
                },
                enabled  = !authViewModel.isLoading.value,
                colors   = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.lightGreen)),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                if (authViewModel.isLoading.value) {
                    CircularProgressIndicator(
                        color       = Color.White,
                        strokeWidth = 2.dp,
                        modifier    = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Resetting…", color = Color.White)
                } else {
                    Text("Reset Password", color = Color.White)
                }
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick  = { navController.popBackStack() },
                enabled  = !authViewModel.isLoading.value,
                colors   = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.back_button_color)),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Back", color = Color.Black)
            }
        }

        AppToast(toastEvent = authViewModel.toastEvent)
    }
}