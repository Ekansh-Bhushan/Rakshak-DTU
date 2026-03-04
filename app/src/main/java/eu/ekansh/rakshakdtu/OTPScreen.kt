package eu.ekansh.rakshakdtu

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.ui.input.key.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPScreen(
    email : String,
    navController: NavHostController
){
    var otpValues = remember { mutableStateListOf("", "", "", "", "", "") }
    val otpCode = otpValues.joinToString("")

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

        Text(text = "OTP sent to $email", //TODO email from the login and register page
            fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OTPInput(
            otpLength = 6
        ) { otp ->
            println("Entered OTP: $otp")
        }




        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // TODO check if the OTP is correct or not
            navController.navigate(Screen.DashboardScreen.route)
        },
            modifier = Modifier.fillMaxWidth(0.50f),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.lightGreen)
            )
        ) {
            Text(text = "Verify and SignIn", color = Color.White)
        }

        Button(onClick = {
            navController.navigate(Screen.LoginScreen.route)
        },
            modifier = Modifier.fillMaxWidth(0.50f),
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

                        val char =
                            when {
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

                            Text(
                                text = char,
                                fontSize = 20.sp
                            )
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
//    OTPScreen("ekanshbhushan@gmail.com")
}