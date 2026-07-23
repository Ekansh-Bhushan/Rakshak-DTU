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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.ekansh.rakshakdtu.ui.theme.*

@Composable
fun AlertStatCard(
    title: String,
    value: String,
    trend: String? = null,
    subtitle: String? = null,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(128.dp)
            .border(1.dp, BorderGrey, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Left border accent
            Box(modifier = Modifier.fillMaxHeight().width(4.dp).background(color).align(Alignment.CenterStart))
            
            Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = title, fontSize = 10.sp, color = TextGrey, fontWeight = FontWeight.Bold)
                    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                }
                Column {
                    Text(text = value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    if (trend != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrendingUp, null, tint = color, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(text = trend, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else if (subtitle != null) {
                        Text(text = subtitle, color = TextGrey, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun AlertFeedItem(
    title: String,
    priority: String,
    location: String,
    time: String,
    description: String,
    icon: ImageVector,
    status: String? = null
) {
    val priorityColor = if (priority == "P1") UnauthorizedRed else TextGrey
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(1.dp, BorderGrey, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxHeight().width(4.dp).background(priorityColor).align(Alignment.CenterStart))
            
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = icon, contentDescription = null, tint = priorityColor, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Surface(color = priorityColor, shape = RoundedCornerShape(20.dp)) {
                        Text(text = priority, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                    }
                }
                
                Spacer(Modifier.height(12.dp))
                
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "LOCATION", fontSize = 10.sp, color = TextGrey, fontWeight = FontWeight.Bold)
                        Text(text = location, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = if (status != null) "STATUS" else "TIME", fontSize = 10.sp, color = TextGrey, fontWeight = FontWeight.Bold)
                        Text(text = status ?: time, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = if (status != null) AuthBlue else Color.Black)
                    }
                }
                
                if (description.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    Box(modifier = Modifier.fillMaxWidth().background(LightBg, RoundedCornerShape(4.dp)).padding(8.dp)) {
                        Text(text = description, fontSize = 11.sp, color = TextGrey, lineHeight = 16.sp)
                    }
                }
                
                Spacer(Modifier.height(12.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {}, 
                        modifier = Modifier.weight(1f), 
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AuthBlue)
                    ) {
                        Text("ASSIGN", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = {}, 
                        modifier = Modifier.weight(1f), 
                        shape = RoundedCornerShape(8.dp),
                        border = borderStroke(1.dp, BorderGrey)
                    ) {
                        Text("VIEW DETAILS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AuthBlue)
                    }
                }
            }
        }
    }
}
