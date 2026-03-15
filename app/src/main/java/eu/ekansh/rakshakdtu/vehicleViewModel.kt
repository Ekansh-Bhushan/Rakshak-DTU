package eu.ekansh.rakshakdtu

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class VehicleViewModel (
    private val vehicleRepository: vehicleRepository = vehicleRepository()
):ViewModel() {

    var campusLogs = mutableStateOf<DataForVehicleLogsInCampus?>(null)
    var vehicleList = mutableStateOf<List<VehicleData>?>(null)
    var errorMessage = mutableStateOf<String?>(null)
    var toastMessage = mutableStateOf<String?>(null)

    private var searchJob: kotlinx.coroutines.Job? = null

    fun onSearchQueryChanged(token: String, query: String) {
        searchJob?.cancel() // Cancel the previous search if user is still typing
        searchJob = viewModelScope.launch {
            if (query.isNotEmpty()) {
                kotlinx.coroutines.delay(500) // Wait for 500ms
            }

            try {
                // If query is empty, it sends null which fetches all vehicles
                val response = vehicleRepository.getAllVehicles(token, query.ifEmpty { null })
                if (response.isSuccessful) {
                    vehicleList.value = response.body()?.data?.vehicles
                }
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }

    fun getAllVehiclesDetails(accessToken: String){
        viewModelScope.launch {
            try {
                val response = vehicleRepository.getAllVehicles(accessToken)
                if (response.isSuccessful) {
                    // response.body() is of type AllVehicleGetData
                    // We need to go: body -> data -> vehicles
                    val fetchedData = response.body()

                    if (fetchedData != null) {
                        this@VehicleViewModel.vehicleList.value = fetchedData.data.vehicles
                        errorMessage.value = null
                    } else {
                        errorMessage.value = "Response body is empty"
                    }
                }
            } catch (e : Exception) {
                errorMessage.value = e.message ?: "An error occurred during fetching the vehicle details"
            }
        }
    }

    fun addAVehicleData(token : String,
                        name : String,
                        fathersName : String,
                        dept : String,
                        dateOfIssue : String,
                        vehicleType : String,
                        stickerNo : String,
                        vehicleNo : String,
                        mobileNo : String
    ){
        viewModelScope.launch {
            try {
                val response = vehicleRepository.addAVehicle(token, name, fathersName, dept, dateOfIssue, vehicleType, stickerNo, vehicleNo, mobileNo)
                if(response.isSuccessful){
                    toastMessage.value = "Vehicle Entry Successfully"
                }

            } catch (e : Exception) {
                errorMessage.value = e.message ?: "An error occurred during adding the vehicle"
            }
        }
    }

    fun deleteAVehicle(token: String,numberPate : String){
        viewModelScope.launch {
            try {
                val response = vehicleRepository.deleteAVehicle(token,numberPate)
                if(response.isSuccessful){
                    toastMessage.value = "Vehicle Delete Successfully"
                }

            } catch (e : Exception) {
                errorMessage.value = e.message ?: "An error occurred during deletion of the vehicle"
            }
        }
    }

    fun editVehicle(token: String, vehicleNo: String, updateRequest: VehicleUpdateRequest) {
        viewModelScope.launch {
            try {
                val response = vehicleRepository.updateVehicle(token, vehicleNo, updateRequest)
                if (response.isSuccessful) {
                    toastMessage.value = "Vehicle updated successfully"
                    // Refresh the list to see changes
                    getAllVehiclesDetails(token)
                } else {
                    errorMessage.value = "Update failed: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "An error occurred during update"
            }
        }
    }
    fun getCampusVehicleDetails(token:String) {
        viewModelScope.launch {
            try {
                val response = vehicleRepository.getVehicleOnCampus(token)
                if (response.isSuccessful) {
                    campusLogs.value = response.body()?.data
                } else {
                    errorMessage.value = "Failed to fetch campus logs: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "An error occurred during update"
            }
        }
    }
}