package soutvoid.com.DsrWeatherApp.ui.util

import android.app.Notification
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger

object NotificationUtils {

    fun createTriggerNotification(context: Context, name: String, locationName: String): Notification {
        val builder = NotificationCompat.Builder(context)
                .setContentTitle(name)
                .setContentText(locationName)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon((ContextCompat.getDrawable(context, R.mipmap.ic_launcher) as BitmapDrawable).bitmap)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        return builder.build()
    }

}