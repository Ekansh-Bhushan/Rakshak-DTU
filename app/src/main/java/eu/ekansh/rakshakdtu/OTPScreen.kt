package eu.ekansh.rakshakdtu

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import eu.ekansh.rakshakdtu.data.TokenManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPScreen(
    email: String,
    navController: NavHostController
) {
    val authViewModel: AuthViewModel = viewModel()
    var otp by remember { mutableStateOf("") }
    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    val scope = rememberCoroutineScope()
    var isNavigating by remember { mutableStateOf(false) }

    // Handle successful OTP verification
    LaunchedEffect(authViewModel.accessToken.value) {
        val token = authViewModel.accessToken.value

        // Only navigate if token exists and we haven't already started navigating
        if (token != null && !isNavigating) {
            isNavigating = true

            // Save token first, then navigate
            scope.launch {
                try {
                    tokenManager.saveToken(token)

                    // Navigate to dashboard after token is saved
                    navController.navigate(Screen.DashboardScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                } catch (e: Exception) {
                    authViewModel.errorMessage.value = "Failed to save session: ${e.message}"
                    isNavigating = false
                }
            }
        }
    }

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
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "DTU Rakshak",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Campus Vehicle Monitoring System")
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "OTP sent to $email",
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // OTP Input Boxes
        OTPInput(otpLength = 6) { enteredOtp ->
            otp = enteredOtp
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Error Message Display
        if (authViewModel.errorMessage.value != null) {
            Text(
                text = authViewModel.errorMessage.value ?: "",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Verify Button
        Button(
            onClick = {
                if (otp.length == 6) {
                    authViewModel.verifySigninOtp(email, otp)
                } else {
                    authViewModel.errorMessage.value = "Please enter 6-digit OTP"
                }
            },
            modifier = Modifier.fillMaxWidth(0.6f),
            enabled = !isNavigating,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.lightGreen)
            )
        ) {
            Text(text = "Verify and SignIn", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Back Button
        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(0.6f),
            enabled = !isNavigating,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.back_button_color)
            )
        ) {
            Text(text = "Back", color = Color.Black)
        }
    }
}

@Composable
fun OTPInput(
    otpLength: Int = 6,
    onOtpComplete: (String) -> Unit
) {
    var otp by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BasicTextField(
            value = otp,
            onValueChange = {
                if (it.length <= otpLength) {
                    otp = it

                    if (it.length == otpLength) {
                        onOtpComplete(it)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(otpLength) { index ->
                        val char = when {
                            index >= otp.length -> ""
                            else -> otp[index].toString()
                        }

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(50.dp)
                                .border(
                                    1.dp,
                                    Color.LightGray,
                                    RoundedCornerShape(10.dp)
                                )
                        ) {
                            Text(text = char, fontSize = 20.sp)
                        }
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OTPScreenPreview() {
    // OTPScreen(email = "test@example.com", navController = rememberNavController())
}