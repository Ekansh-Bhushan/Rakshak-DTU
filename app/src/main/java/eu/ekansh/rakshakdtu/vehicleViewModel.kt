package eu.ekansh.rakshakdtu

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class VehicleViewModel (
    private val vehicleRepository: vehicleRepository = vehicleRepository()
):ViewModel() {

    var vehicleList = mutableStateOf<List<VehicleData>?>(null)
    var errorMessage = mutableStateOf<String?>(null)
    var toastMessage = mutableStateOf<String?>(null)

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
}