package eu.ekansh.rakshakdtu

import androidx.compose.foundation.Image
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController
) {
    val authViewModel: AuthViewModel = viewModel()

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    var passwordSubmitted by remember { mutableStateOf(false) }
    var confirmPasswordSubmitted by remember { mutableStateOf(false) }
    var emailSubmitted by remember { mutableStateOf(false) }

    // Navigate to OTP screen when OTP is sent
    if (authViewModel.otpSent.value) {
        LaunchedEffect(Unit) {
            navController.navigate(Screen.OTPScreen.route + "/${email.value}")
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

        Text(text = "Create your admin account", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Email TextField
        OutlinedTextField(
            value = email.value,
            onValueChange = {
                email.value = it
            },
            label = { Text(text = "Email", color = Color.Black) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            textStyle = TextStyle(color = Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = R.color.black),
                unfocusedBorderColor = colorResource(id = R.color.grey),
                cursorColor = colorResource(id = R.color.black),
                focusedLabelColor = colorResource(id = R.color.black),
                unfocusedLabelColor = colorResource(id = R.color.grey)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        OutlinedTextField(
            value = password.value,
            onValueChange = {
                password.value = it
                passwordSubmitted = true
            },
            label = { Text(text = "Password (min 8 chars)", color = Color.Black) },
            isError = passwordSubmitted && password.value.length < 8,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            textStyle = TextStyle(color = Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = R.color.black),
                unfocusedBorderColor = colorResource(id = R.color.grey),
                cursorColor = colorResource(id = R.color.black),
                focusedLabelColor = colorResource(id = R.color.black),
                unfocusedLabelColor = colorResource(id = R.color.grey)
            )
        )

        if (passwordSubmitted && password.value.length < 8) {
            Text(text = "Password must be at least 8 characters", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password TextField
        OutlinedTextField(
            value = confirmPassword.value,
            onValueChange = {
                confirmPassword.value = it
                confirmPasswordSubmitted = true
            },
            label = { Text(text = "Confirm Password", color = Color.Black) },
            isError = confirmPasswordSubmitted && confirmPassword.value != password.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            textStyle = TextStyle(color = Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = R.color.black),
                unfocusedBorderColor = colorResource(id = R.color.grey),
                cursorColor = colorResource(id = R.color.black),
                focusedLabelColor = colorResource(id = R.color.black),
                unfocusedLabelColor = colorResource(id = R.color.grey)
            )
        )

        if (confirmPasswordSubmitted && confirmPassword.value != password.value) {
            Text(text = "Passwords do not match", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        // Register Button
        Button(
            onClick = {
                if (email.value.isNotEmpty() && password.value.length >= 8 && confirmPassword.value == password.value) {
                    authViewModel.signup(email.value, password.value)
                } else {
                    authViewModel.errorMessage.value = "Please fill all fields correctly"
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.lightGreen)
            ),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(text = "Continue With OTP", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign In Link
        Row {
            Text(text = "Already have an account?")
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Sign In",
                color = colorResource(id = R.color.lightGreen),
                modifier = Modifier.clickable {
                    navController.navigate(Screen.LoginScreen.route)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    // RegisterScreen()
}