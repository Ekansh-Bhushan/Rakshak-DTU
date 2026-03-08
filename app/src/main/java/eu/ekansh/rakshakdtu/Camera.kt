package eu.ekansh.rakshakdtu

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CameraScreen(){

    val totalCameraCount = 12

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

            Button(onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.lightGreen)
                )
            ) {
                Icon(imageVector = Icons.Default.Upload, contentDescription = "Upload the CSV")
                Text(text = "Import Excel")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { /*TODO*/ },
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

        CameraTable()
    }
}

@Composable
fun CameraTable() {

    val Cameras = listOf(
        Camera("DL3CAF0001","Ravi Kumar","22.1223","77.23432","2W","STK-001"),
        Camera("HR26DK5678","Priya Singh","S/o Mohan Singh","MBA","4W","STK-002"),
        Camera("UP32EF1234","Amit Sharma","S/o Rajesh Sharma","CSE","2W","STK-003"),
        Camera("DL8CAB9900","Neha Gupta","S/o Vikas Gupta","ME","Electric","STK-004"),
        Camera("DL1PB3344","Suresh Yadav","S/o Ram Yadav","Civil","Heavy","STK-005"),
    )

    val horizontalScrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .horizontalScroll(horizontalScrollState)
        ) {

            CameraTableHeader()

            Divider(color = Color(0xFFE0E0E0))

            LazyColumn {

                items(Cameras) {

                    CameraRow(it)

                    Divider(color = Color(0xFFEAEAEA))
                }
            }
        }
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
fun CameraRow(camera: Camera) {

    Row(
        modifier = Modifier
            .width(950.dp)
            .padding(vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        CameraPlate(camera.Location)

        CameraChip(camera.Type, 1f)

        CameraColumn(camera.CoordinateLat, camera.CoordinateLong)

        CameraChip(camera.CameraID, 1f)

        CameraChip(camera.Resgistered, 1f)

        CameraActionButtons()
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
    CameraScreen()
}