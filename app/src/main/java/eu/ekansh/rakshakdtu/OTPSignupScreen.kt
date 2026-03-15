package eu.ekansh.rakshakdtu

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
fun OTPSignupScreen(email: String, navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    var otp by remember { mutableStateOf("") }

    LaunchedEffect(authViewModel.accessToken.value) {
        if (authViewModel.accessToken.value != null) {
            navController.navigate(Screen.DashboardScreen.route) {
                popUpTo(Screen.LoginScreen.route) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painterResource(R.drawable.logo_dtu), "DTU logo", modifier = Modifier.size(120.dp))
            Spacer(Modifier.height(16.dp))
            Text("DTU Rakshak", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
            Spacer(Modifier.height(8.dp))
            Text("Campus Vehicle Monitoring System")
            Spacer(Modifier.height(16.dp))
            Text("OTP sent to $email", fontSize = 12.sp)
            Spacer(Modifier.height(24.dp))

            OTPInput(otpLength = 6) { otp = it }

            Spacer(Modifier.height(24.dp))

            authViewModel.errorMessage.value?.let {
                Text(it, color = Color.Red, fontSize = 12.sp)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (otp.length == 6) authViewModel.verifySignupOtp(email, otp)
                    else authViewModel.errorMessage.value = "Please enter 6-digit OTP"
                },
                modifier = Modifier.fillMaxWidth(0.6f),
                enabled = !authViewModel.isLoading.value,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.lightGreen))
            ) {
                if (authViewModel.isLoading.value) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Verifying…", color = Color.White)
                } else {
                    Text("Verify and Sign In", color = Color.White)
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(0.6f),
                enabled = !authViewModel.isLoading.value,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.back_button_color))
            ) { Text("Back", color = Color.Black) }
        }

        AppToast(toastEvent = authViewModel.toastEvent)
    }
}

// ── Shared OTP input widget ────────────────────────────────────────────────────

@Composable
fun OTPInput(otpLength: Int = 6, onOtpComplete: (String) -> Unit) {
    var otp by remember { mutableStateOf("") }
    BasicTextField(
        value = otp,
        onValueChange = {
            if (it.length <= otpLength) {
                otp = it
                if (it.length == otpLength) onOtpComplete(it)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(otpLength) { index ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(50.dp).border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
                    ) {
                        Text(if (index < otp.length) otp[index].toString() else "", fontSize = 20.sp)
                    }
                }
            }
        }
    )
}
