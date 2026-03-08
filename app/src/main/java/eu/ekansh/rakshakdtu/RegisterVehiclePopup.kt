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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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

@Composable
fun RegisterVehicleForm(onClose: () -> Unit) {

    val scrollState = rememberScrollState()

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

        FormField("Owner Name")
        FormField("Father's Name")

        FormField(
            label = "Vehicle No.",
            placeholder = "DL3CAF0001"
        )

        FormField("Sticker No.")

        FormField(
            label = "Department",
            placeholder = "e.g. ECE, CSE"
        )

        VehicleTypeDropdown()

        MobileInput()

        DateField()

        Spacer(modifier = Modifier.height(20.dp))

        /* ACTION BUTTONS */

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
            onClick = { /*TODO*/},
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
fun FormField(
    label: String,
    placeholder: String = ""
) {

    var text by remember { mutableStateOf("") }

    Column {

        Text(
            label,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Composable
fun VehicleTypeDropdown() {

    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf("2W") }

    Column {

        Text(
            "Vehicle Type",
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            OutlinedTextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                listOf("2W", "4W").forEach {

                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            selected = it
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Composable
fun MobileInput() {

    var number by remember { mutableStateOf("") }

    Column {

        Text(
            "Mobile No.",
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Box(
                modifier = Modifier
                    .background(Color(0xFFF3F4F6), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 16.dp)
            ) {
                Text("🇮🇳 +91")
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = number,
                onValueChange = { number = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("9876543269") },
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Composable
fun DateField() {

    var date by remember { mutableStateOf("") }

    Column {

        Text(
            "Date of Issue",
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            placeholder = { Text("dd/mm/yyyy") },
            trailingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )
    }
}



@Composable
fun ImportVehiclesDialog(
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
                "Import Vehicles from CSV",
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
                    "Required columns: name, fathersName, dept,\n" +
                            "dateOfIssue, vehicleType, stickerNo, vehicleNo, mobileNo",
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
