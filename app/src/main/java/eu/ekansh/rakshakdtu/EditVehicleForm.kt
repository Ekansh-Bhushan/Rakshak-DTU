package eu.ekansh.rakshakdtu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditVehicleForm(
    token: String,
    vehicle: VehicleData,
    viewModel: VehicleViewModel,
    onClose: () -> Unit
) {
    val scrollState = rememberScrollState()

    var name        by remember { mutableStateOf(vehicle.name) }
    var fathersName by remember { mutableStateOf(vehicle.fathersName) }
    var dept        by remember { mutableStateOf(vehicle.dept) }
    var stickerNo   by remember { mutableStateOf(vehicle.stickerNo) }
    var mobileNo    by remember { mutableStateOf(vehicle.mobileNo) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(24.dp)
            .verticalScroll(scrollState)
    ) {

        // ── Header ────────────────────────────────────────────────────────
        Text(
            "Edit Vehicle",
            fontSize   = 20.sp,
            fontWeight = FontWeight.Bold,
            color      = Color(0xFF1A1C1E)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Update the details for ${vehicle.vehicleNo}",
            fontSize = 13.sp,
            color    = Color(0xFF6B7280)
        )

        Spacer(Modifier.height(20.dp))

        // ── Read-only plate ───────────────────────────────────────────────
        Text(
            "VEHICLE NUMBER",
            fontSize      = 11.sp,
            fontWeight    = FontWeight.Bold,
            color         = Color(0xFF6B7280),
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(6.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                tint     = Color(0xFF9CA3AF),
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                vehicle.vehicleNo,
                fontWeight = FontWeight.Bold,
                fontSize   = 15.sp,
                color      = Color(0xFF6B7280)
            )
            Spacer(Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .background(Color(0xFFE5E7EB), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text("Read only", fontSize = 10.sp, color = Color(0xFF9CA3AF))
            }
        }

        Spacer(Modifier.height(20.dp))

        HorizontalDivider(color = Color(0xFFF3F4F6))

        Spacer(Modifier.height(20.dp))

        // ── Editable fields ───────────────────────────────────────────────
        EditFormField("Owner Name",   name)        { name = it }
        EditFormField("Father's Name", fathersName) { fathersName = it }
        EditFormField("Department",   dept)         { dept = it }
        EditFormField("Sticker No.",  stickerNo)    { stickerNo = it }
        EditFormField("Mobile No.",   mobileNo)     { mobileNo = it }

        Spacer(Modifier.height(8.dp))

        // ── Action buttons ────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)
        ) {
            OutlinedButton(
                onClick = onClose,
                shape   = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel", color = Color(0xFF374151))
            }

            Button(
                onClick = {
                    viewModel.editVehicle(
                        token, vehicle.vehicleNo,
                        VehicleUpdateRequest(
                            name        = name,
                            fathersName = fathersName,
                            dept        = dept,
                            stickerNo   = stickerNo,
                            mobileNo    = mobileNo
                        )
                    )
                    onClose()
                },
                shape  = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.lightGreen)
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Save Changes", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// ── Shared field composable ───────────────────────────────────────────────────

@Composable
private fun EditFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
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