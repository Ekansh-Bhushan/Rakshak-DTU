package eu.ekansh.rakshakdtu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Initialize osmdroid configuration
    val sharedPrefs = remember { context.getSharedPreferences("osmdroid", 0) }
    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
        Configuration.getInstance().load(context, sharedPrefs)
    }

    val mapView = remember { MapView(context) }

    DisposableEffect(mapView) {
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onDetach()
        }
    }

    // Initialize Map and Markers
    LaunchedEffect(Unit) {
        mapView.apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(17.5)
            controller.setCenter(GeoPoint(28.7501, 77.1177))

            // Add dummy markers to match reference image
            val markers = listOf(
                GeoPoint(28.7505, 77.1165) to "Vehicle 1",
                GeoPoint(28.7498, 77.1182) to "Department",
                GeoPoint(28.7485, 77.1175) to "Security Post"
            )

            markers.forEach { (point, title) ->
                val marker = Marker(this)
                marker.position = point
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.title = title
                overlays.add(marker)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // ── Map View ──────────────────────────────────────────────────────────
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

        // ── Search Bar ────────────────────────────────────────────────────────
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .statusBarsPadding(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search vehicle ID, plate, or building", color = Color.Gray, fontSize = 14.sp) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
                IconButton(onClick = { /* Filter action */ }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = Color(0xFF0D47A1))
                }
            }
        }

        // ── Map Controls (Zoom, My Location, Layers) ──────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FloatingActionButton(
                onClick = { mapView.controller.zoomIn() },
                containerColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Zoom In")
            }

            FloatingActionButton(
                onClick = { mapView.controller.zoomOut() },
                containerColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
            }

            FloatingActionButton(
                onClick = { /* My Location action */ },
                containerColor = Color(0xFF0D47A1),
                contentColor = Color.White,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "My Location")
            }

            FloatingActionButton(
                onClick = { /* Layer action */ },
                containerColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Layers, contentDescription = "Layers")
            }
        }

        // ── Floating Badge Example ────────────────────────────────────────────
        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 100.dp, x = 60.dp),
            color = Color(0xFFC62828),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                "UNAUTHORIZED ENTRY",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}
