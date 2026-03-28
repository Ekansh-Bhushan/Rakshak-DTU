package eu.ekansh.rakshakdtu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import eu.ekansh.rakshakdtu.data.TokenManager


// ─────────────────────────────────────────────────────────────────────────────
//  LOG SCREEN
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun LogScreen(navController: NavHostController) {
    val context = LocalContext.current

    // ✅ FIX: pass Factory so Compose can construct LogViewModel(repository).
    //    Without this, viewModel() throws IllegalArgumentException because
    //    LogViewModel has no zero-arg constructor.
    val viewModel: LogViewModel = viewModel(
        factory = LogViewModel.Factory(LogRepository())
    )

    val tokenManager = remember { TokenManager(context) }

    // Read token once, hand it to VM, then load data
    LaunchedEffect(Unit) {
        val storedToken = tokenManager.getToken()
        if (storedToken != null) {
            viewModel.setToken(storedToken)
            viewModel.refresh()
        } else {
            viewModel.errorMessage = "No session found. Please login."
        }
    }

    val tabs = listOf(
        "All Logs (${viewModel.total})",
        "On Campus (${viewModel.activeLogs.size})",
        "Unauthorized"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FA))
            .padding(16.dp)
    ) {
        Text("Entry / Exit Logs", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color(0xFF1A1C1E))
        Text("All vehicle movement records on DTU campus", fontSize = 14.sp, color = Color(0xFF6B7280))

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .background(Color(0xFFDCFCE7), RoundedCornerShape(6.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                "${viewModel.activeLogs.size} Inside Campus",
                color = Color(0xFF15803D), fontSize = 12.sp, fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(20.dp))

        LogTabRow(tabs = tabs, selectedTab = viewModel.selectedTab, onTabSelected = viewModel::onTabSelected)

        Spacer(Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {

                LogToolbar(
                    query = viewModel.searchQuery,
                    onQueryChange = viewModel::onSearchChanged,
                    vehicleFilter = viewModel.vehicleFilter,
                    onVehicleFilterChanged = viewModel::onVehicleFilterChanged,
                    showFilter = viewModel.selectedTab == 0
                )

                Spacer(Modifier.height(12.dp))

                when {
                    viewModel.isLoading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF16A34A))
                        }
                    }

                    viewModel.errorMessage != null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("⚠️ ${viewModel.errorMessage}", color = Color(0xFFDC2626), textAlign = TextAlign.Center)
                                Spacer(Modifier.height(12.dp))
                                Button(
                                    onClick = { viewModel.refresh() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
                                ) { Text("Retry") }
                            }
                        }
                    }

                    else -> {
                        LogTable(
                            logs = viewModel.displayedLogs,
                            modifier = Modifier.weight(1f),
                            onLogClick = { log ->
                                navController.navigate(
                                    Screen.VehiclePathScreen.route + "/${log.id}/${log.vehicleNo}"
                                )
                            }
                        )

                        if (viewModel.selectedTab == 0 && viewModel.totalPages > 1) {
                            Spacer(Modifier.height(12.dp))
                            PaginationRow(
                                currentPage = viewModel.currentPage,
                                totalPages = viewModel.totalPages,
                                onPageChange = viewModel::onPageChanged
                            )
                        }
                    }
                }
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  TAB ROW
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun LogTabRow(tabs: List<String>, selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        tabs.forEachIndexed { index, title ->
            Column(
                modifier = Modifier.padding(end = 20.dp).clickable { onTabSelected(index) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    color = if (selectedTab == index) Color(0xFF16A34A) else Color(0xFF9CA3AF),
                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .width(if (selectedTab == index) 44.dp else 0.dp)
                        .height(2.dp)
                        .background(Color(0xFF16A34A), RoundedCornerShape(1.dp))
                )
            }
        }
    }
    HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE5E7EB))
}


// ─────────────────────────────────────────────────────────────────────────────
//  TOOLBAR
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun LogToolbar(
    query: String,
    onQueryChange: (String) -> Unit,
    vehicleFilter: String,
    onVehicleFilterChanged: (String) -> Unit,
    showFilter: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search plate, location…", color = Color(0xFFADB5BD), fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = Color(0xFFADB5BD), modifier = Modifier.size(18.dp)) },
            trailingIcon = {
                AnimatedVisibility(visible = query.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
                    IconButton(onClick = { onQueryChange("") }, modifier = Modifier.size(18.dp)) {
                        Icon(Icons.Default.Close, null, tint = Color(0xFFADB5BD))
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF9FAFB),
                unfocusedContainerColor = Color(0xFFF9FAFB),
                focusedBorderColor = Color(0xFF16A34A),
                unfocusedBorderColor = Color(0xFFE5E7EB),
            ),
            modifier = Modifier.weight(1f).height(50.dp)
        )

        if (showFilter) {
            VehicleFilterDropdown(selected = vehicleFilter, onSelected = onVehicleFilterChanged)
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  VEHICLE FILTER DROPDOWN
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun VehicleFilterDropdown(selected: String, onSelected: (String) -> Unit) {
    val options = listOf("all" to "All Vehicles", "true" to "Authorized", "false" to "Unauthorized")
    var expanded by remember { mutableStateOf(false) }
    val label = options.first { it.first == selected }.second

    Box {
        OutlinedTextField(
            value = label,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(
                    if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    null, tint = Color(0xFF6B7280)
                )
            },
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF9FAFB),
                unfocusedContainerColor = Color(0xFFF9FAFB),
                focusedBorderColor = Color(0xFF16A34A),
                unfocusedBorderColor = Color(0xFFE5E7EB),
            ),
            modifier = Modifier.width(160.dp).height(50.dp).clickable { expanded = !expanded }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            options.forEach { (key, display) ->
                DropdownMenuItem(
                    text = {
                        Text(
                            display,
                            fontWeight = if (selected == key) FontWeight.Bold else FontWeight.Normal,
                            color = if (selected == key) Color(0xFF16A34A) else Color(0xFF1A1C1E)
                        )
                    },
                    onClick = { onSelected(key); expanded = false }
                )
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  LOG TABLE
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun LogTable(logs: List<Logs>, modifier: Modifier = Modifier, onLogClick: (Logs) -> Unit = {}) {
    Column(modifier = modifier) {
        Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            Column(modifier = Modifier.width(860.dp)) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF9FAFB), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HeaderCell("VEHICLE NO.",   1.4f)
                    HeaderCell("CAMERA / GATE", 2f)
                    HeaderCell("ENTRY TIME",    1.6f)
                    HeaderCell("EXIT TIME",     1.6f)
                    HeaderCell("DURATION",      1.2f)
                    HeaderCell("STATUS",        1f)
                    HeaderCell("EVENT",         0.8f)
                }

                HorizontalDivider(color = Color(0xFFE5E7EB))

                if (logs.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No logs found", color = Color(0xFF9CA3AF), fontSize = 14.sp)
                    }
                } else {
                    LazyColumn {
                        items(logs, key = { it.id }) { log ->
                            LogRow(log = log, onClick = { onLogClick(log) })
                            HorizontalDivider(color = Color(0xFFF3F4F6))
                        }
                    }
                }
            }
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  LOG ROW
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun LogRow(log: Logs,onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.width(860.dp).padding(horizontal = 12.dp, vertical = 12.dp).clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1.4f)) {
            Text(
                text = log.vehicleNo,
                modifier = Modifier
                    .background(Color(0xFF1A1C1E), RoundedCornerShape(5.dp))
                    .padding(horizontal = 7.dp, vertical = 4.dp),
                color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 0.5.sp
            )
        }

        Column(modifier = Modifier.weight(2f)) {
            Text(log.camera.cameraLocation, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color(0xFF1A1C1E))
            Text(log.camera.cameraType, color = Color(0xFF9CA3AF), fontSize = 11.sp)
        }

        Text(formatDateTime(log.entryTime), modifier = Modifier.weight(1.6f), fontSize = 12.sp, color = Color(0xFF374151), lineHeight = 18.sp)

        Text(
            text = if (log.exitTime != null) formatDateTime(log.exitTime) else "Still Inside",
            modifier = Modifier.weight(1.6f),
            fontSize = 12.sp,
            color = if (log.exitTime != null) Color(0xFF374151) else Color(0xFF9CA3AF),
            lineHeight = 18.sp
        )

        Text(formatDuration(log.vehicleDuration), modifier = Modifier.weight(1.2f), fontSize = 12.sp, color = Color(0xFF374151))

        Box(modifier = Modifier.weight(1f)) {
            val (bg, fg, label) = if (log.isAuthorized)
                Triple(Color(0xFFDCFCE7), Color(0xFF15803D), "✓ Auth")
            else
                Triple(Color(0xFFFFEBEE), Color(0xFFDC2626), "✗ Unauth")
            Text(
                label,
                modifier = Modifier.background(bg, RoundedCornerShape(12.dp)).padding(horizontal = 8.dp, vertical = 4.dp),
                color = fg, fontSize = 11.sp, fontWeight = FontWeight.SemiBold
            )
        }

        Box(modifier = Modifier.weight(0.8f)) {
            val (bg, fg, label) = if (log.exitTime != null)
                Triple(Color(0xFFF3F4F6), Color(0xFF6B7280), "Exited")
            else
                Triple(Color(0xFFFFF8E1), Color(0xFFD97706), "Entry")
            Text(
                label,
                modifier = Modifier.background(bg, RoundedCornerShape(5.dp)).padding(horizontal = 8.dp, vertical = 4.dp),
                color = fg, fontSize = 11.sp
            )
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  HEADER CELL
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun RowScope.HeaderCell(text: String, weight: Float) {
    Text(
        text = text,
        modifier = Modifier.weight(weight),
        fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B7280), letterSpacing = 0.5.sp
    )
}


// ─────────────────────────────────────────────────────────────────────────────
//  PAGINATION
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PaginationRow(currentPage: Int, totalPages: Int, onPageChange: (Int) -> Unit) {
    val visiblePages = (1..totalPages)
        .filter { it == 1 || it == totalPages || it in (currentPage - 2)..(currentPage + 2) }
        .distinct().sorted()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = { if (currentPage > 1) onPageChange(currentPage - 1) }, enabled = currentPage > 1) {
            Text("← Prev", fontSize = 13.sp, color = if (currentPage > 1) Color(0xFF16A34A) else Color(0xFFD1D5DB))
        }

        var prev = 0
        visiblePages.forEach { page ->
            if (prev != 0 && page - prev > 1) {
                Text("…", modifier = Modifier.padding(horizontal = 4.dp), color = Color(0xFF9CA3AF))
            }
            val isCurrent = page == currentPage
            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp).size(32.dp)
                    .background(if (isCurrent) Color(0xFF16A34A) else Color.Transparent, RoundedCornerShape(6.dp))
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onPageChange(page) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "$page", fontSize = 13.sp,
                    color = if (isCurrent) Color.White else Color(0xFF374151),
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                )
            }
            prev = page
        }

        TextButton(onClick = { if (currentPage < totalPages) onPageChange(currentPage + 1) }, enabled = currentPage < totalPages) {
            Text("Next →", fontSize = 13.sp, color = if (currentPage < totalPages) Color(0xFF16A34A) else Color(0xFFD1D5DB))
        }
    }
}


// ─────────────────────────────────────────────────────────────────────────────
//  HELPERS
// ─────────────────────────────────────────────────────────────────────────────

private fun formatDateTime(iso: String): String {
    return try {
        val instant = java.time.Instant.parse(iso)
        val zdt = instant.atZone(java.time.ZoneId.of("Asia/Kolkata"))
        val date = "${zdt.dayOfMonth}/${zdt.monthValue}/${zdt.year}"
        val hour12 = if (zdt.hour % 12 == 0) 12 else zdt.hour % 12
        val ampm = if (zdt.hour < 12) "am" else "pm"
        "$date\n$hour12:${zdt.minute.toString().padStart(2, '0')} $ampm"
    } catch (_: Exception) { iso }
}

private fun formatDuration(raw: String?): String {
    if (raw == null) return "—"
    val secs = raw.toLongOrNull() ?: return raw
    val h = secs / 3600; val m = (secs % 3600) / 60; val s = secs % 60
    return when { h > 0 -> "${h}h ${m}m"; m > 0 -> "${m}m ${s}s"; else -> "${s}s" }
}