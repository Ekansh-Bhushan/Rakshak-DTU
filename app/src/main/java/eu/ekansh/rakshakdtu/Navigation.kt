package eu.ekansh.rakshakdtu

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.ekansh.rakshakdtu.data.TokenManager
import kotlinx.coroutines.launch

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    val scope = rememberCoroutineScope()

    var startDestination by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Check for existing token on app start
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val savedToken = tokenManager.getToken()
                startDestination = if (savedToken != null) {
                    // Token exists, go to dashboard
                    Screen.DashboardScreen.route
                } else {
                    // No token, go to login
                    Screen.LoginScreen.route
                }
                isLoading = false
            } catch (e: Exception) {
                // Error reading token, default to login
                startDestination = Screen.LoginScreen.route
                isLoading = false
            }
        }
    }

    // Show loading screen while checking token
    if (isLoading || startDestination == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    NavHost(
        navController = navController,
        startDestination = startDestination!!
    ) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }

        composable(
            Screen.OTPScreen.route + "/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            OTPScreen(email = email, navController = navController)
        }

        composable(
            Screen.OTPSignupScreen.route + "/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            OTPSignupScreen(email = email, navController = navController)
        }

        composable(Screen.DashboardScreen.route) {
            AppBarView(navController = navController)
        }

        composable(Screen.CameraScreen.route) {
            CameraScreen()
        }

        composable(Screen.VehicleScreen.route) {
            VehicleScreen()
        }

        composable(Screen.LogScreen.route) {
            LogScreen()
        }
    }
}