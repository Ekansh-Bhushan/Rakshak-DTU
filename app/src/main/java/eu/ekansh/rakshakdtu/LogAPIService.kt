package eu.ekansh.rakshakdtu

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface LogAPIServices {

    @GET("scan/logs")
    suspend fun getLogs(
        @Header("Authorization") token: String,
        @Query("page")       page: Int,
        @Query("limit")      limit: Int,
        @Query("authorized") authorized: Boolean? = null,
        @Query("logType")    logType: String? = null,
        @Query("cameraId")   cameraId: String? = null
    ): Response<LogResponse>

    @GET("scan/logs/active")
    suspend fun getActiveLogs(
        @Header("Authorization") token: String
    ): Response<ActiveLogResponse>
}