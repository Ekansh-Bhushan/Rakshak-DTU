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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VehicleScreen(){

    val totalVehicleCount = 12

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .background(colorResource(id = R.color.background_color))
    ) {
        Text(text = "Registered Vehicles",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Manage campus vehicle authorizations — $totalVehicleCount total registered",
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
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add the vehicle")
                Text(text = "Add Vehicle")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        SearchBar()
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "$totalVehicleCount Records")

        Spacer(modifier = Modifier.height(8.dp))

        vehicleTable()
    }
}

@Composable
fun SearchBar() {

    var search by remember { mutableStateOf("") }

    OutlinedTextField(
        value = search,
        onValueChange = { search = it },
        placeholder = {
            Text(
                "Search by plate, name, dept...",
                color = Color(0xFF9E9E9E)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF9E9E9E)
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(28.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF5F5F5),
            unfocusedContainerColor = Color(0xFFF5F5F5),

            focusedBorderColor = Color(0xFFE0E0E0),
            unfocusedBorderColor = Color(0xFFE0E0E0),

            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun vehicleTable() {

    val vehicles = listOf(
        Vehicle("DL3CAF0001","Ravi Kumar","S/o Suresh Kumar","ECE","2W","STK-001","9876543210"),
        Vehicle("HR26DK5678","Priya Singh","S/o Mohan Singh","MBA","4W","STK-002","9123456780"),
        Vehicle("UP32EF1234","Amit Sharma","S/o Rajesh Sharma","CSE","2W","STK-003","9988776655"),
        Vehicle("DL8CAB9900","Neha Gupta","S/o Vikas Gupta","ME","Electric","STK-004","9871234560"),
        Vehicle("DL1PB3344","Suresh Yadav","S/o Ram Yadav","Civil","Heavy","STK-005","9765432100"),
    )

    val horizontalScrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .horizontalScroll(horizontalScrollState)
        ) {

            TableHeader()

            Divider(color = Color(0xFFE0E0E0))

            LazyColumn {

                items(vehicles) {

                    VehicleRow(it)

                    Divider(color = Color(0xFFEAEAEA))
                }
            }
        }
    }
}
@Composable
fun TableHeader() {

    Row(
        modifier = Modifier
            .width(950.dp)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        HeaderText("VEHICLE NO.",1.2f)
        HeaderText("OWNER",2f)
        HeaderText("DEPT",1f)
        HeaderText("TYPE",1f)
        HeaderText("STICKER",1f)
        HeaderText("MOBILE",1.3f)
        HeaderText("ACTIONS",1f)
    }
}

@Composable
fun RowScope.HeaderText(text:String, weight:Float){
    Text(
        text,
        modifier = Modifier.weight(weight),
        color = Color.Gray,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp
    )
}

@Composable
fun VehicleRow(vehicle: Vehicle) {

    Row(
        modifier = Modifier
            .width(950.dp)
            .padding(vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        VehiclePlate(vehicle.vehicleNo)

        OwnerColumn(vehicle.owner, vehicle.father)

        Chip(vehicle.dept, 1f)

        Chip(vehicle.type, 1f)

        Text(
            vehicle.sticker,
            modifier = Modifier.weight(1f)
        )

        Text(
            vehicle.mobile,
            modifier = Modifier.weight(1.3f)
        )

        ActionButtons()
    }
}

@Composable
fun RowScope.VehiclePlate(number:String){

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
fun RowScope.OwnerColumn(owner:String,father:String){

    Column(
        modifier = Modifier.weight(2f)
    ){

        Text(
            owner,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        Text(
            father,
            color = Color.Gray,
            fontSize = 13.sp
        )
    }
}

@Composable
fun RowScope.Chip(text:String, weight:Float){

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
fun RowScope.ActionButtons(){

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
fun VehicleScreenPreview() {
     VehicleScreen()
}