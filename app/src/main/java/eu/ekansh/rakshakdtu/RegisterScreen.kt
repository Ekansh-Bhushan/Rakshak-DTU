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
fun RegisterScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()

    // ── Use authViewModel.email so the VM always knows the typed email ─────
    val email           = authViewModel.email
    val password        = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    var passwordSubmitted        by remember { mutableStateOf(false) }
    var confirmPasswordSubmitted by remember { mutableStateOf(false) }
    var passwordVisible          by remember { mutableStateOf(false) }
    var confirmPasswordVisible   by remember { mutableStateOf(false) }

    LaunchedEffect(authViewModel.otpSent.value) {
        if (authViewModel.otpSent.value) {
            navController.navigate(Screen.OTPSignupScreen.route + "/${email.value}")
        }
    }

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
            Text("Create your admin account", fontSize = 18.sp)
            Spacer(Modifier.height(16.dp))

            // ── Email — bound to authViewModel.email ──────────────────────
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },   // ← updates VM
                label = { Text("Email", color = Color.Black) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                textStyle = TextStyle(color = Color.Black),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.black),
                    unfocusedBorderColor = colorResource(R.color.grey),
                    cursorColor = colorResource(R.color.black)
                )
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it; passwordSubmitted = true },
                label = { Text("Password (min 8 chars)", color = Color.Black) },
                isError = passwordSubmitted && password.value.length < 8,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                textStyle = TextStyle(color = Color.Black),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, null)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.black),
                    unfocusedBorderColor = colorResource(R.color.grey),
                    cursorColor = colorResource(R.color.black)
                )
            )
            if (passwordSubmitted && password.value.length < 8) {
                Text("Password must be at least 8 characters", color = Color.Red, fontSize = 12.sp)
            }
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it; confirmPasswordSubmitted = true },
                label = { Text("Confirm Password", color = Color.Black) },
                isError = confirmPasswordSubmitted && confirmPassword.value != password.value,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                textStyle = TextStyle(color = Color.Black),
                singleLine = true,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, null)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.black),
                    unfocusedBorderColor = colorResource(R.color.grey),
                    cursorColor = colorResource(R.color.black)
                )
            )
            if (confirmPasswordSubmitted && confirmPassword.value != password.value) {
                Text("Passwords do not match", color = Color.Red, fontSize = 12.sp)
            }
            Spacer(Modifier.height(16.dp))

            authViewModel.errorMessage.value?.let {
                Text(it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp))
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (email.value.isNotEmpty() && password.value.length >= 8 && confirmPassword.value == password.value) {
                        authViewModel.signup(email.value, password.value)
                    } else {
                        authViewModel.errorMessage.value = "Please fill all fields correctly"
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
                Text("Already have an account?")
                Spacer(Modifier.width(4.dp))
                Text(
                    "Sign In",
                    color = colorResource(R.color.lightGreen),
                    modifier = Modifier.clickable { navController.navigate(Screen.LoginScreen.route) }
                )
            }
        }

        AppToast(toastEvent = authViewModel.toastEvent)
    }
}