package eu.ekansh.rakshakdtu

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CameraViewModel (
    private val cameraRepository: cameraRepository = cameraRepository()
):ViewModel() {

    var cameraList = mutableStateOf<List<CameraData>?>(null)
    var errorMessage = mutableStateOf<String?>(null)
    var toastMessage = mutableStateOf<String?>(null)

    fun getAllCameraDetails(accessToken: String){
        viewModelScope.launch {
            try {
                val response = cameraRepository.getAllCamera(accessToken)
                if (response.isSuccessful) {
                    // response.body() is of type AllVehicleGetData
                    // We need to go: body -> data
                    val fetchedData = response.body()

                    if (fetchedData != null) {
                        this@CameraViewModel.cameraList.value = fetchedData.data
                        errorMessage.value = null
                    } else {
                        errorMessage.value = "Response body is empty"
                    }
                }
            } catch (e : Exception) {
                errorMessage.value = e.message ?: "An error occurred during fetching the camera details"
            }
        }
    }

    fun addACameraData(token : String,
                        lat : Double,
                       long : Double,
                       cameraType : String,
                       cameraLocation : String
    ){
        viewModelScope.launch {
            try {
                val response = cameraRepository.addACamera(token, lat, long, cameraType, cameraLocation)
                if(response.isSuccessful){
                    toastMessage.value = "Camera Entry Successfully"
                }

            } catch (e : Exception) {
                errorMessage.value = e.message ?: "An error occurred during adding the vehicle"
            }
        }
    }

    fun deleteACamera(token: String,id : String){
        viewModelScope.launch {
            try {
                val response = cameraRepository.deleteAVehicle(token,id)
                if(response.isSuccessful){
                    toastMessage.value = "Camera Delete Successfully"
                    getAllCameraDetails(token)
                }

            } catch (e : Exception) {
                errorMessage.value = e.message ?: "An error occurred during deletion of the Camera"
            }
        }
    }

    fun editCamera(token: String, vehicleNo: String, updateRequest: CameraUpdateRequest) {
        viewModelScope.launch {
            try {
                val response = cameraRepository.updateCamera(token, vehicleNo, updateRequest)
                if (response.isSuccessful) {
                    toastMessage.value = "Vehicle updated successfully"
                    // Refresh the list to see changes
                    getAllCameraDetails(token)
                } else {
                    errorMessage.value = "Update failed: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "An error occurred during update"
            }
        }
    }
}