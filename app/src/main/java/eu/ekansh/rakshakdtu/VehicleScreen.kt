package eu.ekansh.rakshakdtu

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
fun VehicleScreen(
    viewModel: VehicleViewModel = viewModel(),
    navController: NavHostController
) {
    val vehicles     by viewModel.vehicleList
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
            navController.navigate(Screen.LoginScreen.route)
        }
    }

    var vehicleToEdit: VehicleData? by remember { mutableStateOf(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBg),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search Bar (collapsible via header)
            item {
                AnimatedVisibility(visible = viewModel.showSearch.value) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { 
                            searchQuery = it
                            storedToken?.let { token -> viewModel.onSearchQueryChanged(token, it) }
                        },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        placeholder = { Text("Search plate, owner, dept...") },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { 
                                    searchQuery = "" 
                                    storedToken?.let { token -> viewModel.onSearchQueryChanged(token, "") }
                                }) { Icon(Icons.Default.Close, null) }
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

            // KPI Horizontal Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    VehicleStatCard(
                        title = "Total Registered",
                        value = vehicles?.size?.toString() ?: "0",
                        trend = "+12%",
                        icon = Icons.Default.DirectionsCar,
                        color = AuthBlue
                    )
                    VehicleStatCard(
                        title = "Active Today",
                        value = "1,822",
                        badgeText = "ACTIVE",
                        icon = Icons.Default.CheckCircle,
                        color = SuccessGreen
                    )
                }
            }

            // Recent Activity Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Recent Activity", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    TextButton(onClick = {}) {
                        Icon(Icons.Default.FilterList, null, modifier = Modifier.size(18.dp), tint = AuthBlue)
                        Spacer(Modifier.width(4.dp))
                        Text(text = "Filter", color = AuthBlue, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Vehicle List
            items(vehicles ?: emptyList()) { vehicle ->
                VehicleActivityCard(
                    vehicle = vehicle,
                    onHistoryClick = { /* Navigate to history */ },
                    onEditClick = { vehicleToEdit = vehicle },
                    onDeleteClick = { storedToken?.let { viewModel.deleteAVehicle(it, vehicle.vehicleNo) } }
                )
            }

            // Load More Button
            item {
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FlaggedGrey.copy(0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "LOAD MORE RECORDS", color = TextGrey, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.KeyboardArrowDown, null, tint = TextGrey)
                }
            }
            
            // Footer
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "© 2024 DTU Rakshak Campus Security System", fontSize = 10.sp, color = TextGrey.copy(0.6f))
                    Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(text = "Privacy Policy", fontSize = 10.sp, color = AuthBlue, fontWeight = FontWeight.Bold)
                        Text(text = "Support Desk", fontSize = 10.sp, color = AuthBlue, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            item { Spacer(Modifier.height(80.dp)) }
        }

        // Action Dialogs
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

        if (viewModel.showRegisterForm.value) {
            androidx.compose.ui.window.Dialog(onDismissRequest = { viewModel.showRegisterForm.value = false }) {
                Card(
                    modifier = Modifier.width(420.dp),
                    shape    = RoundedCornerShape(16.dp),
                    colors   = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    RegisterVehicleForm(token = storedToken ?: "", onClose = { viewModel.showRegisterForm.value = false })
                }
            }
        }
    }
}
