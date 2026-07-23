package eu.ekansh.rakshakdtu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.ekansh.rakshakdtu.ui.theme.*

@Composable
fun RowScope.VehicleStatCard(
    title: String,
    value: String,
    trend: String? = null,
    badgeText: String? = null,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .height(110.dp)
            .border(1.dp, BorderGrey, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                if (trend != null) {
                    Text(text = trend, color = SuccessGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.background(SuccessGreen.copy(0.1f), RoundedCornerShape(4.dp)).padding(horizontal = 4.dp, vertical = 2.dp))
                } else if (badgeText != null) {
                    Text(text = badgeText, color = SuccessGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.background(SuccessGreen.copy(0.1f), RoundedCornerShape(4.dp)).padding(horizontal = 4.dp, vertical = 2.dp))
                }
            }
            Spacer(Modifier.weight(1f))
            Text(text = title.uppercase(), fontSize = 10.sp, color = TextGrey, fontWeight = FontWeight.Bold)
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

@Composable
fun VehicleActivityCard(
    vehicle: VehicleData,
    onHistoryClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // Determine status - hardcoded for now or based on some logic if available
    val status = if (vehicle.dept == "Admin") "EXPIRED" else if (vehicle.vehicleType == "Visitor") "GUEST PASS" else "VALID"
    val statusColor = when(status) {
        "VALID" -> SuccessGreen
        "EXPIRED" -> UnauthorizedRed
        else -> AuthBlue
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(1.dp, if (status == "EXPIRED") UnauthorizedRed.copy(0.2f) else BorderGrey, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = vehicle.vehicleNo.chunked(2).joinToString(" "),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AuthBlue,
                    letterSpacing = 1.sp
                )
                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }
            
            Spacer(Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "OWNER", fontSize = 10.sp, color = TextGrey, fontWeight = FontWeight.Bold)
                    Text(text = vehicle.name, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "DEPT", fontSize = 10.sp, color = TextGrey, fontWeight = FontWeight.Bold)
                    Text(text = vehicle.dept, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
            
            Spacer(Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "STICKER NO.", fontSize = 10.sp, color = TextGrey, fontWeight = FontWeight.Bold)
                    Text(text = vehicle.stickerNo, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Icon(Icons.Default.History, null, modifier = Modifier.size(20.dp).clickable { onHistoryClick() }, tint = TextGrey)
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(20.dp).clickable { onEditClick() }, tint = TextGrey)
                    Icon(Icons.Default.Delete, null, modifier = Modifier.size(20.dp).clickable { onDeleteClick() }, tint = TextGrey)
                }
            }
        }
    }
}
