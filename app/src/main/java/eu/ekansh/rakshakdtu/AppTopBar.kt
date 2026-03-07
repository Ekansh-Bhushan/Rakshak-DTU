package eu.ekansh.rakshakdtu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun AppBarView(navController: NavHostController) {


    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val localnavController = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                AppDrawer(
                    navController = localnavController,
                    onItemClick = {}
                )
            }
        }
    ) {

        Scaffold(

            topBar = {
                TopAppBar(
                    title = { Text("DTU Rakshak", color = Color.White) },
                    backgroundColor = colorResource(id = R.color.app_bar_color),

                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                )
            }

        ) { padding ->

            /* SCREEN CONTENT */
            NavHost(
                navController = localnavController,
                startDestination = Screen.DashboardScreen.route,
                modifier = Modifier.padding(padding)
            ) {

                composable(Screen.DashboardScreen.route) {
                    DashBoardScreen(navController)
                }

                composable(Screen.CameraScreen.route) {
                    CameraScreen()
                }

                composable(Screen.VehicleScreen.route) {
                    VehicleScreen()
                }

                composable(Screen.LogScreen.route) {
                    LogScreen()
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun AppBarViewPreview() {
//    AppBarView()
}