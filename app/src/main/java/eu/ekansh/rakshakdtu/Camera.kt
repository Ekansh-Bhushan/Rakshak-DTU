package eu.ekansh.rakshakdtu

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import eu.ekansh.rakshakdtu.data.TokenManager

@Composable
fun CameraScreen(viewModel: CameraViewModel = viewModel(), navController: NavHostController){
    val cameras by viewModel.cameraList
    val errorMessage by viewModel.errorMessage
    val toastMessage by viewModel.toastMessage

    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    var storedToken by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.toastMessage.value = null
        }
    }

    LaunchedEffect(Unit){
        val token = tokenManager.getToken()
        if (token != null) {
            storedToken = token
            viewModel.getAllCameraDetails(token)
        } else {
            viewModel.errorMessage.value = "No session found. Please login."
            navController.navigate(Screen.LoginScreen.route)
        }
    }

    val totalCameraCount =  cameras?.size ?: 0
    var showRegisterForm by remember { mutableStateOf(false) }
    var showImportExcelForm by remember {
        mutableStateOf(false)
    }

    var cameraToEdit by remember { mutableStateOf<CameraData?>(null) }

    if (cameraToEdit != null) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { cameraToEdit = null }) {
            Card(shape = RoundedCornerShape(16.dp)) {
                EditCameraForm(
                    token = storedToken ?: "",
                    camera = cameraToEdit!!,
                    viewModel = viewModel,
                    onClose = { cameraToEdit = null }
                )
            }
        }
    }

    if (showRegisterForm) {

        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showRegisterForm = false }
        ) {

            Card(
                modifier = Modifier.width(420.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                RegisterCameraForm(token = storedToken ?: "",onClose = { showRegisterForm = false })
            }
        }
    }


    if (showImportExcelForm) {

        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showImportExcelForm = false }
        ) {

            Card(
                modifier = Modifier.width(420.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                ImportCameraDialog(onClose = { showImportExcelForm = false })
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .background(colorResource(id = R.color.background_color))
    ) {
        Text(text = "Campus Camera",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Manage CCTV Camera Points — $totalCameraCount Installed",
            fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Row {

            Button(onClick = {
                showImportExcelForm = true
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.lightGreen)
                )
            ) {
                Icon(imageVector = Icons.Default.Upload, contentDescription = "Upload the CSV")
                Text(text = "Import Excel")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                             showRegisterForm = true
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.lightGreen)
                )
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add the Camera")
                Text(text = "Add Camera")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        CameraTable(
            cameras = cameras ?: emptyList(),
            onDelete = { id ->
                storedToken?.let { viewModel.deleteACamera(it, id) }
            },
            onEdit = { camera ->
                cameraToEdit = camera
            }
        )
    }
}

@Composable
fun CameraTableHeader() {

    Row(
        modifier = Modifier
            .width(950.dp)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        CameraHeaderText("Location",1.2f)
        CameraHeaderText("Type",2f)
        CameraHeaderText("Coordinates",1f)
        CameraHeaderText("Camera ID",1f)
        CameraHeaderText("Resgistered",1f)
        CameraHeaderText("ACTIONS",1f)
    }
}

@Composable
fun RowScope.CameraHeaderText(text:String, weight:Float){
    Text(
        text,
        modifier = Modifier.weight(weight),
        color = Color.Gray,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp
    )
}

@Composable
fun CameraTable(
    cameras: List<CameraData>,
    onDelete: (String) -> Unit,
    onEdit: (CameraData) -> Unit
) {
    val horizontalScrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.horizontalScroll(horizontalScrollState)) {
            CameraTableHeader()
            HorizontalDivider(color = Color(0xFFE0E0E0))

            LazyColumn(modifier = Modifier.height(500.dp)) { // Give it a height or use weight
                items(cameras) { camera ->
                    CameraRow(
                        camera = camera,
                        onDelete = { onDelete(camera.id) },
                        onEdit = { onEdit(camera) }
                    )
                    HorizontalDivider(color = Color(0xFFEAEAEA))
                }
            }
        }
    }
}

@Composable
fun CameraRow(
    camera: CameraData,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier.width(950.dp).padding(vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Map API data to UI
        CameraPlate(camera.cameraLocation) // Using location as the primary ID/Plate
        CameraChip(camera.cameraType, 1f)
        CameraColumn(camera.lat.toString(), camera.long.toString())
        CameraChip(camera.id.takeLast(5), 1f) // Short ID
        CameraChip(camera.createdAt.split("T")[0], 1f) // Date only

        // Action Buttons
        Row(modifier = Modifier.weight(1f)) {
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}
@Composable
fun RowScope.CameraPlate(number:String){

    Box(
        modifier = Modifier
            .weight(1.2f)
    ){
        Box(
            modifier = Modifier
                .background(Color(0xFF1F2235), RoundedCornerShape(8.dp))
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ){
            Text(
                number,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun RowScope.CameraColumn(lat:String,long:String){

    Column(
        modifier = Modifier.weight(2f)
    ){

        Text(
            lat,
            color = Color.Gray,
            fontSize = 13.sp
        )

        Text(
            long,
            color = Color.Gray,
            fontSize = 13.sp
        )
    }
}

@Composable
fun RowScope.CameraChip(text:String, weight:Float){

    Box(
        modifier = Modifier.weight(weight)
    ){

        Box(
            modifier = Modifier
                .background(Color(0xFFE6EEF6), RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ){
            Text(
                text,
                color = Color(0xFF3A7BBF),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun RowScope.CameraActionButtons(){

    Row(
        modifier = Modifier.weight(1f)
    ){

        IconButton(onClick = {}) {
            Icon(Icons.Default.Edit, contentDescription = null)
        }

        IconButton(onClick = {}) {
            Icon(
                Icons.Default.Delete,
                contentDescription = null,
                tint = Color.Red
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CameraScreenPreview() {
//    CameraScreen()
}