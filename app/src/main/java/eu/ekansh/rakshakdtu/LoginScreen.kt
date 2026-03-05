package eu.ekansh.rakshakdtu

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun LoginScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    var submitted by remember { mutableStateOf(false) }

    val email = authViewModel.email
    val password = authViewModel.password

    LaunchedEffect(authViewModel.otpSent.value) {
        if (authViewModel.otpSent.value) {
            navController.navigate(Screen.OTPScreen.route + "/${authViewModel.email.value}")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(id = R.drawable.logo_dtu),
            contentDescription = "DTU logo",
            modifier = Modifier.size(120.dp))
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "DTU Rakshak",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Campus Vehicle Monitoring System")
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Sign In to Access Campus Vehicle Monitoring Dashboard",
            fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email.value,
            onValueChange = {
                email.value = it
            },
            label = { androidx.compose.material3.Text(text = "Email", color = Color.Black) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
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

        OutlinedTextField(
            value = password.value,
            onValueChange = {
                password.value = it
                submitted = true
            },
            label = { androidx.compose.material3.Text(text = "Password", color = Color.Black) },
            isError = submitted && password.value.length < 8,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            authViewModel.signIn()
        },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.lightGreen)
                )
            ) {
            Text(text = "Continue With OTP", color = Color.White)
        }

        Row {
            Text(text = "New Here ?")
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Create Account",
                color = colorResource(id = R.color.lightGreen),
                modifier = Modifier.clickable {
                    navController.navigate(Screen.RegisterScreen.route)
                }
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
//    LoginScreen()
}