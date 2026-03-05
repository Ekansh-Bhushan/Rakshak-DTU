package eu.ekansh.rakshakdtu

data class SigninRequest(
    val email: String,
    val password: String
)

data class SigninResponse(
    val statusCode: Int,
    val data: EmailData,
    val message: String
)

data class EmailData(
    val email: String
)