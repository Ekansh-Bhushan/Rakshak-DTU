package eu.ekansh.rakshakdtu

data class CameraAddRequest(
    val lat : Double,
    val long : Double,
    val cameraType : String,
    val cameraLocation : String
)

data class CameraAddResponse(
    val statusCode : Int,
    val data : CameraData,
    val message : String,
    val success : Boolean
)

data class CameraData(
    val id : String,
    val lat : Double,
    val long : Double,
    val cameraType : String,
    val cameraLocation : String,
    val createdAt : String
)

data class AllCameraGetData(
    val statusCode : Int,
    val data : List<CameraData>,
    val message : String,
    val success : Boolean
)

data class DeleteCameraResponse(
    val statusCode : Int,
    val data : Any?,
    val message : String,
    val success : Boolean
)

data class CameraUpdateRequest(
    val lat: Double? = null,
    val long: Double? = null,
    val cameraType: String? = null,
    val cameraLocation: String? = null,
)