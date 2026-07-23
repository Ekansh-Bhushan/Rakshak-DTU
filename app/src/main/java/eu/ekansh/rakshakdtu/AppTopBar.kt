package eu.ekansh.rakshakdtu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.ekansh.rakshakdtu.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarView(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val localNavController = rememberNavController()
    val vehicleViewModel: VehicleViewModel = viewModel()
    val cameraViewModel: CameraViewModel = viewModel()
    
    val navBackStackEntry by localNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        containerColor = LightBg,
        topBar = {
            when (currentRoute) {
                Screen.DashboardScreen.route -> SecurityProfileHeader(
                    actions = {
                        IconButton(onClick = {}) { Icon(Icons.Default.Search, null, tint = AuthBlue) }
                    }
                )
                Screen.VehicleScreen.route -> SecurityProfileHeader(
                    actions = {
                        IconButton(
                            onClick = { vehicleViewModel.showRegisterForm.value = true },
                            modifier = Modifier.background(AuthBlue, CircleShape).size(36.dp)
                        ) {
                            Icon(Icons.Default.Add, null, tint = Color.White)
                        }
                        Spacer(Modifier.width(8.dp))
                        IconButton(onClick = { vehicleViewModel.showSearch.value = !vehicleViewModel.showSearch.value }) { 
                            Icon(Icons.Default.Search, null, tint = AuthBlue) 
                        }
                    }
                )
                Screen.CameraScreen.route -> SecurityProfileHeader(
                    actions = {
                        IconButton(
                            onClick = { cameraViewModel.showRegisterForm.value = true },
                            modifier = Modifier.background(AuthBlue, CircleShape).size(36.dp)
                        ) {
                            Icon(Icons.Default.Add, null, tint = Color.White)
                        }
                        Spacer(Modifier.width(8.dp))
                        IconButton(onClick = { cameraViewModel.showSearch.value = !cameraViewModel.showSearch.value }) { 
                            Icon(Icons.Default.Search, null, tint = AuthBlue) 
                        }
                    }
                )
                Screen.LogScreen.route -> SecurityProfileHeader(
                    actions = {
                        IconButton(onClick = {}) { Icon(Icons.Default.Search, null, tint = AuthBlue) }
                    }
                )
                Screen.MapScreen.route -> SecurityProfileHeader(
                    actions = {
                        IconButton(onClick = {}) { Icon(Icons.Default.Search, null, tint = AuthBlue) }
                    }
                )
                "alerts" -> SecurityProfileHeader(
                    actions = {
                        IconButton(
                            onClick = { },
                            modifier = Modifier.background(PanicRed.copy(alpha = 0.1f), RoundedCornerShape(8.dp)).size(40.dp)
                        ) {
                            Icon(painter = painterResource(R.drawable.rakshak_logo), null, tint = PanicRed, modifier = Modifier.size(24.dp))
                        }
                    }
                )
                else -> SecurityProfileHeader()
            }
        },
        bottomBar = {
            SecurityBottomNavigation(localNavController)
        }
    ) { padding ->
        NavHost(
            navController = localNavController,
            startDestination = Screen.DashboardScreen.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.DashboardScreen.route) {
                DashBoardScreen(
                    vehicleViewModel = vehicleViewModel,
                    cameraViewModel = cameraViewModel,
                    navController = navController
                )
            }
            composable(Screen.VehicleScreen.route) { 
                VehicleScreen(viewModel = vehicleViewModel, navController = navController) 
            }
            composable(Screen.CameraScreen.route) { CameraScreen(navController = navController) }
            composable(Screen.LogScreen.route) { LogScreen(navController = navController) }
            composable(Screen.MapScreen.route) { MapScreen(navController = navController) }
            composable(Screen.UpdatePasswordScreen.route) {
                UpdatePasswordScreen(rootNavController = navController)
            }
            composable("alerts") {
                AlertsScreen()
            }
        }
    }
}

@Composable
fun SecurityProfileHeader(actions: @Composable RowScope.() -> Unit = {}) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(FlaggedGrey, CircleShape)
                        .border(1.dp, BorderGrey, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, tint = TextGrey, modifier = Modifier.size(28.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(text = "Security Head", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AuthBlue)
                    Text(text = "ADMIN OFFICE", fontSize = 12.sp, color = TextGrey, fontWeight = FontWeight.Medium)
                }
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                actions()
            }
        }
    }
}

@Composable
fun SecurityBottomNavigation(navController: NavHostController) {
    val items = listOf(
        NavigationItem("Dashboard", Screen.DashboardScreen.route, Icons.Default.Dashboard),
        NavigationItem("Vehicles", Screen.VehicleScreen.route, Icons.Default.DirectionsCar),
        NavigationItem("Camera", Screen.CameraScreen.route, Icons.Default.Videocam),
        NavigationItem("Logs", Screen.LogScreen.route, Icons.Default.ListAlt),
        NavigationItem("Alerts", "alerts", Icons.Default.Notifications),
        NavigationItem("Maps", Screen.MapScreen.route, Icons.Default.Map)
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(72.dp)
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = { 
                    BadgedBox(badge = {
                        if (item.title == "Alerts") {
                            Badge(containerColor = PanicRed)
                        }
                    }) {
                        Icon(
                            imageVector = item.icon, 
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = { 
                    Text(
                        text = item.title, 
                        fontSize = 8.sp, 
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        maxLines = 1,
                        softWrap = false
                    ) 
                },
                selected = selected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(Screen.DashboardScreen.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AuthBlue,
                    selectedTextColor = AuthBlue,
                    unselectedIconColor = TextGrey,
                    unselectedTextColor = TextGrey,
                    indicatorColor = AuthBlue.copy(alpha = 0.1f)
                )
            )
        }
    }
}

data class NavigationItem(val title: String, val route: String, val icon: ImageVector)
