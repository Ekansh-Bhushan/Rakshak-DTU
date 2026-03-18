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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// ─────────────────────────────────────────────────────────────────────────────
//  REGISTER VEHICLE FORM
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun RegisterVehicleForm(
    token: String,
    viewModel: VehicleViewModel = viewModel(),
    onClose: () -> Unit
) {
    val scrollState = rememberScrollState()
    var name        by remember { mutableStateOf("") }
    var fathersName by remember { mutableStateOf("") }
    var vehicleNo   by remember { mutableStateOf("") }
    var stickerNo   by remember { mutableStateOf("") }
    var dept        by remember { mutableStateOf("") }
    var vehicleType by remember { mutableStateOf("2W") }
    var mobileNo    by remember { mutableStateOf("") }
    var dateOfIssue by remember { mutableStateOf("") }

    val isFormValid = name.isNotBlank() && fathersName.isNotBlank() &&
            stickerNo.isNotBlank() && vehicleNo.isNotBlank() &&
            mobileNo.length == 10 && dateOfIssue.isNotBlank()

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
                Text("Register Vehicle", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E))
                Text("Add a new authorized vehicle", fontSize = 13.sp, color = Color(0xFF6B7280))
            }
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, null, tint = Color(0xFF9CA3AF))
            }
        }

        Spacer(Modifier.height(20.dp))
        HorizontalDivider(color = Color(0xFFF3F4F6))
        Spacer(Modifier.height(20.dp))

        // ── Section: Owner details ────────────────────────────────────────
        SectionLabel("OWNER DETAILS")
        Spacer(Modifier.height(12.dp))

        VehicleFormField("OWNER NAME", "e.g. Ekansh Sharma", name, Icons.Default.Person) { name = it }
        VehicleFormField("FATHER'S NAME", "e.g. Rakesh Sharma", fathersName, Icons.Default.Person) { fathersName = it }
        VehicleFormField("DEPARTMENT", "e.g. ECE, CSE, MBA", dept, Icons.Default.Badge) { dept = it }
        VehicleFormField("MOBILE NO.", "9876543210", mobileNo, Icons.Default.Phone,
            keyboardType = KeyboardType.Number,
            maxLength    = 10,
            prefix       = {
                Text("🇮🇳 +91  ", fontSize = 13.sp, color = Color(0xFF374151), fontWeight = FontWeight.Medium)
            }
        ) { if (it.length <= 10) mobileNo = it }

        Spacer(Modifier.height(4.dp))
        HorizontalDivider(color = Color(0xFFF3F4F6))
        Spacer(Modifier.height(16.dp))

        // ── Section: Vehicle details ──────────────────────────────────────
        SectionLabel("VEHICLE DETAILS")
        Spacer(Modifier.height(12.dp))

        VehicleFormField("VEHICLE NUMBER", "DL3CAF0001", vehicleNo, Icons.Default.DirectionsCar) {
            vehicleNo = it.uppercase()
        }
        VehicleFormField("STICKER NO.", "e.g. STK-2024-001", stickerNo, Icons.Default.Badge) { stickerNo = it }

        // Vehicle type toggle
        Text("VEHICLE TYPE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280), letterSpacing = 0.5.sp)
        Spacer(Modifier.height(8.dp))
        VehicleTypeToggle(selected = vehicleType) { vehicleType = it }
        Spacer(Modifier.height(14.dp))

        // Date picker
        ImprovedDateField(dateOfIssue) { dateOfIssue = it }

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
                    viewModel.addAVehicleData(
                        token       = token, name = name, fathersName = fathersName,
                        dept        = dept,  dateOfIssue = dateOfIssue,
                        vehicleType = vehicleType, stickerNo = stickerNo,
                        vehicleNo   = vehicleNo, mobileNo = mobileNo
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
//  IMPORT VEHICLES DIALOG
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun ImportVehiclesDialog(onClose: () -> Unit) {
    val context    = LocalContext.current
    var pickedUri  by remember { mutableStateOf<Uri?>(null) }
    var pickedName by remember { mutableStateOf<String?>(null) }

    val filePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            pickedUri  = it
            pickedName = context.contentResolver
                .query(it, null, null, null, null)
                ?.use { cursor ->
                    val idx = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    if (idx >= 0) cursor.getString(idx) else null
                }
            context.contentResolver.openInputStream(it)
                ?.bufferedReader()
                ?.use { r -> Log.d("CSV_DATA", r.readText()) }
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
                Text("Import Vehicles", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E))
                Text("Bulk add vehicles from a CSV file", fontSize = 13.sp, color = Color(0xFF6B7280))
            }
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, null, tint = Color(0xFF9CA3AF))
            }
        }

        Spacer(Modifier.height(20.dp))
        HorizontalDivider(color = Color(0xFFF3F4F6))
        Spacer(Modifier.height(16.dp))

        // ── Info box ──────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFDCFCE7), RoundedCornerShape(8.dp))
                .padding(14.dp)
        ) {
            Text("Required columns:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF15803D))
            Spacer(Modifier.height(4.dp))
            Text(
                "name, fathersName, dept, dateOfIssue,\nvehicleType, stickerNo, vehicleNo, mobileNo",
                fontSize = 12.sp, color = Color(0xFF166534)
            )
            Spacer(Modifier.height(8.dp))
            Text("Download template →", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF16A34A))
        }

        Spacer(Modifier.height(16.dp))

        // ── Upload drop zone ──────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (pickedUri != null) Color(0xFFDCFCE7) else Color(0xFFF9FAFB))
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
                    Icons.Default.Upload, null,
                    tint     = if (pickedUri != null) Color(0xFF16A34A) else Color(0xFF9CA3AF),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.height(8.dp))
                if (pickedUri != null) {
                    Text(pickedName ?: "File selected ✓", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF15803D))
                    Spacer(Modifier.height(4.dp))
                    Text("Tap to change file", fontSize = 11.sp, color = Color(0xFF6B7280))
                } else {
                    Text("Tap to browse CSV file", fontSize = 13.sp, color = Color(0xFF6B7280))
                    Spacer(Modifier.height(4.dp))
                    Text("Only .csv files · Max 5 MB", fontSize = 11.sp, color = Color(0xFF9CA3AF))
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
//  SUB-COMPOSABLES
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Text(
        text,
        fontSize      = 11.sp,
        fontWeight    = FontWeight.Bold,
        color         = Color(0xFF16A34A),
        letterSpacing = 1.sp
    )
}

@Composable
private fun VehicleFormField(
    label: String,
    placeholder: String = "",
    value: String,
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLength: Int = Int.MAX_VALUE,
    prefix: (@Composable () -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280), letterSpacing = 0.5.sp)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value         = value,
            onValueChange = { if (it.length <= maxLength) onValueChange(it) },
            placeholder   = { Text(placeholder, color = Color(0xFFADB5BD), fontSize = 13.sp) },
            leadingIcon   = leadingIcon?.let { icon ->
                { Icon(icon, null, tint = Color(0xFF9CA3AF), modifier = Modifier.size(18.dp)) }
            },
            prefix        = prefix,
            singleLine    = true,
            shape         = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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

// 2W / 4W segmented toggle — cleaner than a dropdown for just two options
@Composable
private fun VehicleTypeToggle(selected: String, onSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {
        listOf("2W" to "🛵  Two Wheeler", "4W" to "🚗  Four Wheeler").forEach { (key, label) ->
            val isSelected = selected == key
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isSelected) Color.White else Color.Transparent)
                    .then(
                        if (isSelected) Modifier.border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(6.dp))
                        else Modifier
                    )
                    .clickable { onSelected(key) }
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    label,
                    fontSize   = 13.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color      = if (isSelected) Color(0xFF16A34A) else Color(0xFF6B7280)
                )
            }
        }
    }
}

@Composable
private fun ImprovedDateField(dateOfIssue: String, onDateChange: (String) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long) =
                utcTimeMillis < System.currentTimeMillis()
        }
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("DATE OF ISSUE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280), letterSpacing = 0.5.sp)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value         = dateOfIssue,
            onValueChange = {},
            readOnly      = true,
            placeholder   = { Text("Select a date", color = Color(0xFFADB5BD), fontSize = 13.sp) },
            leadingIcon   = {
                Icon(Icons.Default.CalendarMonth, null, tint = Color(0xFF9CA3AF), modifier = Modifier.size(18.dp))
            },
            trailingIcon  = {
                TextButton(onClick = { showDatePicker = true }) {
                    Text("Pick", fontSize = 12.sp, color = Color(0xFF16A34A), fontWeight = FontWeight.SemiBold)
                }
            },
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
            modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val fmt = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                            onDateChange(fmt.format(java.util.Date(millis)))
                        }
                        showDatePicker = false
                    }) { Text("OK", color = Color(0xFF16A34A)) }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                }
            ) { DatePicker(state = datePickerState) }
        }
    }
    Spacer(Modifier.height(14.dp))
}

// Legacy dropdown kept for backward compat
@Composable
fun VehicleTypeDropdown(selectedType: String, onTypeChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text("Vehicle Type", fontWeight = FontWeight.Medium, fontSize = 14.sp)
        Spacer(Modifier.height(6.dp))
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = selectedType, onValueChange = {}, readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(), shape = RoundedCornerShape(10.dp)
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                listOf("2W", "4W").forEach {
                    DropdownMenuItem(text = { Text(it) }, onClick = { onTypeChange(it); expanded = false })
                }
            }
        }
        Spacer(Modifier.height(14.dp))
    }
}

// Legacy mobile input kept for backward compat
@Composable
fun MobileInput(mobileNo: String, onMobileChange: (String) -> Unit) {
    Column {
        Text("Mobile No.", fontWeight = FontWeight.Medium, fontSize = 14.sp)
        Spacer(Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFF3F4F6), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 16.dp)
            ) { Text("🇮🇳 +91") }
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(
                value = mobileNo, onValueChange = onMobileChange,
                modifier = Modifier.weight(1f), placeholder = { Text("9876543210") },
                shape = RoundedCornerShape(10.dp), singleLine = true
            )
        }
        Spacer(Modifier.height(14.dp))
    }
}

// Legacy date field kept for backward compat
@Composable
fun DateField(dateOfIssue: String, onDateChange: (String) -> Unit) {
    ImprovedDateField(dateOfIssue, onDateChange)
}