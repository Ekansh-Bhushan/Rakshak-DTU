package eu.ekansh.rakshakdtu

data class ScanActivity(
    val vehicleNo: String,
    val camera: String,
    val entryTime: String,
    val duration: String?,
    val authorized: Boolean
)