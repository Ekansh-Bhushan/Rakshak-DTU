package eu.ekansh.rakshakdtu

import retrofit2.Response

class vehicleRepository {

    suspend fun getAllVehicles(token: String) : Response<AllVehicleGetData>{
        return vehicleRakshakService.getAllVehicles(accessToken = "Bearer $token")
    }
}