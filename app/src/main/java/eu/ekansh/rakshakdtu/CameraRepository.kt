package eu.ekansh.rakshakdtu

import retrofit2.Response

class cameraRepository {

    suspend fun getAllCamera(token: String,) : Response<AllCameraGetData>{
        return cameraRakshakService.getAllCamera(accessToken = "Bearer $token")
    }

    suspend fun addACamera(
        token : String,
        lat : Double,
        long : Double,
        cameraType : String,
        cameraLocation : String
    ) : Response<CameraAddResponse> {
        val request = CameraAddRequest(lat, long, cameraType, cameraLocation)
        return cameraRakshakService.addCamera(accessToken = "Bearer $token",request=request)
    }

    suspend fun deleteAVehicle(
        token : String,
        id : String
    ) : Response<DeleteCameraResponse> {
        return cameraRakshakService.deleteACamera(accessToken = "Bearer $token",id = id)
    }

    suspend fun updateCamera(
        token: String,
        vehicleNo: String,
        request: CameraUpdateRequest
    ): Response<Unit> {
        return cameraRakshakService.editACamera("Bearer $token", vehicleNo, request)
    }

}