package soutvoid.com.DsrWeatherApp.ui.receivers

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import soutvoid.com.DsrWeatherApp.ui.util.ifNotNull

class NotificationPublisher: BroadcastReceiver() {

    companion object {
        const val NOTIFICATION = "notification"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        ifNotNull(context, intent) { ctx, intnt ->
            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = intnt.getParcelableExtra<Notification>(NOTIFICATION)
            notificationManager.notify(System.currentTimeMillis().hashCode(), notification)
        }
    }
}