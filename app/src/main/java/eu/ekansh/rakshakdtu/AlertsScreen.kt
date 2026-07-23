package eu.ekansh.rakshakdtu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.ekansh.rakshakdtu.ui.theme.*

@Composable
fun AlertsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Operational KPIs Header
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Operational KPIs", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "Last 24h", fontSize = 12.sp, color = TextGrey, fontWeight = FontWeight.Medium)
            }
        }

        // Horizontal KPIs Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AlertStatCard(
                    title = "ACTIVE ALERTS",
                    value = "12",
                    trend = "14%",
                    icon = Icons.Default.Warning,
                    color = UnauthorizedRed
                )
                AlertStatCard(
                    title = "AVG RESOLUTION",
                    value = "14.2m",
                    trend = "4.1m",
                    icon = Icons.Default.Schedule,
                    color = AuthBlue
                )
                AlertStatCard(
                    title = "CRITICAL ISSUES",
                    value = "03",
                    subtitle = "Immediate Dispatch",
                    icon = Icons.Default.PriorityHigh,
                    color = UnauthorizedRed
                )
            }
        }

        // Alert Trends Card
        item {
            TrafficFlowCard() // Using the same visualization logic for now as placeholders are similar
        }

        // Alert Types Card
        item {
            AuthorizationStatusCard() // Same here, visualization matches the breakdown donut
        }

        // Live Alert Feed Header
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Live Alert Feed", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = {}, modifier = Modifier.background(FlaggedGrey, RoundedCornerShape(8.dp)).size(36.dp)) {
                    Icon(Icons.Default.FilterList, null, tint = TextGrey, modifier = Modifier.size(20.dp))
                }
            }
        }

        // Alert Feed Items
        item {
            AlertFeedItem(
                title = "Unauthorized Entry",
                priority = "P1",
                location = "Gate 4 - Main Ent.",
                time = "2 mins ago",
                description = "Vehicle DL-3C-AS-9982 flagged on blacklist. Entry attempted.",
                icon = Icons.Default.GppMaybe
            )
        }

        item {
            AlertFeedItem(
                title = "Camera Offline",
                priority = "P2",
                location = "Admin Block - Parking",
                time = "15 mins ago",
                description = "",
                icon = Icons.Default.VideocamOff,
                status = "Acknowledged"
            )
        }
        
        item { Spacer(Modifier.height(80.dp)) }
    }
}
