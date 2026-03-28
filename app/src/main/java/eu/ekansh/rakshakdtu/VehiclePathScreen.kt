package eu.ekansh.rakshakdtu

import android.graphics.Color
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun VehiclePathScreen(
    token: String,
    entryId: String,
    vehicleNo: String,
    onBack: () -> Unit,
    viewModel: VehiclePathViewModel = viewModel()
) {
    val context = LocalContext.current

    Configuration.getInstance().apply {
        userAgentValue = context.packageName
        load(context, context.getSharedPreferences("osmdroid", 0))
    }

    LaunchedEffect(entryId, token) {
        if (token.isNotEmpty()) viewModel.loadPath(token, entryId)
    }

    // Use a Box as root so map never overlaps other layers
    Box(modifier = Modifier.fillMaxSize()) {

        when {
            viewModel.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            viewModel.path.isEmpty() && !viewModel.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        viewModel.errorMessage ?: "No path data available",
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                }
            }

            else -> {
                // Map fills full screen as background layer
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        MapView(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            setTileSource(TileSourceFactory.MAPNIK)
                            setMultiTouchControls(true)
                            controller.setZoom(17.0)
                        }
                    },
                    update = { mapView ->
                        mapView.overlays.clear()
                        val path = viewModel.path
                        if (path.isEmpty()) return@AndroidView

                        val geoPoints = path.map { GeoPoint(it.lat, it.lng) }

                        val polyline = Polyline().apply {
                            setPoints(geoPoints)
                            outlinePaint.color = android.graphics.Color.parseColor("#2196F3")
                            outlinePaint.strokeWidth = 8f
                        }
                        mapView.overlays.add(polyline)

                        path.forEach { point ->
                            val marker = Marker(mapView).apply {
                                position = GeoPoint(point.lat, point.lng)
                                title = point.cameraLocation ?: point.type
                                snippet = point.timestamp.take(19).replace("T", " ")
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            }
                            mapView.overlays.add(marker)
                        }

                        if (geoPoints.size == 1) {
                            mapView.controller.setCenter(geoPoints.first())
                        } else {
                            val box = BoundingBox.fromGeoPoints(geoPoints)
                            mapView.zoomToBoundingBox(box.increaseByScale(1.3f), true)
                        }

                        mapView.invalidate()
                    }
                )

                // Timeline sheet sits at the bottom — fixed height, never overlaps map controls
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .align(Alignment.BottomCenter),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(
                        topStart = 20.dp, topEnd = 20.dp,
                        bottomStart = 0.dp, bottomEnd = 0.dp
                    ),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = androidx.compose.ui.graphics.Color.White
                    )
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Drag handle
                                Box(
                                    modifier = Modifier
                                        .width(36.dp)
                                        .height(4.dp)
                                        .background(
                                            androidx.compose.ui.graphics.Color(0xFFE0E0E0),
                                            androidx.compose.foundation.shape.RoundedCornerShape(2.dp)
                                        )
                                )
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    "Camera Timeline",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Spacer(Modifier.weight(1f))
                                Text(
                                    "${viewModel.path.size} points",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = androidx.compose.ui.graphics.Color.Gray
                                )
                            }
                        }
                        itemsIndexed(viewModel.path) { index, point ->
                            PathTimelineItem(
                                point = point,
                                isLast = index == viewModel.path.size - 1
                            )
                        }
                    }
                }
            }
        }

        // Top bar always on top as an overlay — never part of scroll/weight layout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(
                    androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.85f)
                )
                .statusBarsPadding()  // ← handles the gap at the top
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Text(
                    "←",
                    color = androidx.compose.ui.graphics.Color.White,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Column {
                Text(
                    vehicleNo,
                    style = MaterialTheme.typography.titleMedium,
                    color = androidx.compose.ui.graphics.Color.White
                )
                Text(
                    "${viewModel.path.size} camera points",
                    style = MaterialTheme.typography.bodySmall,
                    color = androidx.compose.ui.graphics.Color.Gray
                )
            }
        }
    }
}

@Composable
fun PathTimelineItem(point: PathPoint, isLast: Boolean) {
    val dotColor = when (point.type) {
        "ENTRY"    -> androidx.compose.ui.graphics.Color(0xFF4CAF50)
        "EXIT"     -> androidx.compose.ui.graphics.Color(0xFFF44336)
        "SIGHTING" -> androidx.compose.ui.graphics.Color(0xFF2196F3)
        else       -> androidx.compose.ui.graphics.Color.Gray
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // Timeline dot + line
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(dotColor, shape = androidx.compose.foundation.shape.CircleShape)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(36.dp)
                        .background(androidx.compose.ui.graphics.Color.LightGray)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            Text(
                point.cameraLocation ?: point.type,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                point.timestamp.take(19).replace("T", " "),
                style = MaterialTheme.typography.bodySmall,
                color = androidx.compose.ui.graphics.Color.Gray
            )
            Text(
                point.type,
                style = MaterialTheme.typography.labelSmall,
                color = dotColor
            )
        }
    }
}