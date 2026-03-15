package eu.ekansh.rakshakdtu

import java.util.Date

data class VehicleAddRequest(
    val name : String,
    val fathersName : String,
    val dept : String,
    val dateOfIssue : String,
    val vehicleType : String,
    val stickerNo : String,
    val vehicleNo : String,
    val mobileNo : String
)

data class VehicleAddResponse(
    val statusCode: Int,
    val data: VehicleData,
    val message: String,
    val success : Boolean
)

data class VehicleData(
    val id : String,
    val name : String,
    val fathersName : String,
    val dept : String,
    val dateOfIssue : String,
    val vehicleType : String,
    val stickerNo : String,
    val vehicleNo : String,
    val mobileNo : String,
    val createdAt : String,
    val updatedAt : String,
)

data class AllVehicleGetData(
    val statusCode : Int,
    val data :ListOfGetVehicles,
    val message : String,
    val success : Boolean
)

data class ListOfGetVehicles(
    val vehicles : List<VehicleData>,
    val total : Int,
    val page : Int,
    val limit : Int
)

data class DeleteVehicleResponse(
    val statusCode : Int,
    val data : Any?,
    val message : String,
    val success : Boolean
)

data class VehicleUpdateRequest(
    val name: String? = null,
    val fathersName: String? = null,
    val dept: String? = null,
    val vehicleType: String? = null,
    val stickerNo: String? = null,
    val mobileNo: String? = null,
    val dateOfIssue: String? = null
)

data class SearchVehicleResponse(
    val statusCode : Int,
    val data : VehicleData,
    val message : String,
    val success : Boolean
)