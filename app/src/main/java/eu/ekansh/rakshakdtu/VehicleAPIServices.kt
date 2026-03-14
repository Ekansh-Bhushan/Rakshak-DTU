package eu.ekansh.rakshakdtu

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


val vehicleRakshakService: VehicleAPIServices = AllAPIClient.vehicleApiService
interface VehicleAPIServices {

    @POST("vehicles")
    suspend fun addVehicle(
        @Header("Authorization") accessToken : String,
        @Body request: VehicleAddRequest
    ) : Response<VehicleAddResponse>

    @GET("vehicles")
    suspend fun getAllVehicles(
        @Header("Authorization") accessToken : String,
    ) : Response<AllVehicleGetData>

    @DELETE("vehicles/{vehicleNo}")
    suspend fun deleteAVehicle(
        @Header("Authorization") accessToken : String,
        @Path("vehicleNo") vehicleNo: String
    ) : Response<DeleteVehicleResponse>

    @PUT("vehicles/{vehicleNo}")
    suspend fun editAVehicle(
        @Header("Authorization") accessToken : String,
        @Path("vehicleNo") vehicleNo: String,
        @Body request: VehicleUpdateRequest
    ) : Response<Unit>
}