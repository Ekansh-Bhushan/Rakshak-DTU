package eu.ekansh.rakshakdtu

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegisterCameraForm(token : String,viewModel: CameraViewModel = viewModel(), onClose: () -> Unit) {

    val scrollState = rememberScrollState()
    var lat by remember { mutableStateOf(0.00) }
    var long by remember { mutableStateOf(0.00) }
    var cameraType by remember { mutableStateOf("") }
    var cameraLocation by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {

        /* HEADER */

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                "Register Vehicle",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.clickable { onClose() }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        /* FORM */

        CordinateFormField("Latitude",lat) {lat=it}
        CordinateFormField("Longitude",long) {long=it}

        FormField(
            label = "Camera Type",
            placeholder = "Bullet",
            value = cameraType
        ) {cameraType = it}

        FormField("Camera Location", value = cameraLocation) {cameraLocation=it}

        Spacer(modifier = Modifier.height(20.dp))

        /* ACTION BUTTONS */

        val isFormValid = lat.isFinite() &&
                long.isFinite() &&
                cameraType.isNotBlank() &&
                cameraLocation.isNotBlank()

        Button(
            onClick = {onClose()},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE5E5E5)
            )
        ) {
            Text("Cancel", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                viewModel.addACameraData(
                    token = token,
                    lat = lat,
                    long = long,
                    cameraType = cameraType,
                    cameraLocation = cameraLocation
                )
                onClose()
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2EA65A)
            )
        ) {
            Text("Register")
        }
    }
}

@Composable
fun ImportCameraDialog(
    token : String,
    onClose: () -> Unit
) {
    val context = LocalContext.current

    val filePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->

            uri?.let {

                val inputStream = context.contentResolver.openInputStream(it)

                inputStream?.bufferedReader()?.use { reader ->
                    val csvText = reader.readText()

                    Log.d("CSV_DATA", csvText)
                }
            }
        }

    Column(
        modifier = Modifier
            .width(420.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {

        /* HEADER */

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                "Import Cameras from CSV",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.clickable { onClose() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        /* INFO BOX */

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFFEFF8F1),
                    RoundedCornerShape(10.dp)
                )
                .padding(14.dp)
        ) {

            Column {

                Text(
                    "Required columns: lat, long, cameraType, cameraLocation",
                    fontSize = 13.sp,
                    color = Color(0xFF2F6E45)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    "Download template",
                    color = Color(0xFF2EA65A),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        /* UPLOAD AREA */

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(16.dp)
                .clickable {
                    filePicker.launch("text/csv")
                },
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.Upload,
                    contentDescription = null,
                    tint = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Drag & drop your CSV here, or browse",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    "Only .csv files · Max 5 MB",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        /* BUTTONS */

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE5E5E5)
            )
        ) {
            Text("Cancel", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2EA65A)
            )
        ) {
            Icon(Icons.Default.Upload, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Upload & Import")
        }
    }
}

@Composable
fun CordinateFormField(
    label: String,
    value: Double,
    onValueChange: (Double) -> Unit
) {
    // Local state to manage the text input as the user types
    var textValue by remember(value) { mutableStateOf(value.toString()) }

    Column {
        Text(
            label,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            // OutlinedTextField MUST take a String
            value = textValue,
            onValueChange = { newValue ->
                textValue = newValue
                // Only update the actual double value if the input is a valid number
                newValue.toDoubleOrNull()?.let { onValueChange(it) }
            },
            placeholder = { Text("0.00") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(14.dp))
    }
}



