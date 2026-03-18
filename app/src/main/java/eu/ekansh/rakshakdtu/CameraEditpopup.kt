package eu.ekansh.rakshakdtu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditCameraForm(
    token: String,
    camera: CameraData,
    viewModel: CameraViewModel,
    onClose: () -> Unit
) {
    val scrollState    = rememberScrollState()
    var cameraType     by remember { mutableStateOf(camera.cameraType) }
    var cameraLocation by remember { mutableStateOf(camera.cameraLocation) }

    val isFormValid = cameraType.isNotBlank() && cameraLocation.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(24.dp)
            .verticalScroll(scrollState)
    ) {
        // ── Header ────────────────────────────────────────────────────────
        Text(
            "Edit Camera",
            fontSize   = 20.sp,
            fontWeight = FontWeight.Bold,
            color      = Color(0xFF1A1C1E)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Update details for camera ${camera.id.takeLast(8)}",
            fontSize = 13.sp,
            color    = Color(0xFF6B7280)
        )

        Spacer(Modifier.height(20.dp))
        HorizontalDivider(color = Color(0xFFF3F4F6))
        Spacer(Modifier.height(20.dp))

        // ── Read-only coordinates ─────────────────────────────────────────
        Text(
            "COORDINATES",
            fontSize      = 11.sp,
            fontWeight    = FontWeight.Bold,
            color         = Color(0xFF6B7280),
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(8.dp))

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CoordReadOnly(label = "Latitude",  value = camera.lat.toString(),  modifier = Modifier.weight(1f))
            CoordReadOnly(label = "Longitude", value = camera.long.toString(), modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(20.dp))
        HorizontalDivider(color = Color(0xFFF3F4F6))
        Spacer(Modifier.height(20.dp))

        // ── Editable fields ───────────────────────────────────────────────
        CameraEditField(
            label       = "CAMERA TYPE",
            placeholder = "e.g. Bullet, Dome, PTZ",
            value       = cameraType,
            leadingIcon = Icons.Default.Videocam
        ) { cameraType = it }

        CameraEditField(
            label       = "CAMERA LOCATION",
            placeholder = "e.g. Main Gate — South Campus",
            value       = cameraLocation,
            leadingIcon = Icons.Default.LocationOn
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
                    viewModel.editCamera(
                        token, camera.id,
                        CameraUpdateRequest(
                            cameraLocation = cameraLocation,
                            cameraType     = cameraType
                        )
                    )
                    onClose()
                },
                enabled  = isFormValid,
                shape    = RoundedCornerShape(8.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Save Changes", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  READ-ONLY COORDINATE PILL
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CoordReadOnly(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            label.uppercase(),
            fontSize      = 10.sp,
            fontWeight    = FontWeight.Bold,
            color         = Color(0xFF9CA3AF),
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                tint     = Color(0xFF9CA3AF),
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(value, fontSize = 13.sp, color = Color(0xFF6B7280), fontWeight = FontWeight.Medium)
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  EDITABLE FIELD
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CameraEditField(
    label: String,
    placeholder: String = "",
    value: String,
    leadingIcon: ImageVector? = null,
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
            leadingIcon   = leadingIcon?.let { icon ->
                { Icon(icon, null, tint = Color(0xFF9CA3AF), modifier = Modifier.size(18.dp)) }
            },
            singleLine = true,
            shape      = RoundedCornerShape(8.dp),
            colors     = OutlinedTextFieldDefaults.colors(
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


// ─────────────────────────────────────────────────────────────────────────────
//  SHARED COMPOSABLES (used by RegisterVehicleForm etc.)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun FormField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            label.uppercase(),
            fontSize      = 11.sp,
            fontWeight    = FontWeight.Bold,
            color         = Color(0xFF6B7280),
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
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

@Composable
fun ReadOnlyField(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            label.uppercase(),
            fontSize      = 10.sp,
            fontWeight    = FontWeight.Bold,
            color         = Color(0xFF9CA3AF),
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Icon(Icons.Default.Lock, null, tint = Color(0xFF9CA3AF), modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(6.dp))
            Text(value, fontSize = 13.sp, color = Color(0xFF6B7280), fontWeight = FontWeight.Medium)
        }
    }
}