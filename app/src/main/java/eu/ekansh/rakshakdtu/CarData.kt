package eu.ekansh.rakshakdtu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatusChip(authorized: Boolean) {

    val bgColor = if (authorized) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
    val textColor = if (authorized) Color(0xFF2E7D32) else Color(0xFFD32F2F)
    val label = if (authorized) "Authorized" else "✗ Unauthorized"

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ScanRow(activity: ScanActivity) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .background(Color(0xFF1F2333), RoundedCornerShape(6.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                activity.vehicleNo,
                color = Color.White,
                fontSize = 12.sp
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {

            Text(
                activity.camera,
                fontSize = 13.sp,
                color = Color.Gray
            )

            Text(
                activity.entryTime,
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.width(12.dp))

        Text(
            activity.duration ?: "—",
            fontSize = 13.sp,
            modifier = Modifier.width(40.dp)
        )

        Spacer(Modifier.width(8.dp))

        StatusChip(activity.authorized)
    }
}

@Composable
fun RecentScanActivityCard(activities: List<ScanActivity>) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    "Recent Scan Activity",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    "",
                    color = Color(0xFF2E7D32),
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            Divider()

            activities.forEachIndexed { index, activity ->

                ScanRow(activity)

                if (index != activities.lastIndex)
                    Divider()
            }
        }
    }
}