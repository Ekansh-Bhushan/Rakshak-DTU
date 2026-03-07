package eu.ekansh.rakshakdtu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VehicleScreen(){

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "THis is vehicle Screen")
    }
}