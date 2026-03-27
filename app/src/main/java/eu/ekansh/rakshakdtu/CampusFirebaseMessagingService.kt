package eu.ekansh.rakshakdtu

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import eu.ekansh.rakshakdtu.data.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CampusFirebaseMessagingService : FirebaseMessagingService() {

    // Called when FCM assigns a new token to this device
    // (on first install, or when token is rotated)
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        sendTokenToBackend(token)
    }

    // Called when a push notification arrives
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        android.util.Log.d("FCM_RECEIVE", "🔥 Message received! Data: ${message.data}")

        // Pull title and body from the DATA map, not the notification object
        val title     = message.data["title"] ?: "Campus Alert"
        val body      = message.data["message"] ?: ""
        val alertType = message.data["alertType"] ?: ""
        val alertId   = message.data["alertId"] ?: ""
        val rawPlate  = message.data["rawPlate"] ?: ""
        val logId     = message.data["logId"] ?: ""

        showNotification(title, body, alertType, alertId, rawPlate, logId)
    }
    private fun showNotification(
        title: String,
        body: String,
        alertType: String,
        alertId: String,
        rawPlate: String,
        logId: String
    ) {
        val channelId = "alerts"

        // Tapping the notification opens MainActivity and passes alert data
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("alertId",   alertId)
            putExtra("alertType", alertType)
            putExtra("rawPlate",  rawPlate)
            putExtra("logId",     logId)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, alertId.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val color = when (alertType) {
            "OVERSTAY",
            "ACTIVE_OVERSTAY_ALARM",
            "EXIT_WITHOUT_ENTRY"        -> Color.RED
            "CONCURRENT_ENTRY_OVERWRITE" -> Color.BLUE
            "ORPHAN_SIGHTING"           -> Color.YELLOW
            else                        -> Color.RED
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // replace with your alert icon
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setColor(color)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(alertId.hashCode(), notification)
    }

    private fun sendTokenToBackend(fcmToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val tokenManager = TokenManager(applicationContext)
                val accessToken  = tokenManager.getToken() ?: return@launch

                AllAPIClient.deviceApiService.registerDeviceToken(
                    accessToken = "Bearer $accessToken",
                    request     = DeviceTokenRequest(fcmToken)
                )
            } catch (e: Exception) {
                // Silent fail — will retry on next token refresh
            }
        }
    }
}