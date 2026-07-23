package eu.ekansh.rakshakdtu

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import eu.ekansh.rakshakdtu.ui.theme.RakshakDTUTheme
import android.Manifest
import android.util.Log

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("RAKSHAK_ERROR", "MainActivity onCreate")
        
        try {
            com.google.firebase.FirebaseApp.initializeApp(this)
            ApiClient.init(applicationContext)
            AllAPIClient.init(applicationContext)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        101
                    )
                }
            }

            createNotificationChannel()

            // FLAG_SECURE REMOVED FOR DEBUGGING
            // window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

            WindowCompat.setDecorFitsSystemWindows(window, true)
            window.statusBarColor = android.graphics.Color.BLACK

            val controller = WindowCompat.getInsetsController(window, window.decorView)
            controller.isAppearanceLightStatusBars = false

            setContent {
                RakshakDTUTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = androidx.compose.ui.graphics.Color.White
                    ) {
                        Navigation()
                    }
                }
            }
            Log.e("RAKSHAK_ERROR", "MainActivity setContent done")
        } catch (e: Exception) {
            Log.e("RAKSHAK_ERROR", "MainActivity Error", e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            "alerts",
            "Campus Alerts",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Real-time campus security alerts"
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
        }
        manager.createNotificationChannel(channel)
    }
}