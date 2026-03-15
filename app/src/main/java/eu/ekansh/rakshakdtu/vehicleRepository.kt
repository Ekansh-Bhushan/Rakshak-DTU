package eu.ekansh.rakshakdtu

import retrofit2.Response

class vehicleRepository {

    suspend fun getAllVehicles(token: String,search: String? = null) : Response<AllVehicleGetData>{
        return vehicleRakshakService.getAllVehicles(accessToken = "Bearer $token",query = search)
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

    suspend fun deleteAVehicle(
        token : String,
        numberPlate : String
    ) : Response<DeleteVehicleResponse> {
        return vehicleRakshakService.deleteAVehicle(accessToken = "Bearer $token",vehicleNo = numberPlate)
    }

    suspend fun updateVehicle(
        token: String,
        vehicleNo: String,
        request: VehicleUpdateRequest
    ): Response<Unit> {
        return vehicleRakshakService.editAVehicle("Bearer $token", vehicleNo, request)
    }

}