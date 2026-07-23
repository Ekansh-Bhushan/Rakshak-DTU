package eu.ekansh.rakshakdtu

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import eu.ekansh.rakshakdtu.data.TokenManager
import eu.ekansh.rakshakdtu.ui.theme.*

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = viewModel(),
    navController: NavHostController
) {
    val cameras      by viewModel.cameraList
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
            navController.navigate(Screen.LoginScreen.route)
        }
    }

    var cameraToEdit: CameraData? by remember { mutableStateOf(null) }

    val displayedCameras = remember(cameras, searchQuery) {
        val list = cameras ?: emptyList()
        if (searchQuery.isBlank()) list
        else list.filter {
            it.cameraLocation.contains(searchQuery, ignoreCase = true) ||
                    it.cameraType.contains(searchQuery, ignoreCase = true) ||
                    it.id.contains(searchQuery, ignoreCase = true)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBg),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search Bar (collapsible)
            item {
                AnimatedVisibility(visible = viewModel.showSearch.value) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        placeholder = { Text("Search location, type, ID...") },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) { Icon(Icons.Default.Close, null) }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AuthBlue,
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )
                }
            }

            // KPI Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val activeCount = displayedCameras.size
                    val totalCount = 45 // Design placeholder or real total if available
                    
                    VehicleStatCard(
                        title = "Active Cameras",
                        value = "$activeCount / $totalCount",
                        trend = "${if (totalCount > 0) (activeCount * 100 / totalCount) else 0}%",
                        icon = Icons.Default.Videocam,
                        color = StatBlue
                    )
                    VehicleStatCard(
                        title = "System Health",
                        value = "100%",
                        badgeText = "STABLE",
                        icon = Icons.Default.CheckCircle,
                        color = SuccessGreen
                    )
                }
            }

            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Camera Network", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    TextButton(onClick = {}) {
                        Icon(Icons.Default.FilterList, null, modifier = Modifier.size(18.dp), tint = AuthBlue)
                        Spacer(Modifier.width(4.dp))
                        Text(text = "Filter", color = AuthBlue, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Camera Cards
            items(displayedCameras) { camera ->
                CameraActivityCard(
                    camera = camera,
                    onEditClick = { cameraToEdit = camera },
                    onDeleteClick = { storedToken?.let { viewModel.deleteACamera(it, camera.id) } }
                )
            }

            item { Spacer(Modifier.height(80.dp)) }
        }

        // Dialogs
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

        if (viewModel.showRegisterForm.value) {
            androidx.compose.ui.window.Dialog(onDismissRequest = { viewModel.showRegisterForm.value = false }) {
                Card(
                    modifier = Modifier.width(420.dp),
                    shape    = RoundedCornerShape(16.dp),
                    colors   = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    RegisterCameraForm(token = storedToken ?: "", onClose = { viewModel.showRegisterForm.value = false })
                }
            }
        }
    }
}

@Composable
fun CameraActivityCard(
    camera: CameraData,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(1.dp, BorderGrey, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = camera.cameraLocation,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AuthBlue
                )
                Surface(
                    color = SuccessGreen.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = camera.cameraType,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )
                }
            }
            
            Spacer(Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "COORDINATES", fontSize = 10.sp, color = TextGrey, fontWeight = FontWeight.Bold)
                    Text(text = "${camera.lat}, ${camera.long}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "CAMERA ID", fontSize = 10.sp, color = TextGrey, fontWeight = FontWeight.Bold)
                    Text(text = camera.id.takeLast(8), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
            
            Spacer(Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "REGISTERED", fontSize = 10.sp, color = TextGrey, fontWeight = FontWeight.Bold)
                    Text(text = camera.createdAt.split("T")[0], fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(20.dp).clickable { onEditClick() }, tint = TextGrey)
                    Icon(Icons.Default.Delete, null, modifier = Modifier.size(20.dp).clickable { onDeleteClick() }, tint = TextGrey)
                }
            }
        }
    }
}
