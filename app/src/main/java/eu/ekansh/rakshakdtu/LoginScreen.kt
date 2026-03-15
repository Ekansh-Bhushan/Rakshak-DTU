package eu.ekansh.rakshakdtu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
fun LoginScreen(navController: NavHostController) {
    var submitted by remember { mutableStateOf(false) }
    val authViewModel: AuthViewModel = viewModel()
    val email    = authViewModel.email       // ← email lives in VM, typed by user
    val password = authViewModel.password
    var passwordVisible by remember { mutableStateOf(false) }

    // Navigate when OTP sent
    LaunchedEffect(authViewModel.otpSent.value) {
        if (authViewModel.otpSent.value) {
            // email.value is already set from the text field via authViewModel.email
            navController.navigate(Screen.OTPScreen.route + "/${email.value}")
        }
    }

    // Wrap everything in a Box so AppToast can overlay at the bottom
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
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
            Spacer(Modifier.height(16.dp))
            Text("Sign In to Access Campus Vehicle Monitoring Dashboard", fontSize = 12.sp)
            Spacer(Modifier.height(16.dp))

            // ── Email — bound directly to authViewModel.email ─────────────
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },   // ← updates VM state
                label = { Text("Email", color = Color.Black) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                textStyle = TextStyle(color = Color.Black),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor   = colorResource(R.color.black),
                    unfocusedBorderColor = colorResource(R.color.grey),
                    cursorColor          = colorResource(R.color.black)
                )
            )
            Spacer(Modifier.height(16.dp))

            // ── Password ──────────────────────────────────────────────────
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it; submitted = true },
                label = { Text("Password", color = Color.Black) },
                isError = submitted && password.value.length < 8,
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                textStyle = TextStyle(color = Color.Black),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, null)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor   = colorResource(R.color.black),
                    unfocusedBorderColor = colorResource(R.color.grey),
                    cursorColor          = colorResource(R.color.black)
                )
            )
            if (submitted && password.value.length < 8) {
                Text("Password must be at least 8 characters", color = Color.Red, fontSize = 12.sp)
            }
            Spacer(Modifier.height(16.dp))

            authViewModel.errorMessage.value?.let {
                Text(it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp))
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (email.value.isNotEmpty() && password.value.length >= 8) {
                        authViewModel.signIn()
                    } else {
                        authViewModel.errorMessage.value = "Please enter valid email and password"
                    }
                },
                enabled = !authViewModel.isLoading.value,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.lightGreen)),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                if (authViewModel.isLoading.value) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Sending OTP…", color = Color.White)
                } else {
                    Text("Continue With OTP", color = Color.White)
                }
            }

            Spacer(Modifier.height(16.dp))
            Row {
                Text("New Here?")
                Spacer(Modifier.width(4.dp))
                Text(
                    "Create Account",
                    color = colorResource(R.color.lightGreen),
                    modifier = Modifier.clickable { navController.navigate(Screen.RegisterScreen.route) }
                )
            }
        }

        // ── Custom in-app toast (no Android icon) ─────────────────────────
        AppToast(toastEvent = authViewModel.toastEvent)
    }
}