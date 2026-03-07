package eu.ekansh.rakshakdtu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import eu.ekansh.rakshakdtu.data.TokenManager
import kotlinx.coroutines.launch

@Composable
fun DashBoardScreen(navController: NavHostController? = null) {

    val sampleData = listOf(
        ScanActivity(
            "DL11SK5193",
            "Main Gate — South Campus",
            "26/2/2026, 10:07:29 am",
            null,
            false
        ),
        ScanActivity(
            "DL11SK5193",
            "Faculty Parking Bay",
            "26/2/2026, 8:33:34 am",
            "1m",
            false
        ),
        ScanActivity(
            "DL11SK5193",
            "Hostel Gate",
            "26/2/2026, 7:34:48 am",
            null,
            false
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_color)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            DashboardStatCard(
                title = "Registered Vehicles",
                value = "10",
                icon = Icons.Default.DirectionsCar,
                iconColor = Color(0xFF2E7D32),
                backgroundColor = Color(0xFFE8F5E9)
            )
        }

        item {
            DashboardStatCard(
                title = "Active Cameras",
                value = "11",
                icon = Icons.Default.Videocam,
                iconColor = Color(0xFF1565C0),
                backgroundColor = Color(0xFFE3F2FD)
            )
        }

        item {
            DashboardStatCard(
                title = "Vehicles on Campus",
                value = "2",
                icon = Icons.Default.ListAlt,
                iconColor = Color(0xFFEF6C00),
                backgroundColor = Color(0xFFFFF3E0)
            )
        }

        item {
            DashboardStatCard(
                title = "Unauthorized Today",
                value = "3",
                icon = Icons.Default.Warning,
                iconColor = Color(0xFFC62828),
                backgroundColor = Color(0xFFFFEBEE)
            )
        }

//        item {
//            VehicleEntriesChart()
//        }

        item {
            AuthorizationDonutChart(
                authorized = 6f,
                unauthorized = 10f
            )
        }

        item {
            RecentScanActivityCard(sampleData)
        }
    }
}
@Composable
fun DashboardStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    backgroundColor: Color
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(backgroundColor, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Text(
                    text = value,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DashBoardScreenPreview() {
    DashBoardScreen()
}