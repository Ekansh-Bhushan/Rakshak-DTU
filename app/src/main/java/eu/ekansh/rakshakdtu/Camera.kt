package eu.ekansh.rakshakdtu

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun CameraScreen(
    viewModel: CameraViewModel = viewModel(),
    navController: NavHostController
) {
    val cameras      by viewModel.cameraList
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
            viewModel.getAllCameraDetails(token)
        } else {
            viewModel.errorMessage.value = "No session found. Please login."
            navController.navigate(Screen.LoginScreen.route)
        }
    }

    val totalCameraCount              = cameras?.size ?: 0
    var showRegisterForm              by remember { mutableStateOf(false) }
    var showImportExcelForm           by remember { mutableStateOf(false) }
    var cameraToEdit: CameraData?     by remember { mutableStateOf(null) }

    // Filtered list driven by search query (client-side, same pattern as VehicleScreen)
    val displayedCameras = remember(cameras, searchQuery) {
        val list = cameras ?: emptyList()
        if (searchQuery.isBlank()) list
        else list.filter {
            it.cameraLocation.contains(searchQuery, ignoreCase = true) ||
                    it.cameraType.contains(searchQuery, ignoreCase = true) ||
                    it.id.contains(searchQuery, ignoreCase = true)
        }
    }

    // ── Dialogs ───────────────────────────────────────────────────────────────
    cameraToEdit?.let { camera ->
        androidx.compose.ui.window.Dialog(onDismissRequest = { cameraToEdit = null }) {
            Card(shape = RoundedCornerShape(16.dp)) {
                EditCameraForm(
                    token     = storedToken ?: "",
                    camera    = camera,
                    viewModel = viewModel,
                    onClose   = { cameraToEdit = null }
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
                RegisterCameraForm(token = storedToken ?: "", onClose = { showRegisterForm = false })
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
                ImportCameraDialog(onClose = { showImportExcelForm = false })
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
            "Campus Cameras",
            fontWeight = FontWeight.Bold,
            fontSize   = 22.sp,
            color      = Color(0xFF1A1C1E)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Manage CCTV camera points — $totalCameraCount installed",
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
                Text("Add Camera")
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Search + table inside Card ────────────────────────────────────────
        Card(
            shape     = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            modifier  = Modifier.fillMaxWidth().weight(1f)
        ) {
            Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {

                // Search bar
                OutlinedTextField(
                    value         = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder   = {
                        Text(
                            "Search by location, type, ID…",
                            color    = Color(0xFFADB5BD),
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon  = {
                        Icon(
                            Icons.Default.Search, null,
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
                                onClick  = { searchQuery = "" },
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
                        "${displayedCameras.size} Records",
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color      = Color(0xFF6B7280)
                    )
                }

                Spacer(Modifier.height(10.dp))

                // Table / states
                when {
                    errorMessage != null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Error: $errorMessage", color = Color.Red)
                        }
                    }
                    cameras == null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF16A34A))
                        }
                    }
                    else -> {
                        CameraTable(
                            cameras  = displayedCameras,
                            onDelete = { id -> storedToken?.let { viewModel.deleteACamera(it, id) } },
                            onEdit   = { cameraToEdit = it },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  CAMERA TABLE
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun CameraTable(
    cameras: List<CameraData>,
    onDelete: (String) -> Unit,
    onEdit: (CameraData) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.horizontalScroll(rememberScrollState())) {

            // Header
            Row(
                modifier = Modifier
                    .width(950.dp)
                    .background(Color(0xFFF9FAFB), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CameraHeaderText("LOCATION",   1.4f)
                CameraHeaderText("TYPE",       1f)
                CameraHeaderText("COORDINATES",1.6f)
                CameraHeaderText("CAMERA ID",  1f)
                CameraHeaderText("REGISTERED", 1f)
                CameraHeaderText("ACTIONS",    0.8f)
            }

            HorizontalDivider(color = Color(0xFFE5E7EB))

            if (cameras.isEmpty()) {
                Box(
                    modifier = Modifier.width(950.dp).height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No cameras found", color = Color(0xFF9CA3AF), fontSize = 14.sp)
                }
            } else {
                LazyColumn {
                    items(cameras, key = { it.id }) { camera ->
                        CameraRow(
                            camera   = camera,
                            onDelete = { onDelete(camera.id) },
                            onEdit   = { onEdit(camera) }
                        )
                        HorizontalDivider(color = Color(0xFFF3F4F6))
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.CameraHeaderText(text: String, weight: Float) {
    Text(
        text,
        modifier      = Modifier.weight(weight),
        color         = Color(0xFF6B7280),
        fontWeight    = FontWeight.Bold,
        fontSize      = 11.sp,
        letterSpacing = 0.5.sp
    )
}

@Composable
fun CameraTableHeader() {
    Row(
        modifier = Modifier.width(950.dp).padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CameraHeaderText("LOCATION",    1.4f)
        CameraHeaderText("TYPE",        1f)
        CameraHeaderText("COORDINATES", 1.6f)
        CameraHeaderText("CAMERA ID",   1f)
        CameraHeaderText("REGISTERED",  1f)
        CameraHeaderText("ACTIONS",     0.8f)
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  CAMERA ROW
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun CameraRow(
    camera: CameraData,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier
            .width(950.dp)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Location — dark plate style
        Box(modifier = Modifier.weight(1.4f)) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF1A1C1E), RoundedCornerShape(5.dp))
                    .padding(horizontal = 7.dp, vertical = 4.dp)
            ) {
                Text(
                    camera.cameraLocation,
                    color         = Color.White,
                    fontWeight    = FontWeight.Bold,
                    fontSize      = 11.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Type chip
        CameraChip(camera.cameraType, 1f)

        // Coordinates
        Column(modifier = Modifier.weight(1.6f)) {
            Text("${camera.lat}", color = Color(0xFF374151), fontSize = 12.sp)
            Text("${camera.long}", color = Color(0xFF9CA3AF), fontSize = 11.sp)
        }

        // Short Camera ID chip
        CameraChip(camera.id.takeLast(8), 1f)

        // Registered date
        Text(
            text     = camera.createdAt.split("T")[0],
            modifier = Modifier.weight(1f),
            fontSize = 12.sp,
            color    = Color(0xFF374151)
        )

        // Actions
        Row(modifier = Modifier.weight(0.8f)) {
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF6B7280))
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626))
            }
        }
    }
}

@Composable
fun RowScope.CameraPlate(number: String) {
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
fun RowScope.CameraColumn(lat: String, long: String) {
    Column(modifier = Modifier.weight(2f)) {
        Text(lat,  color = Color(0xFF374151), fontSize = 12.sp)
        Text(long, color = Color(0xFF9CA3AF), fontSize = 11.sp)
    }
}

@Composable
fun RowScope.CameraChip(text: String, weight: Float) {
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
fun RowScope.CameraActionButtons() {
    Row(modifier = Modifier.weight(1f)) {
        IconButton(onClick = {}) {
            Icon(Icons.Default.Edit, contentDescription = null, tint = Color(0xFF6B7280))
        }
        IconButton(onClick = {}) {
            Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFDC2626))
        }
    }
}