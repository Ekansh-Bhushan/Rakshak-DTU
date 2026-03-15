package eu.ekansh.rakshakdtu

sealed class Screen(val route:String) {
    object LoginScreen: Screen("logInScreen")
    object RegisterScreen: Screen("registerScreen")
    object OTPScreen: Screen("otpScreen")

    object OTPSignupScreen: Screen("otpSignupScreen")
    object DashboardScreen: Screen("dashboard")

    object CameraScreen : Screen("cameraScreen")

    object VehicleScreen : Screen("vehicleScreen")

    object LogScreen : Screen("logScreen")

    object SplashScreen : Screen("splashScreen")
}