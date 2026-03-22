package eu.ekansh.rakshakdtu

// ==================== SIGNUP ====================
data class SignupRequest(
    val email: String,
    val password: String
)

data class SignupResponse(
    val statusCode: Int,
    val data: EmailData,
    val message: String
)

// ==================== SIGNIN ====================
data class SigninRequest(
    val email: String,
    val password: String
)

data class SigninResponse(
    val statusCode: Int,
    val data: EmailData,
    val message: String
)

// ==================== VERIFY OTP ====================
data class VerifyOtpRequest(
    val email: String,
    val otp: String
)

data class VerifyOtpResponse(
    val statusCode: Int,
    val data: TokenData,
    val message: String
)

// ==================== SHARED DATA CLASSES ====================
data class EmailData(
    val email: String
)

data class TokenData(
    val accessToken: String,
    val email: String
)

// ==================== FORGOT PASSWORD ====================
data class ForgotPasswordRequest(
    val email: String
)

data class ForgotPasswordResponse(
    val statusCode: Int,
    val data: EmailData,
    val message: String
)

data class VerifyForgotPasswordRequest(
    val email: String,
    val otp: String,
    val newPassword: String
)

data class VerifyForgotPasswordResponse(
    val statusCode: Int,
    val data: Any?,
    val message: String
)

// ==================== UPDATE PASSWORD ====================
data class UpdatePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)

data class UpdatePasswordResponse(
    val statusCode: Int,
    val data: Any?,
    val message: String
)