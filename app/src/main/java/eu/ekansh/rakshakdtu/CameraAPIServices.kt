package eu.ekansh.rakshakdtu

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


val cameraRakshakService: CameraAPIServices = AllAPIClient.cameraApiService

interface CameraAPIServices{

    @POST("cameras")
    suspend fun addCamera(
        @Header("Authorization") accessToken : String,
        @Body request: CameraAddRequest
    ) : Response<CameraAddResponse>

    @GET("cameras")
    suspend fun getAllCamera(
        @Header("Authorization") accessToken : String,
    ) : Response<AllCameraGetData>


    @DELETE("vehicles/{id}")
    suspend fun deleteACamera(
        @Header("Authorization") accessToken : String,
        @Path("id") id: String
    ) : Response<DeleteCameraResponse>

    @PUT("vehicles/{id}")
    suspend fun editACamera(
        @Header("Authorization") accessToken : String,
        @Path("id") id: String,
        @Body request: CameraUpdateRequest
    ) : Response<Unit>

}