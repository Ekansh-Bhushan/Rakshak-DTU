package eu.ekansh.rakshakdtu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import eu.ekansh.rakshakdtu.data.TokenManager
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    navController: NavHostController,
    selectedItem: String = "Dashboard",
    onItemClick: (String) -> Unit,
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1620))
            .padding(16.dp)
    ) {

        /* ---------- HEADER ---------- */

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
        ) {

            Image(
                painter = painterResource(R.drawable.logo_dtu),
                contentDescription = "logo",
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    "DTU Rakshak",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    "CAMPUS SECURITY",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }

        /* ---------- SECTION TITLE ---------- */

        Text(
            "MAIN",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        /* ---------- MENU ITEMS ---------- */

        DrawerItem(
            icon = Icons.Default.Dashboard,
            title = "Dashboard",
            selected = currentRoute == Screen.DashboardScreen.route,
            onClick = {
                navController.navigate(Screen.DashboardScreen.route){
                    popUpTo(Screen.DashboardScreen.route)
                    launchSingleTop = true
                }
            }
        )

        DrawerItem(
            icon = Icons.Default.DirectionsCar,
            title = "Vehicles",
            selected = currentRoute == Screen.VehicleScreen.route,
            onClick = {
                navController.navigate(Screen.VehicleScreen.route){
                    popUpTo(Screen.DashboardScreen.route)
                    launchSingleTop = true
                }
            }
        )

        DrawerItem(
            icon = Icons.Default.Videocam,
            title = "Cameras",
            selected = currentRoute == Screen.CameraScreen.route,
            onClick = {
                navController.navigate(Screen.CameraScreen.route){
                    popUpTo(Screen.DashboardScreen.route)
                    launchSingleTop = true
                }
            }
        )

        DrawerItem(
            icon = Icons.Default.ListAlt,
            title = "Entry/Exit Logs",
            selected = currentRoute == Screen.LogScreen.route,
            onClick = {
                navController.navigate(Screen.LogScreen.route){
                    popUpTo(Screen.DashboardScreen.route)
                    launchSingleTop = true
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        /* ---------- USER SECTION ---------- */

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFF1DB954), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("A", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text("Admin", color = Color.White)
                Text("DTU Campus", color = Color.Gray, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        /* ---------- SIGN OUT ---------- */

        OutlinedButton(
            onClick = {
                scope.launch {

                    tokenManager.clearToken()

                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(0)
                    }
                }
            },
            border = BorderStroke(1.dp, Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Out", color = Color.Red)
        }
    }
}

@Composable
fun DrawerItem(
    icon: ImageVector,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    val background =
        if (selected) Color(0xFF123524) else Color.Transparent

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(background, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {

        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (selected) Color(0xFF1DB954) else Color.Gray
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            color = if (selected) Color(0xFF1DB954) else Color.Gray,
            fontSize = 16.sp
        )
    }

    Spacer(modifier = Modifier.height(6.dp))
}