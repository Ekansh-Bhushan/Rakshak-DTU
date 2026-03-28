package eu.ekansh.rakshakdtu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class VehiclePathViewModel(
    private val repository: LogRepository = LogRepository()
) : ViewModel() {

    var path by mutableStateOf<List<PathPoint>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadPath(token: String, entryId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = repository.fetchEntryPath(token, entryId)
                if (response.isSuccessful) {
                    path = response.body()?.data?.path ?: emptyList()
                } else {
                    errorMessage = "Failed to load path: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            } finally {
                isLoading = false
            }
        }
    }
}