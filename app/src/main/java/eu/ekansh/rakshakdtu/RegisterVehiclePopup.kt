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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
fun RegisterVehicleForm(token : String,viewModel: VehicleViewModel = viewModel(), onClose: () -> Unit) {

    val scrollState = rememberScrollState()
    var name by remember { mutableStateOf("") }
    var fathersName by remember { mutableStateOf("") }
    var vehicleNo by remember { mutableStateOf("") }
    var stickerNo by remember { mutableStateOf("") }
    var dept by remember { mutableStateOf("") }
    var vehicleType by remember { mutableStateOf("2W") }
    var mobileNo by remember { mutableStateOf("") }
    var dateOfIssue by remember { mutableStateOf("") }

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

        FormField("Owner Name",name) {name=it}
        FormField("Father's Name",fathersName) {fathersName=it}

        FormField(
            label = "Vehicle No.",
            placeholder = "DL3CAF0001",
            value = vehicleNo
        ) {vehicleNo = it}

        FormField("Sticker No.", value = stickerNo) {stickerNo=it}

        FormField(
            label = "Department",
            placeholder = "e.g. ECE, CSE",
            value = dept
        ) {dept = it}

        // Custom Dropdown with callback
        VehicleTypeDropdown(vehicleType) { vehicleType = it }

        // Custom Mobile with callback
        MobileInput(mobileNo) { mobileNo = it }

        // Custom Date with callback
        DateField(dateOfIssue) { dateOfIssue = it }

        Spacer(modifier = Modifier.height(20.dp))

        /* ACTION BUTTONS */

        val isFormValid = name.isNotBlank() &&
                fathersName.isNotBlank() &&
                stickerNo.isNotBlank() &&
                vehicleNo.isNotBlank() &&
                mobileNo.length == 10 &&
                dateOfIssue.isNotBlank()

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
                viewModel.addAVehicleData(
                    token = token,
                    name = name,
                    fathersName = fathersName,
                    dept = dept,
                    dateOfIssue = dateOfIssue,
                    vehicleType = vehicleType,
                    stickerNo = stickerNo,
                    vehicleNo = vehicleNo,
                    mobileNo = mobileNo
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
fun FormField(
    label: String,
    value: String,
    placeholder: String = "",
    onValueChange: (String) -> Unit
) {

    Column {

        Text(
            label,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Composable
fun VehicleTypeDropdown(selectedType: String, onTypeChange: (String) -> Unit) {

    var expanded by remember { mutableStateOf(false) }

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
                value = selectedType,
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
                            onTypeChange(it)
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
fun MobileInput(mobileNo: String, onMobileChange: (String) -> Unit) {

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
                value = mobileNo,
                onValueChange = onMobileChange,
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
fun DateField(dateOfIssue: String, onDateChange: (String) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }

    // 1. Create a state that restricts selection to the past
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Return true only if the date is BEFORE today
                return utcTimeMillis < System.currentTimeMillis()
            }
        }
    )

    Column {
        Text("Date of Issue", fontWeight = FontWeight.Medium, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = dateOfIssue,
            onValueChange = { /* Read only, changed via picker */ },
            readOnly = true,
            placeholder = { Text("YYYY-MM-DD") },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

        // 2. The Actual DatePickerDialog
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                            val dateString = formatter.format(java.util.Date(millis))
                            onDateChange(dateString)
                        }
                        showDatePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
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
