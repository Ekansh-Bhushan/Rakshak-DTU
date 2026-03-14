package eu.ekansh.rakshakdtu

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


val vehicleRakshakService: VehicleAPIServices = AllAPIClient.vehicleApiService
interface VehicleAPIServices {

    @POST("vehicles")
    suspend fun addVehicle(
        @Body request: VehicleAddRequest
    ) : Response<VehicleAddResponse>

    @GET("vehicles")
    suspend fun getAllVehicles(
        @Header("Authorization") accessToken : String,
    ) : Response<AllVehicleGetData>
}