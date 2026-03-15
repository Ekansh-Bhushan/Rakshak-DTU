package eu.ekansh.rakshakdtu

class LogRepository {

    suspend fun fetchLogs(
        token: String,
        page: Int,
        authorized: Boolean?,
        logType: String?
    ) = logRakshakService.getLogs("Bearer $token", page, 20, authorized, logType)

    suspend fun fetchActiveLogs(token: String) =
        logRakshakService.getActiveLogs("Bearer $token")
}