package soutvoid.com.DsrWeatherApp.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import soutvoid.com.DsrWeatherApp.ui.util.NotificationUtils

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.let { NotificationUtils.scheduleAllNotifications(it) }
    }
}