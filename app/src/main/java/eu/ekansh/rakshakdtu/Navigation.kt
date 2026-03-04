package eu.ekansh.rakshakdtu

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController()
){
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ){
        composable(Screen.LoginScreen.route){
            LoginScreen(navController=navController)
        }

        composable(Screen.RegisterScreen.route){
            RegisterScreen(navController=navController)
        }

        composable(Screen.OTPScreen.route + "/{email}",arguments = listOf(
            navArgument("email"){
                type = NavType.StringType
                defaultValue = ""
            }
        )){

            val email = it.arguments?.getString("email") ?: ""
            OTPScreen(email = email,navController=navController)
        }

        composable(Screen.DashboardScreen.route){
            DashBoardScreen()
        }
    }
}