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
fun EditVehicleForm(
    token: String,
    vehicle: VehicleData, // Pass the existing data
    viewModel: VehicleViewModel,
    onClose: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Pre-fill states with current vehicle data
    var name by remember { mutableStateOf(vehicle.name) }
    var fathersName by remember { mutableStateOf(vehicle.fathersName) }
    var dept by remember { mutableStateOf(vehicle.dept) }
    var stickerNo by remember { mutableStateOf(vehicle.stickerNo) }
    var mobileNo by remember { mutableStateOf(vehicle.mobileNo) }

    Column(modifier = Modifier.fillMaxWidth().padding(20.dp).verticalScroll(scrollState)) {
        Text("Edit Vehicle Details", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Greyed out / Read-only field for Vehicle Number
        Text("Vehicle Number (Cannot be changed)", fontSize = 12.sp, color = Color.Gray)
        OutlinedTextField(
            value = vehicle.vehicleNo,
            onValueChange = {},
            enabled = false, // This greys out the field
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                disabledContainerColor = Color(0xFFF0F0F0),
                disabledBorderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Editable fields
        FormField("Owner Name", name) { name = it }
        FormField("Father's Name", fathersName) { fathersName = it }
        FormField("Department", dept) { dept = it }
        FormField("Sticker No.", stickerNo) { stickerNo = it }
        FormField("Mobile No.", mobileNo) { mobileNo = it }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = onClose, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    val request = VehicleUpdateRequest(
                        name = name,
                        fathersName = fathersName,
                        dept = dept,
                        stickerNo = stickerNo,
                        mobileNo = mobileNo
                    )
                    viewModel.editVehicle(token, vehicle.vehicleNo, request)
                    onClose()
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.lightGreen))
            ) {
                Text("Save Changes")
            }
        }
    }
}