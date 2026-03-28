package eu.ekansh.rakshakdtu

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun AppBarView(navController: NavHostController) {  // navController = outer/root

    val drawerState        = rememberDrawerState(DrawerValue.Closed)
    val scope              = rememberCoroutineScope()
    val localNavController = rememberNavController()   // inner — tabs only
    val vehicleViewModel: VehicleViewModel = viewModel()
    val cameraViewModel: CameraViewModel   = viewModel()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                AppDrawer(
                    navController     = localNavController,
                    rootNavController = navController,
                    onCloseDrawer     = { scope.launch { drawerState.close() } }
                )
            }
        }
    ) {
        Scaffold(
            containerColor = colorResource(id = R.color.background_color),
            topBar = {
                TopAppBar(
                    title = { Text("DTU Rakshak", color = Color.White) },
                    backgroundColor = colorResource(id = R.color.app_bar_color),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White)
                        }
                    }
                )
            }
        ) { padding ->
            NavHost(
                navController      = localNavController,
                startDestination   = Screen.DashboardScreen.route,
                modifier           = Modifier.padding(padding)
            ) {
                composable(Screen.DashboardScreen.route) {
                    DashBoardScreen(
                        vehicleViewModel = vehicleViewModel,
                        cameraViewModel  = cameraViewModel,
                        navController    = navController
                    )
                }
                composable(Screen.CameraScreen.route)  { CameraScreen(navController = navController) }
                composable(Screen.VehicleScreen.route) { VehicleScreen(navController = navController) }
                composable(Screen.LogScreen.route)     { LogScreen(navController = navController) }
                composable(Screen.UpdatePasswordScreen.route) {
                    UpdatePasswordScreen(rootNavController = navController)
                }
            }
        }
    }
}