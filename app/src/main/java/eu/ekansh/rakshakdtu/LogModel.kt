package eu.ekansh.rakshakdtu

// ── All-logs response ─────────────────────────────────────────────────────────

data class LogResponse(
    val statusCode: Int,
    val data: LogData,
    val message: String,
    val success: Boolean
)

data class LogData(
    val logs: List<Logs>,
    val total: Int,
    val page: Int,
    val limit: Int
)

// ── Active-logs response ──────────────────────────────────────────────────────

data class ActiveLogResponse(
    val statusCode: Int,
    val data: ActiveLogData,
    val message: String,
    val success: Boolean
)

data class ActiveLogData(
    val count: Int,
    val logs: List<Logs>
)

// ── Shared log model ──────────────────────────────────────────────────────────

data class Logs(
    val id: String,
    val vehicleNo: String,
    val logType: String,
    val isAuthorized: Boolean,
    val vehicleCategory: String,
    val entryTime: String,
    val exitTime: String? = null,
    val vehicleDuration: String? = null,
    val confidence: Double? = null,
    val rawPlate: String,
    val camera: CameraDetails,
    val vehicle: VehicleDetails,

    // Active/unauth enrichment (from /scan/logs/active)
    val allowedUntil: String? = null,
    val remainingSeconds: Int? = null,
    val isOverdue: Boolean? = null
)

data class CameraDetails(
    val id: String,
    val cameraLocation: String,
    val cameraType: String
)

data class VehicleDetails(
    val name: String,
    val dept: String,
    val vehicleType: String
)