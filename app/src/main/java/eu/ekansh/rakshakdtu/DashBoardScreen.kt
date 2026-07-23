package eu.ekansh.rakshakdtu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import eu.ekansh.rakshakdtu.data.TokenManager
import eu.ekansh.rakshakdtu.ui.theme.*

@Composable
fun DashBoardScreen(
    vehicleViewModel: VehicleViewModel = viewModel(),
    cameraViewModel: CameraViewModel = viewModel(),
    navController: NavHostController? = null
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }

    LaunchedEffect(Unit) {
        val token = tokenManager.getToken()
        if (!token.isNullOrEmpty()) {
            vehicleViewModel.getCampusVehicleDetails(token)
            vehicleViewModel.getAllVehiclesDetails(token)
            cameraViewModel.getAllCameraDetails(token)
        }
    }

    val campusData = vehicleViewModel.campusLogs.value
    val allVehicles = vehicleViewModel.vehicleList.value
    val cameras = cameraViewModel.cameraList.value

    val totalRegisteredCount = allVehicles?.size ?: 0
    val activeCamerasCount = cameras?.size ?: 0
    val totalCameras = 45 // Hardcoded for design match or get from VM if available
    val vehiclesOnCampusCount = campusData?.count ?: 0
    val unauthorizedToday = campusData?.logs?.count { !it.isAuthorized } ?: 0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { SecurityHeadHeader() }

        item {
            SecurityStatCard(
                title = "Registered Vehicles",
                value = String.format("%, d", totalRegisteredCount),
                trend = "+12%",
                subtitle = "Since last month",
                icon = Icons.Default.DirectionsCar
            )
        }

        item {
            SecurityStatCard(
                title = "Active Cameras",
                value = "$activeCamerasCount / $totalCameras",
                subtitle = "${if (totalCameras > 0) (activeCamerasCount * 100 / totalCameras) else 0}% ONLINE",
                progress = if (totalCameras > 0) activeCamerasCount.toFloat() / totalCameras else 0f,
                icon = Icons.Default.Videocam
            )
        }

        item {
            SecurityStatCard(
                title = "Vehicles on Campus",
                value = vehiclesOnCampusCount.toString(),
                subtitle = "REAL-TIME TRACKING ENABLED",
                icon = Icons.Default.Login
            )
        }

        item {
            SecurityStatCard(
                title = "Unauthorized Today",
                value = String.format("%02d", unauthorizedToday),
                subtitle = "Action Required",
                icon = Icons.Default.ReportProblem,
                isAlert = true
            )
        }

        item { TrafficFlowCard() }

        item { AuthorizationStatusCard() }

        item {
            val recentScans = campusData?.logs?.take(5)?.map { log ->
                ScanActivity(
                    vehicleNo = log.vehicleNo,
                    camera = log.camera.cameraLocation,
                    entryTime = log.entryTime,
                    duration = null,
                    authorized = log.isAuthorized
                )
            } ?: emptyList()
            
            RecentScanActivityTable(recentScans)
        }

        item { RecentAlertsSection() }
        
        item { Spacer(Modifier.height(80.dp)) } // Bottom nav padding
    }
}
