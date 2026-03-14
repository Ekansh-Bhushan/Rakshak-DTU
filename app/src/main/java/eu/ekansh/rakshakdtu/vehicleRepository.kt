package eu.ekansh.rakshakdtu

import retrofit2.Response

class vehicleRepository {

    suspend fun getAllVehicles(token: String) : Response<AllVehicleGetData>{
        return vehicleRakshakService.getAllVehicles(accessToken = "Bearer $token")
    }

    suspend fun addAVehicle(
        token : String,
        name : String,
        fathersName : String,
        dept : String,
        dateOfIssue : String,
        vehicleType : String,
        stickerNo : String,
        vehicleNo : String,
        mobileNo : String
    ) : Response<VehicleAddResponse> {
        val request = VehicleAddRequest(name, fathersName,dept,dateOfIssue, vehicleType, stickerNo, vehicleNo, mobileNo)
        return vehicleRakshakService.addVehicle(accessToken = "Bearer $token",request=request)
    }
}