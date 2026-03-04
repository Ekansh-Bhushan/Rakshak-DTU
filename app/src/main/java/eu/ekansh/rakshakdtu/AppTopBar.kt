package eu.ekansh.rakshakdtu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppBarView(
    title : String,
    onBackNavClick : () -> Unit = {}
){
    val navigationIcon : (@Composable () -> Unit) ?=
        if (!title.contains("Wishlist")) {
            {
                IconButton(onClick = { onBackNavClick() }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            }
        } else {
            null
        }
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(id = R.drawable.logo_dtu),
                    contentDescription = "DTU logo",
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = title,
                    color = colorResource(id = R.color.white),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        modifier = Modifier
            .heightIn(max = 54.dp),
        elevation = 3.dp,
        backgroundColor = colorResource(id = R.color.app_bar_color),
        navigationIcon = navigationIcon
    )
}

@Preview(showBackground = true)
@Composable
fun AppBarViewPreview() {
//    AppBarView("DTU Rakshak",{})
}