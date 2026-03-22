package eu.ekansh.rakshakdtu

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePasswordScreen(
    rootNavController: NavHostController    // needs root nav to go to LoginScreen on success
) {
    val authViewModel: AuthViewModel = viewModel()

    var currentPassword        by remember { mutableStateOf("") }
    var newPassword            by remember { mutableStateOf("") }
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible     by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { authViewModel.clearError() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Update Password",
                fontWeight = FontWeight.ExtraBold,
                fontSize   = 22.sp,
                color      = Color.White
            )
            Text(
                "Enter your current password and choose a new one.",
                fontSize = 13.sp,
                color    = Color.Gray
            )
            Spacer(Modifier.height(8.dp))

            // ── Current password ─────────────────────────────────────────
            OutlinedTextField(
                value         = currentPassword,
                onValueChange = {
                    currentPassword = it
                    authViewModel.clearError()
                },
                label         = { Text("Current Password", color = Color.Gray) },
                modifier      = Modifier.fillMaxWidth(),
                textStyle     = TextStyle(color = Color.White),
                singleLine    = true,
                visualTransformation = if (currentPasswordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction    = ImeAction.Next
                ),
                trailingIcon = {
                    IconButton(onClick = { currentPasswordVisible = !currentPasswordVisible }) {
                        Icon(
                            imageVector = if (currentPasswordVisible)
                                Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor   = colorResource(R.color.lightGreen),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor          = Color.White
                )
            )

            // ── New password ─────────────────────────────────────────────
            OutlinedTextField(
                value         = newPassword,
                onValueChange = {
                    newPassword = it
                    authViewModel.clearError()
                },
                label         = { Text("New Password", color = Color.Gray) },
                modifier      = Modifier.fillMaxWidth(),
                textStyle     = TextStyle(color = Color.White),
                singleLine    = true,
                visualTransformation = if (newPasswordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction    = ImeAction.Done
                ),
                trailingIcon = {
                    IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                        Icon(
                            imageVector = if (newPasswordVisible)
                                Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor   = colorResource(R.color.lightGreen),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor          = Color.White
                )
            )

            // ── Error ────────────────────────────────────────────────────
            authViewModel.errorMessage.value?.let {
                Text(it, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(Modifier.height(4.dp))

            // ── Submit ───────────────────────────────────────────────────
            Button(
                onClick = {
                    when {
                        currentPassword.isEmpty() ->
                            authViewModel.errorMessage.value = "Please enter your current password"
                        newPassword.length < 8 ->
                            authViewModel.errorMessage.value = "New password must be at least 8 characters"
                        currentPassword == newPassword ->
                            authViewModel.errorMessage.value = "New password must differ from current password"
                        else -> authViewModel.updatePassword(
                            currentPassword = currentPassword,
                            newPassword     = newPassword,
                            onSuccess       = {
                                rootNavController.navigate(Screen.LoginScreen.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                },
                enabled  = !authViewModel.isLoading.value,
                colors   = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.lightGreen)),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                if (authViewModel.isLoading.value) {
                    CircularProgressIndicator(
                        color       = Color.White,
                        strokeWidth = 2.dp,
                        modifier    = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Updating…", color = Color.White)
                } else {
                    Text("Update Password", color = Color.White)
                }
            }
        }

        // ── Toast overlay ────────────────────────────────────────────────
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            AppToast(toastEvent = authViewModel.toastEvent)
        }
    }
}