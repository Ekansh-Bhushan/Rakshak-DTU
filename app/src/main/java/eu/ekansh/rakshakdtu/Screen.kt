package eu.ekansh.rakshakdtu

sealed class Screen(val route:String) {
    object LoginScreen: Screen("logInScreen")
    object RegisterScreen: Screen("registerScreen")
    object OTPScreen: Screen("otpScreen")
    object DashboardScreen: Screen("dashboard")
}