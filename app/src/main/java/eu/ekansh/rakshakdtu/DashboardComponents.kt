package eu.ekansh.rakshakdtu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.ekansh.rakshakdtu.ui.theme.*

@Composable
fun SecurityHeadHeader(date: String = "Jan 24, 2024") {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)) {
        Text(
            text = "Security Head",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(SuccessGreen, CircleShape)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "System Status: ",
                fontSize = 14.sp,
                color = TextGrey
            )
            Text(
                text = "All Nodes Operational",
                fontSize = 14.sp,
                color = SuccessGreen,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = " • $date",
                fontSize = 14.sp,
                color = TextGrey
            )
        }
    }
}

@Composable
fun SecurityStatCard(
    title: String,
    value: String,
    subtitle: String? = null,
    trend: String? = null,
    icon: ImageVector,
    iconColor: Color = StatBlue,
    progress: Float? = null,
    isAlert: Boolean = false
) {
    val borderColor = if (isAlert) UnauthorizedRed else BorderGrey
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(text = title.uppercase(), fontSize = 12.sp, color = TextGrey, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(text = value, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = if (isAlert) UnauthorizedRed else Color.Black)
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(if (isAlert) UnauthorizedRed.copy(alpha = 0.1f) else iconColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = if (isAlert) UnauthorizedRed else iconColor, modifier = Modifier.size(24.dp))
                }
            }
            
            if (progress != null) {
                Spacer(Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = iconColor,
                    trackColor = iconColor.copy(alpha = 0.1f)
                )
            }
            
            if (subtitle != null || trend != null) {
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (trend != null) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp, 
                            contentDescription = null, 
                            tint = SuccessGreen, 
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(text = trend, color = SuccessGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(4.dp))
                    }
                    if (subtitle != null) {
                        Text(text = subtitle, color = TextGrey, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun TrafficFlowCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, BorderGrey, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Vehicle Traffic Flow", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Weekly entry vs exit analysis", fontSize = 14.sp, color = TextGrey)
                }
                Surface(
                    color = FlaggedGrey,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Last 7 Days", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Icon(Icons.Default.KeyboardArrowDown, null, modifier = Modifier.size(16.dp))
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            // area chart visualization
            Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val path = androidx.compose.ui.graphics.Path()
                    val width = size.width
                    val height = size.height
                    
                    path.moveTo(0f, height * 0.7f)
                    path.quadraticBezierTo(width * 0.2f, height * 0.6f, width * 0.4f, height * 0.8f)
                    path.quadraticBezierTo(width * 0.6f, height * 0.9f, width * 0.8f, height * 0.5f)
                    path.lineTo(width, height * 0.4f)
                    path.lineTo(width, height)
                    path.lineTo(0f, height)
                    path.close()
                    
                    drawPath(
                        path = path,
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(StatBlue.copy(alpha = 0.3f), Color.Transparent)
                        )
                    )
                    
                    val linePath = androidx.compose.ui.graphics.Path()
                    linePath.moveTo(0f, height * 0.7f)
                    linePath.quadraticBezierTo(width * 0.2f, height * 0.6f, width * 0.4f, height * 0.8f)
                    linePath.quadraticBezierTo(width * 0.6f, height * 0.9f, width * 0.8f, height * 0.5f)
                    linePath.lineTo(width, height * 0.4f)
                    
                    drawPath(
                        path = linePath,
                        color = StatBlue,
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN").forEach { day ->
                    Text(text = day, fontSize = 10.sp, color = TextGrey, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AuthorizationStatusCard(successRate: Int = 82) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, BorderGrey, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Authorization Status", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
            Text(text = "Today's scan breakdown", fontSize = 14.sp, color = TextGrey, modifier = Modifier.align(Alignment.Start))
            
            Spacer(Modifier.height(24.dp))
            
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(160.dp)) {
                Canvas(modifier = Modifier.size(160.dp)) {
                    drawArc(
                        color = FlaggedGrey,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = AuthBlue,
                        startAngle = -90f,
                        sweepAngle = (successRate / 100f) * 360f,
                        useCenter = false,
                        style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$successRate%", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Text(text = "SUCCESS RATE", fontSize = 10.sp, color = TextGrey, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusLegendItem("Authorized", "256", AuthBlue)
                StatusLegendItem("Unauthorized", "42", UnauthorizedRed)
                StatusLegendItem("Flagged", "14", FlaggedGrey)
            }
        }
    }
}

@Composable
private fun StatusLegendItem(label: String, count: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(10.dp).background(color, CircleShape))
            Spacer(Modifier.width(8.dp))
            Text(text = label, fontSize = 14.sp, color = TextGrey)
        }
        Text(text = count, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RecentScanActivityTable(activities: List<ScanActivity>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, BorderGrey, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Recent Scan Activity", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = "View All", fontSize = 12.sp, color = StatBlue, fontWeight = FontWeight.Bold)
            }
            
            Spacer(Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth().background(FlaggedGrey.copy(alpha = 0.5f)).padding(8.dp)) {
                Text(text = "TIME", modifier = Modifier.weight(1f), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGrey)
                Text(text = "PLATE NO", modifier = Modifier.weight(1f), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGrey)
                Text(text = "LOCATION", modifier = Modifier.weight(1.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGrey)
                Text(text = "STATUS", modifier = Modifier.weight(1f), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextGrey)
            }
            
            activities.forEach { activity ->
                ScanTableRow(activity)
            }
        }
    }
}

@Composable
fun ScanTableRow(activity: ScanActivity) {
    val formattedTime = try {
        // Simple extraction of HH:mm from ISO string
        if (activity.entryTime.contains("T")) {
            activity.entryTime.substringAfter("T").substring(0, 8)
        } else {
            activity.entryTime
        }
    } catch (e: Exception) {
        activity.entryTime
    }

    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = formattedTime, modifier = Modifier.weight(1f), fontSize = 12.sp)
        Text(text = activity.vehicleNo, modifier = Modifier.weight(1f), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AuthBlue)
        Text(text = activity.camera, modifier = Modifier.weight(1.5f), fontSize = 12.sp)
        Box(modifier = Modifier.weight(1f)) {
            Surface(
                color = if (activity.authorized) SuccessGreen.copy(alpha = 0.1f) else UnauthorizedRed.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = if (activity.authorized) "AUTH" else "UNAUTH",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (activity.authorized) SuccessGreen else UnauthorizedRed
                )
            }
        }
    }
    HorizontalDivider(color = BorderGrey.copy(alpha = 0.5f))
}

@Composable
fun RecentAlertsSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Recent Alerts", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Surface(color = UnauthorizedRed, shape = RoundedCornerShape(4.dp)) {
                Text(text = "3 NEW", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
            }
        }
        
        Spacer(Modifier.height(12.dp))
        
        AlertCard(
            title = "Unauthorized Entry",
            time = "2m ago",
            desc = "Plate UP16BZ9901 failed biometric link at Gate 2.",
            icon = Icons.Default.ErrorOutline,
            color = UnauthorizedRed
        )
        
        AlertCard(
            title = "Camera Offline: Se...",
            time = "15m ago",
            desc = "CCTV_S4_N2 has lost connection to central node.",
            icon = Icons.Default.VideocamOff,
            color = StatBlue,
            isSystem = true
        )
        
        AlertCard(
            title = "Overstay Warning",
            time = "1h ago",
            desc = "Vehicle DL1CA2344 has exceeded guest parking limit.",
            icon = Icons.Default.AccessTime,
            color = TextGrey
        )
        
        Spacer(Modifier.height(12.dp))
        
        OutlinedButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = borderStroke(1.dp, BorderGrey)
        ) {
            Text(text = "DISMISS ALL", color = TextGrey, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AlertCard(title: String, time: String, desc: String, icon: ImageVector, color: Color, isSystem: Boolean = false) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, if (isSystem) BorderGrey else color.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = if (isSystem) FlaggedGrey.copy(alpha = 0.3f) else color.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = time, fontSize = 12.sp, color = TextGrey)
                }
                Text(text = desc, fontSize = 12.sp, color = TextGrey)
            }
        }
    }
}

// Helper to avoid material2/material3 ambiguity if any
fun borderStroke(width: androidx.compose.ui.unit.Dp, color: Color) = androidx.compose.foundation.BorderStroke(width, color)
