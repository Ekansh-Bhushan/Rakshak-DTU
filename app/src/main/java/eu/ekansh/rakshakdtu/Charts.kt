package eu.ekansh.rakshakdtu

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry

@Composable
fun VehicleEntriesChart() {

    val modelProducer = remember { ChartEntryModelProducer() }

    LaunchedEffect(Unit) {
        modelProducer.setEntries(
            listOf(
                FloatEntry(0f, 0f),
                FloatEntry(1f, 1f),
                FloatEntry(2f, 2f),
                FloatEntry(3f, 3f)
            )
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White,contentColor = Color.Black),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Vehicle Entries — Last 7 Days",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Chart(
                chart = lineChart(),
                chartModelProducer = modelProducer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }
    }
}
@Composable
fun AuthorizationDonutChart(
    authorized: Float,
    unauthorized: Float
) {

    val total = authorized + unauthorized
    val authorizedAngle = if (total == 0f) 0f else (authorized / total) * 360f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Authorization Status",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(contentAlignment = Alignment.Center) {

                Canvas(
                    modifier = Modifier.size(200.dp)
                ) {

                    drawArc(
                        color = Color(0xFFE74C3C),
                        startAngle = -90f,
                        sweepAngle = 360f - authorizedAngle,
                        useCenter = false,
                        style = Stroke(width = 40f)
                    )

                    drawArc(
                        color = Color(0xFF2ECC71),
                        startAngle = -90f,
                        sweepAngle = authorizedAngle,
                        useCenter = false,
                        style = Stroke(width = 40f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    "Unauthorized ${(unauthorized / total * 100).toInt()}%",
                    color = Color.Red
                )

                Text(
                    "Authorized ${(authorized / total * 100).toInt()}%",
                    color = Color(0xFF2ECC71)
                )
            }
        }
    }
}

