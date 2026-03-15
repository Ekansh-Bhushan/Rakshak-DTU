package eu.ekansh.rakshakdtu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditCameraForm(
    token: String,
    camera: CameraData, // Assuming camera.lat and camera.long are Double
    viewModel: CameraViewModel,
    onClose: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Pre-fill states
    var cameraType by remember { mutableStateOf(camera.cameraType) }
    var cameraLocation by remember { mutableStateOf(camera.cameraLocation) }

    Column(modifier = Modifier.fillMaxWidth().padding(20.dp).verticalScroll(scrollState)) {
        Text("Edit Camera Details", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Camera Coordinates (Read-only)", fontSize = 12.sp, color = Color.Gray)

        // Handling Doubles by converting to String
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ReadOnlyField(
                label = "Latitude",
                value = camera.lat.toString(),
                modifier = Modifier.weight(1f)
            )
            ReadOnlyField(
                label = "Longitude",
                value = camera.long.toString(),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        FormField("Camera Type", cameraType) { cameraType = it }
        Spacer(modifier = Modifier.height(8.dp))
        FormField("Camera Location", cameraLocation) { cameraLocation = it }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = onClose, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                enabled = cameraType.isNotBlank() && cameraLocation.isNotBlank(),
                onClick = {
                    val request = CameraUpdateRequest(
                        cameraLocation = cameraLocation,
                        cameraType = cameraType
                    )
                    viewModel.editCamera(token, camera.id, request)
                    onClose()
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.lightGreen))
            ) {
                Text("Save Changes")
            }
        }
    }
}

@Composable
fun FormField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ReadOnlyField(label: String, value: String, modifier: Modifier = Modifier) { // Change value type to String
    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        enabled = false,
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
            disabledContainerColor = Color(0xFFF5F5F5),
            disabledBorderColor = Color.LightGray,
            disabledTextColor = Color.DarkGray
        )
    )
}