package eu.ekansh.rakshakdtu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.ekansh.rakshakdtu.data.TokenManager

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {

        // ── Splash ────────────────────────────────────────────────────────────
        composable(Screen.SplashScreen.route) {
            val context = LocalContext.current
            val tokenManager = remember { TokenManager(context) }

            // SplashScreen now receives a suspend-safe onFinished
            SplashScreen(
                tokenManager = tokenManager,
                onFinished = { destination ->
                    navController.navigate(destination) {
                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                }
            )
        }

        // ── Auth ──────────────────────────────────────────────────────────────
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }

        composable(
            Screen.OTPScreen.route + "/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            OTPScreen(email = backStackEntry.arguments?.getString("email") ?: "", navController = navController)
        }

        composable(
            Screen.OTPSignupScreen.route + "/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            OTPSignupScreen(email = backStackEntry.arguments?.getString("email") ?: "", navController = navController)
        }

        // ── App ───────────────────────────────────────────────────────────────
        composable(Screen.DashboardScreen.route) {
            AppBarView(navController = navController)
        }

        composable(Screen.CameraScreen.route) {
            CameraScreen(navController = navController)
        }

        composable(Screen.VehicleScreen.route) {
            VehicleScreen(navController = navController)
        }

        composable(Screen.LogScreen.route) {
            LogScreen()
        }
    }
}