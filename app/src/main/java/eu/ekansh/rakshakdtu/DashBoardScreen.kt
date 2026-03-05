package eu.ekansh.rakshakdtu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import eu.ekansh.rakshakdtu.data.TokenManager
import kotlinx.coroutines.launch

@Composable
fun DashBoardScreen(navController: NavHostController? = null) {
    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to DTU Rakshak",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Campus Vehicle Monitoring System",
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "Dashboard",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 32.dp)
        )

        // Your dashboard content goes here
        Text(
            text = "Add your dashboard widgets and features here",
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 24.dp),
            color = Color.Gray
        )

        // Logout Button
        Button(
            onClick = {
                scope.launch {
                    // Clear token from storage
                    tokenManager.clearToken()

                    // Navigate back to login and clear backstack
                    navController?.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.DashboardScreen.route) { inclusive = true }
                    }
                }
            },
            modifier = Modifier.padding(top = 48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.back_button_color)
            )
        ) {
            Text(text = "Logout", color = Color.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashBoardScreenPreview() {
    DashBoardScreen()
}