package eu.ekansh.rakshakdtu

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import eu.ekansh.rakshakdtu.data.TokenManager

@Composable
fun VehicleScreen(
    viewModel: VehicleViewModel = viewModel(),
    navController: NavHostController
) {
    val vehicles     by viewModel.vehicleList
    val errorMessage by viewModel.errorMessage
    val toastMessage by viewModel.toastMessage

    val context      = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    var storedToken  by remember { mutableStateOf<String?>(null) }
    var searchQuery  by remember { mutableStateOf("") }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.toastMessage.value = null
        }
    }

    LaunchedEffect(Unit) {
        val token = tokenManager.getToken()
        if (token != null) {
            storedToken = token
            viewModel.getAllVehiclesDetails(token)
        } else {
            viewModel.errorMessage.value = "No session found. Please login."
            navController.navigate(Screen.LoginScreen.route)
        }
    }

    val totalVehicleCount              = vehicles?.size ?: 0
    var showRegisterForm               by remember { mutableStateOf(false) }
    var showImportExcelForm            by remember { mutableStateOf(false) }
    var vehicleToEdit: VehicleData?    by remember { mutableStateOf(null) }

    // ── Dialogs ───────────────────────────────────────────────────────────────
    vehicleToEdit?.let { vehicle ->
        androidx.compose.ui.window.Dialog(onDismissRequest = { vehicleToEdit = null }) {
            Card(shape = RoundedCornerShape(16.dp)) {
                EditVehicleForm(
                    token     = storedToken ?: "",
                    vehicle   = vehicle,
                    viewModel = viewModel,
                    onClose   = { vehicleToEdit = null }
                )
            }
        }
    }

    if (showRegisterForm) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showRegisterForm = false }) {
            Card(
                modifier = Modifier.width(420.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                RegisterVehicleForm(token = storedToken ?: "", onClose = { showRegisterForm = false })
            }
        }
    }

    if (showImportExcelForm) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showImportExcelForm = false }) {
            Card(
                modifier = Modifier.width(420.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                ImportVehiclesDialog(onClose = { showImportExcelForm = false })
            }
        }
    }

    // ── Screen body ───────────────────────────────────────────────────────────
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FA))
            .padding(16.dp)
    ) {
        // Header
        Text(
            "Registered Vehicles",
            fontWeight = FontWeight.Bold,
            fontSize   = 22.sp,
            color      = Color(0xFF1A1C1E)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Manage campus vehicle authorizations — $totalVehicleCount total registered",
            fontSize = 13.sp,
            color    = Color(0xFF6B7280)
        )

        Spacer(Modifier.height(16.dp))

        // Action buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { showImportExcelForm = true },
                colors  = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.lightGreen)),
                shape   = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Import Excel")
            }
            Button(
                onClick = { showRegisterForm = true },
                colors  = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.lightGreen)),
                shape   = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Add Vehicle")
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Search + table inside a Card (mirrors LogScreen style) ────────────
        Card(
            shape     = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            modifier  = Modifier.fillMaxWidth().weight(1f)
        ) {
            Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {

                // ── Polished search bar ───────────────────────────────────────
                OutlinedTextField(
                    value       = searchQuery,
                    onValueChange = { q ->
                        searchQuery = q
                        storedToken?.let { token ->
                            viewModel.onSearchQueryChanged(token, q)
                        }
                    },
                    placeholder = {
                        Text(
                            "Search by plate, name, dept…",
                            color    = Color(0xFFADB5BD),
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint     = Color(0xFFADB5BD),
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = searchQuery.isNotEmpty(),
                            enter   = fadeIn(),
                            exit    = fadeOut()
                        ) {
                            IconButton(
                                onClick  = {
                                    searchQuery = ""
                                    storedToken?.let { viewModel.onSearchQueryChanged(it, "") }
                                },
                                modifier = Modifier.size(18.dp)
                            ) {
                                Icon(Icons.Default.Close, null, tint = Color(0xFFADB5BD))
                            }
                        }
                    },
                    singleLine = true,
                    shape      = RoundedCornerShape(10.dp),
                    colors     = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor   = Color(0xFFF9FAFB),
                        unfocusedContainerColor = Color(0xFFF9FAFB),
                        focusedBorderColor      = Color(0xFF16A34A),
                        unfocusedBorderColor    = Color(0xFFE5E7EB),
                    ),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                )

                Spacer(Modifier.height(10.dp))

                // Record count pill
                Box(
                    modifier = Modifier
                        .background(Color(0xFFF3F4F6), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        "$totalVehicleCount Records",
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color      = Color(0xFF6B7280)
                    )
                }

                Spacer(Modifier.height(10.dp))

                // ── Table / states ────────────────────────────────────────────
                when {
                    errorMessage != null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Error: $errorMessage", color = Color.Red)
                        }
                    }
                    vehicles == null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF16A34A))
                        }
                    }
                    else -> {
                        VehicleTable(
                            vehicles    = vehicles!!,
                            token       = storedToken ?: "",
                            viewModel   = viewModel,
                            onEditClick = { vehicleToEdit = it },
                            modifier    = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  VEHICLE TABLE  (unchanged logic, modifier param added for weight support)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun VehicleTable(
    vehicles: List<VehicleData>,
    token: String,
    viewModel: VehicleViewModel,
    onEditClick: (VehicleData) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.horizontalScroll(rememberScrollState())) {

            // Header row
            Row(
                modifier = Modifier
                    .width(950.dp)
                    .background(Color(0xFFF9FAFB), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeaderText("VEHICLE NO.", 1.2f)
                HeaderText("OWNER",       2f)
                HeaderText("DEPT",        1f)
                HeaderText("TYPE",        1f)
                HeaderText("STICKER",     1f)
                HeaderText("MOBILE",      1.3f)
                HeaderText("ACTIONS",     1f)
            }

            HorizontalDivider(color = Color(0xFFE5E7EB))

            LazyColumn {
                items(vehicles) { vehicle ->
                    VehicleRow(
                        vehicle     = vehicle,
                        token       = token,
                        viewModel   = viewModel,
                        onEditClick = onEditClick
                    )
                    HorizontalDivider(color = Color(0xFFF3F4F6))
                }
            }
        }
    }
}

@Composable
fun TableHeader() {
    Row(
        modifier = Modifier.width(950.dp).padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderText("VEHICLE NO.", 1.2f)
        HeaderText("OWNER",       2f)
        HeaderText("DEPT",        1f)
        HeaderText("TYPE",        1f)
        HeaderText("STICKER",     1f)
        HeaderText("MOBILE",      1.3f)
        HeaderText("ACTIONS",     1f)
    }
}

@Composable
fun RowScope.HeaderText(text: String, weight: Float) {
    Text(
        text,
        modifier   = Modifier.weight(weight),
        color      = Color(0xFF6B7280),
        fontWeight = FontWeight.Bold,
        fontSize   = 11.sp,
        letterSpacing = 0.5.sp
    )
}

@Composable
fun VehicleRow(
    vehicle: VehicleData,
    token: String,
    viewModel: VehicleViewModel,
    onEditClick: (VehicleData) -> Unit
) {
    Row(
        modifier = Modifier.width(950.dp).padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VehiclePlate(vehicle.vehicleNo)
        OwnerColumn(vehicle.name, vehicle.fathersName)
        Chip(vehicle.dept, 1f)
        Chip(vehicle.vehicleType, 1f)
        Text(vehicle.stickerNo, modifier = Modifier.weight(1f), fontSize = 13.sp)
        Text(vehicle.mobileNo,  modifier = Modifier.weight(1.3f), fontSize = 13.sp)
        ActionButtons(
            onEditClick   = { onEditClick(vehicle) },
            onDeleteClick = { viewModel.deleteAVehicle(token, vehicle.vehicleNo) }
        )
    }
}

@Composable
fun RowScope.VehiclePlate(number: String) {
    Box(modifier = Modifier.weight(1.2f)) {
        Box(
            modifier = Modifier
                .background(Color(0xFF1A1C1E), RoundedCornerShape(5.dp))
                .padding(horizontal = 7.dp, vertical = 4.dp)
        ) {
            Text(number, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 0.5.sp)
        }
    }
}

@Composable
fun RowScope.OwnerColumn(owner: String, father: String) {
    Column(modifier = Modifier.weight(2f)) {
        Text(owner,  fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF1A1C1E))
        Text(father, color = Color(0xFF9CA3AF), fontSize = 11.sp)
    }
}

@Composable
fun RowScope.Chip(text: String, weight: Float) {
    Box(modifier = Modifier.weight(weight)) {
        Box(
            modifier = Modifier
                .background(Color(0xFFE6EEF6), RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(text, color = Color(0xFF3A7BBF), fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
        }
    }
}

@Composable
fun RowScope.ActionButtons(onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Row(modifier = Modifier.weight(1f)) {
        IconButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF6B7280))
        }
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626))
        }
    }
}

// SearchBar kept for backward compat but VehicleScreen now uses inline OutlinedTextField
@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value         = query,
        onValueChange = onQueryChange,
        placeholder   = { Text("Search by plate, name, dept…", color = Color(0xFFADB5BD), fontSize = 13.sp) },
        leadingIcon   = { Icon(Icons.Default.Search, null, tint = Color(0xFFADB5BD), modifier = Modifier.size(18.dp)) },
        trailingIcon  = {
            AnimatedVisibility(visible = query.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
                IconButton(onClick = { onQueryChange("") }, modifier = Modifier.size(18.dp)) {
                    Icon(Icons.Default.Close, null, tint = Color(0xFFADB5BD))
                }
            }
        },
        singleLine = true,
        shape      = RoundedCornerShape(10.dp),
        colors     = OutlinedTextFieldDefaults.colors(
            focusedContainerColor   = Color(0xFFF9FAFB),
            unfocusedContainerColor = Color(0xFFF9FAFB),
            focusedBorderColor      = Color(0xFF16A34A),
            unfocusedBorderColor    = Color(0xFFE5E7EB),
        ),
        modifier = Modifier.fillMaxWidth().height(50.dp)
    )
}