package eu.ekansh.rakshakdtu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Videocam
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
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    navController: NavHostController,          // local (inner) nav — for tab switching
    rootNavController: NavHostController,      // ✅ outer nav — for sign out → LoginScreen
    onCloseDrawer: () -> Unit,
    onItemClick: (String) -> Unit = {},
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val authViewModel: AuthViewModel = viewModel()
    val scope = rememberCoroutineScope()

    val userEmail    = authViewModel.storedEmail.value
    val displayName  = userEmail?.substringBefore("@") ?: "Admin"
    val avatarLetter = displayName.first().uppercaseChar().toString()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1620))
            .padding(16.dp)
    ) {
        // ── Header ────────────────────────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(R.drawable.rakshak_logo),
                contentDescription = "logo",
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text("DTU Rakshak", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("CAMPUS SECURITY", color = Color.Gray, fontSize = 12.sp)
            }
        }

        Text("MAIN", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(vertical = 12.dp))

        // ── Nav items (use inner navController) ───────────────────────────
        DrawerItem(
            icon = Icons.Default.Dashboard, title = "Dashboard",
            selected = currentRoute == Screen.DashboardScreen.route,
            onClick = {
                navController.navigate(Screen.DashboardScreen.route) {
                    popUpTo(Screen.DashboardScreen.route); launchSingleTop = true
                }
                onCloseDrawer()
            }
        )
        DrawerItem(
            icon = Icons.Default.DirectionsCar, title = "Vehicles",
            selected = currentRoute == Screen.VehicleScreen.route,
            onClick = {
                navController.navigate(Screen.VehicleScreen.route) {
                    popUpTo(Screen.DashboardScreen.route); launchSingleTop = true
                }
                onCloseDrawer()
            }
        )
        DrawerItem(
            icon = Icons.Default.Videocam, title = "Cameras",
            selected = currentRoute == Screen.CameraScreen.route,
            onClick = {
                navController.navigate(Screen.CameraScreen.route) {
                    popUpTo(Screen.DashboardScreen.route); launchSingleTop = true
                }
                onCloseDrawer()
            }
        )
        DrawerItem(
            icon = Icons.Default.ListAlt, title = "Entry/Exit Logs",
            selected = currentRoute == Screen.LogScreen.route,
            onClick = {
                navController.navigate(Screen.LogScreen.route) {
                    popUpTo(Screen.DashboardScreen.route); launchSingleTop = true
                }
                onCloseDrawer()
            }
        )

        Spacer(Modifier.weight(1f))

        // ── User section ──────────────────────────────────────────────────
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(Color(0xFF1A6AC8), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(avatarLetter, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(displayName, color = Color.White, fontWeight = FontWeight.Medium)
                Text(userEmail ?: "DTU Campus", color = Color.Gray, fontSize = 11.sp, maxLines = 1)
            }
        }

        Spacer(Modifier.height(12.dp))

        // ── Sign out ──────────────────────────────────────────────────────
        OutlinedButton(
            onClick = {
                scope.launch {
                    // ✅ await signOut() fully (clears token + email) before navigating
                    authViewModel.signOut()
                    // ✅ use rootNavController — LoginScreen lives on the outer graph
                    rootNavController.navigate(Screen.LoginScreen.route) {
                        popUpTo(0) { inclusive = true }
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
fun DrawerItem(icon: ImageVector, title: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(if (selected) Color(0xFF123524) else Color.Transparent, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Icon(icon, title, tint = if (selected) Color(0xFF1DB954) else Color.Gray)
        Spacer(Modifier.width(12.dp))
        Text(title, color = if (selected) Color(0xFF1DB954) else Color.Gray, fontSize = 16.sp)
    }
    Spacer(Modifier.height(6.dp))
}