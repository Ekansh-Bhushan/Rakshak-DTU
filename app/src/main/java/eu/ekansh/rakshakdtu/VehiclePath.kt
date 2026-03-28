package eu.ekansh.rakshakdtu

data class PathPoint(
    val lat: Double,
    val lng: Double,
    val timestamp: String,
    val cameraId: String?,
    val cameraLocation: String?,
    val type: String  // "ENTRY" | "SIGHTING" | "EXIT"
)

data class EntryPathData(
    val entryId: String,
    val path: List<PathPoint>
)

data class EntryPathResponse(
    val statusCode: Int,
    val data: EntryPathData,
    val message: String,
    val success: Boolean
)