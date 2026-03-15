package eu.ekansh.rakshakdtu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.math.ceil

class LogViewModel(private val repository: LogRepository) : ViewModel() {

    // ── All-logs state ─────────────────────────────────────────────
    var logs by mutableStateOf<List<Logs>>(emptyList()); private set
    var total by mutableStateOf(0); private set
    var currentPage by mutableStateOf(1); private set
    val limit = 20

    // ── Active-logs state ──────────────────────────────────────────
    var activeLogs by mutableStateOf<List<Logs>>(emptyList()); private set

    // ── UI state ───────────────────────────────────────────────────
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // ── Filters ────────────────────────────────────────────────────
    var selectedTab by mutableStateOf(0)        // 0=All  1=OnCampus  2=Unauthorized
    var searchQuery by mutableStateOf("")
    var vehicleFilter by mutableStateOf("all")  // "all" | "true" | "false"

    // ── Derived list the UI renders ────────────────────────────────
    val displayedLogs: List<Logs>
        get() {
            val base = when (selectedTab) {
                1    -> activeLogs
                2    -> logs.filter { !it.isAuthorized }
                else -> logs
            }
            return if (searchQuery.isBlank()) base
            else base.filter {
                it.vehicleNo.contains(searchQuery.trim(), ignoreCase = true) ||
                        it.camera.cameraLocation.contains(searchQuery.trim(), ignoreCase = true)
            }
        }

    val totalPages: Int get() = if (total > 0) ceil(total.toDouble() / limit).toInt() else 1

    // ── Auth token (set once from the screen / nav entry point) ───
    private var token: String = ""

    fun setToken(t: String) {
        token = t
    }

    // ── Public event handlers (called from UI) ─────────────────────

    fun onTabSelected(index: Int) {
        selectedTab = index
        currentPage = 1
        loadAll()
        if (index == 1) loadActive()
    }

    fun onSearchChanged(q: String) {
        searchQuery = q
    }

    fun onVehicleFilterChanged(f: String) {
        vehicleFilter = f
        currentPage = 1
        loadAll()
    }

    fun onPageChanged(page: Int) {
        currentPage = page
        loadAll()
    }

    fun refresh() {
        loadAll()
        loadActive()
    }

    // ── Private loaders ────────────────────────────────────────────

    private fun loadAll() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val authorizedFilter: Boolean? = when (vehicleFilter) {
                    "true"  -> true
                    "false" -> false
                    else    -> null
                }
                val response = repository.fetchLogs(token, currentPage, authorizedFilter, null)
                if (response.isSuccessful) {
                    val body = response.body()?.data
                    logs  = body?.logs  ?: emptyList()
                    total = body?.total ?: 0
                } else {
                    errorMessage = "Error ${response.code()}: ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Unknown error"
            } finally {
                isLoading = false
            }
        }
    }

    private fun loadActive() {
        viewModelScope.launch {
            try {
                val r = repository.fetchActiveLogs(token)
                if (r.isSuccessful) {
                    activeLogs = r.body()?.data?.logs ?: emptyList()
                }
            } catch (_: Exception) { }
        }
    }

    // ── Factory ────────────────────────────────────────────────────

    class Factory(private val repository: LogRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LogViewModel::class.java)) {
                return LogViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}