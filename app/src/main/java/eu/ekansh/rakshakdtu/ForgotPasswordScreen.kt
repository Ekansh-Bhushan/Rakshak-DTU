package eu.ekansh.rakshakdtu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
fun ForgotPasswordScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()

    // Use the dedicated forgot-password state, not the signin email state
    val email = authViewModel.forgotPasswordEmail

    // Navigate to OTP screen once OTP is sent
    LaunchedEffect(authViewModel.forgotPasswordOtpSent.value) {
        if (authViewModel.forgotPasswordOtpSent.value) {
            navController.navigate(Screen.ForgotPasswordOTPScreen.route + "/${email.value}")
        }
    }

    // Clear any stale error when entering screen
    LaunchedEffect(Unit) {
        authViewModel.clearError()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_dtu),
                contentDescription = "DTU logo",
                modifier = Modifier.size(120.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text("DTU Rakshak", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
            Spacer(Modifier.height(8.dp))
            Text("Campus Vehicle Monitoring System")
            Spacer(Modifier.height(24.dp))
            Text(
                "Enter your registered email to receive a password reset OTP.",
                fontSize = 12.sp
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = email.value,
                onValueChange = {
                    email.value = it
                    authViewModel.clearError()
                },
                label = { Text("Email", color = Color.Black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                textStyle = TextStyle(color = Color.Black),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction    = ImeAction.Done
                ),
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
                    if (email.value.isNotBlank()) {
                        authViewModel.forgotPassword(email.value.trim())
                    } else {
                        authViewModel.errorMessage.value = "Please enter your email"
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
                    Text("Sending OTP…", color = Color.White)
                } else {
                    Text("Send OTP", color = Color.White)
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "Back to Sign In",
                color    = colorResource(R.color.lightGreen),
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    authViewModel.resetForgotPassword()
                    navController.popBackStack()
                }
            )
        }

        AppToast(toastEvent = authViewModel.toastEvent)
    }
}