package soutvoid.com.DsrWeatherApp.ui.receivers

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.ui.screen.main.MainActivityView
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.timeDialog.data.NotificationTime
import soutvoid.com.DsrWeatherApp.ui.util.ifNotNull

class NotificationPublisher : BroadcastReceiver() {

    companion object {
        const val LOCATION_KEY = "location"
        const val TRIGGER_NAME_KEY = "name"
        const val NOTIF_TIME_KEY = "notif_time"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        ifNotNull(context, intent) { ctx, intent1 ->
            if (intent1.hasExtra(LOCATION_KEY) && intent1.hasExtra(TRIGGER_NAME_KEY) && intent1.hasExtra(NOTIF_TIME_KEY)) {
                val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notification = createTriggerNotification(
                        ctx,
                        intent1.getSerializableExtra(LOCATION_KEY) as SavedLocation,
                        intent1.getStringExtra(TRIGGER_NAME_KEY),
                        intent1.getSerializableExtra(NOTIF_TIME_KEY) as NotificationTime
                        )
                notificationManager.notify(System.currentTimeMillis().hashCode(), notification)
            }
        }
    }

    fun createTriggerNotification(context: Context,
                                  location: SavedLocation,
                                  name: String,
                                  notificationTime: NotificationTime): Notification {
        val title = "$name ${context.getString(R.string.`in`)} ${location.name} ${context.getString(R.string.in_time)} ${notificationTime.getNiceString(context)}"
        val contentText = context.getString(R.string.tap_to_see_weather)

        val intent = Intent(context, MainActivityView::class.java)
        intent.putExtra(MainActivityView.LOCATION_KEY, location)
        val pendingIntent = PendingIntent.getActivity(
                context, System.currentTimeMillis().hashCode(), intent, 0)

        val builder = NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_cloud_queue_white)
                .setLargeIcon((ContextCompat.getDrawable(context, R.mipmap.ic_launcher) as BitmapDrawable).bitmap)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
        return builder.build()
    }
}