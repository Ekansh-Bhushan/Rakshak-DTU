package eu.ekansh.rakshakdtu

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// ─────────────────────────────────────────────────────────────────────────────
//  REGISTER CAMERA FORM
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun RegisterCameraForm(
    token: String,
    viewModel: CameraViewModel = viewModel(),
    onClose: () -> Unit
) {
    val scrollState = rememberScrollState()
    var lat            by remember { mutableStateOf(0.00) }
    var long           by remember { mutableStateOf(0.00) }
    var cameraType     by remember { mutableStateOf("") }
    var cameraLocation by remember { mutableStateOf("") }

    val isFormValid = lat.isFinite() && long.isFinite() &&
            cameraType.isNotBlank() && cameraLocation.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .imePadding()
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        // ── Header ────────────────────────────────────────────────────────
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Register Camera",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color(0xFF1A1C1E)
                )
                Text(
                    "Add a new CCTV camera point",
                    fontSize = 13.sp,
                    color    = Color(0xFF6B7280)
                )
            }
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, null, tint = Color(0xFF9CA3AF))
            }
        }

        Spacer(Modifier.height(20.dp))
        HorizontalDivider(color = Color(0xFFF3F4F6))
        Spacer(Modifier.height(20.dp))

        // ── Coordinates row ───────────────────────────────────────────────
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                CoordinateFormField("LATITUDE", lat)  { lat = it }
            }
            Box(modifier = Modifier.weight(1f)) {
                CoordinateFormField("LONGITUDE", long) { long = it }
            }
        }

        // ── Camera type ───────────────────────────────────────────────────
        CameraFormField(
            label       = "CAMERA TYPE",
            placeholder = "e.g. Bullet, Dome, PTZ",
            value       = cameraType,
            leadingIcon = {
                Icon(Icons.Default.Videocam, null,
                    tint = Color(0xFF9CA3AF), modifier = Modifier.size(18.dp))
            }
        ) { cameraType = it }

        // ── Camera location ───────────────────────────────────────────────
        CameraFormField(
            label       = "CAMERA LOCATION",
            placeholder = "e.g. Main Gate — South Campus",
            value       = cameraLocation,
            leadingIcon = {
                Icon(Icons.Default.LocationOn, null,
                    tint = Color(0xFF9CA3AF), modifier = Modifier.size(18.dp))
            }
        ) { cameraLocation = it }

        Spacer(Modifier.height(8.dp))

        // ── Buttons ───────────────────────────────────────────────────────
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick  = onClose,
                shape    = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel", color = Color(0xFF374151))
            }

            Button(
                onClick = {
                    viewModel.addACameraData(
                        token          = token,
                        lat            = lat,
                        long           = long,
                        cameraType     = cameraType,
                        cameraLocation = cameraLocation
                    )
                    onClose()
                },
                enabled  = isFormValid,
                shape    = RoundedCornerShape(8.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Register", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  IMPORT CAMERA DIALOG
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun ImportCameraDialog(onClose: () -> Unit) {
    val context    = LocalContext.current
    var pickedUri  by remember { mutableStateOf<Uri?>(null) }
    var pickedName by remember { mutableStateOf<String?>(null) }

    val filePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            pickedUri  = it
            pickedName = context.contentResolver
                .query(it, null, null, null, null)
                ?.use { cursor ->
                    val idx = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    if (idx >= 0) cursor.getString(idx) else null
                }
            val inputStream = context.contentResolver.openInputStream(it)
            inputStream?.bufferedReader()?.use { reader -> Log.d("CSV_DATA", reader.readText()) }
        }
    }

    Column(
        modifier = Modifier
            .width(420.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(24.dp)
    ) {
        // ── Header ────────────────────────────────────────────────────────
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Import Cameras",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color(0xFF1A1C1E)
                )
                Text(
                    "Bulk add cameras from a CSV file",
                    fontSize = 13.sp,
                    color    = Color(0xFF6B7280)
                )
            }
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, null, tint = Color(0xFF9CA3AF))
            }
        }

        Spacer(Modifier.height(20.dp))
        HorizontalDivider(color = Color(0xFFF3F4F6))
        Spacer(Modifier.height(20.dp))

        // ── Info box ──────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFDCFCE7), RoundedCornerShape(8.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    "Required columns:",
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color(0xFF15803D)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "lat, long, cameraType, cameraLocation",
                    fontSize = 12.sp,
                    color    = Color(0xFF166534)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Download template →",
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color(0xFF16A34A)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Upload drop zone ──────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (pickedUri != null) Color(0xFFDCFCE7) else Color(0xFFF9FAFB)
                )
                .border(
                    width = 1.5.dp,
                    color = if (pickedUri != null) Color(0xFF16A34A) else Color(0xFFE5E7EB),
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable { filePicker.launch("text/csv") },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.Upload,
                    contentDescription = null,
                    tint     = if (pickedUri != null) Color(0xFF16A34A) else Color(0xFF9CA3AF),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.height(8.dp))
                if (pickedUri != null) {
                    Text(
                        pickedName ?: "File selected ✓",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color(0xFF15803D)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("Tap to change file", fontSize = 11.sp, color = Color(0xFF6B7280))
                } else {
                    Text(
                        "Tap to browse CSV file",
                        fontSize = 13.sp,
                        color    = Color(0xFF6B7280)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Only .csv files · Max 5 MB",
                        fontSize = 11.sp,
                        color    = Color(0xFF9CA3AF)
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Buttons ───────────────────────────────────────────────────────
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick  = onClose,
                shape    = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel", color = Color(0xFF374151))
            }

            Button(
                onClick  = { /* TODO: upload logic */ },
                enabled  = pickedUri != null,
                shape    = RoundedCornerShape(8.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Upload, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Upload & Import", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  SHARED FIELD COMPOSABLES
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun CoordinateFormField(
    label: String,
    value: Double,
    onValueChange: (Double) -> Unit
) {
    var textValue by remember(value) { mutableStateOf(if (value == 0.0) "" else value.toString()) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            label,
            fontSize      = 11.sp,
            fontWeight    = FontWeight.Bold,
            color         = Color(0xFF6B7280),
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value         = textValue,
            onValueChange = { new ->
                textValue = new
                new.toDoubleOrNull()?.let { onValueChange(it) }
            },
            placeholder = { Text("0.000000", color = Color(0xFFADB5BD)) },
            singleLine  = true,
            shape       = RoundedCornerShape(8.dp),
            colors      = OutlinedTextFieldDefaults.colors(
                focusedContainerColor   = Color(0xFFF9FAFB),
                unfocusedContainerColor = Color(0xFFF9FAFB),
                focusedBorderColor      = Color(0xFF16A34A),
                unfocusedBorderColor    = Color(0xFFE5E7EB),
                focusedTextColor        = Color(0xFF1A1C1E),
                unfocusedTextColor      = Color(0xFF1A1C1E),
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(14.dp))
    }
}

@Composable
private fun CameraFormField(
    label: String,
    placeholder: String = "",
    value: String,
    leadingIcon: (@Composable () -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            label,
            fontSize      = 11.sp,
            fontWeight    = FontWeight.Bold,
            color         = Color(0xFF6B7280),
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            placeholder   = { Text(placeholder, color = Color(0xFFADB5BD), fontSize = 13.sp) },
            leadingIcon   = leadingIcon,
            singleLine    = true,
            shape         = RoundedCornerShape(8.dp),
            colors        = OutlinedTextFieldDefaults.colors(
                focusedContainerColor   = Color(0xFFF9FAFB),
                unfocusedContainerColor = Color(0xFFF9FAFB),
                focusedBorderColor      = Color(0xFF16A34A),
                unfocusedBorderColor    = Color(0xFFE5E7EB),
                focusedTextColor        = Color(0xFF1A1C1E),
                unfocusedTextColor      = Color(0xFF1A1C1E),
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(14.dp))
    }
}