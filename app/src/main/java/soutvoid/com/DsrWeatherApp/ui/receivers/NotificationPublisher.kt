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
import soutvoid.com.DsrWeatherApp.app.log.Logger
import soutvoid.com.DsrWeatherApp.ui.screen.main.MainActivityView
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.timeDialog.data.NotificationTime
import soutvoid.com.DsrWeatherApp.ui.util.ifNotNull

class NotificationPublisher : BroadcastReceiver() {

    companion object {
        const val LOCATION_NAME_KEY = "location"
        const val LOCATION_ID_KEY = "location_id"
        const val TRIGGER_NAME_KEY = "name"
        const val NOTIF_TIME_VALUE_KEY = "notif_time_value"
        const val NOTIF_TIME_UNIT_KEY = "notif_time_unit"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        ifNotNull(context, intent) { ctx, intent1 ->
            val extras = intent1.extras
            extras.keySet().forEach {
                val obj = extras.get(it)
                Logger.i(obj.toString())
            }
            if (intent1.hasExtra(LOCATION_NAME_KEY)
                    && intent1.hasExtra(TRIGGER_NAME_KEY)
                    && intent1.hasExtra(LOCATION_ID_KEY)
                    && intent1.hasExtra(NOTIF_TIME_UNIT_KEY)
                    && intent1.hasExtra(NOTIF_TIME_VALUE_KEY)) {
                val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notification = createTriggerNotification(
                        ctx,
                        intent1.getStringExtra(LOCATION_NAME_KEY),
                        intent1.getIntExtra(LOCATION_ID_KEY, 0),
                        intent1.getStringExtra(TRIGGER_NAME_KEY),
                        intent1.getIntExtra(NOTIF_TIME_UNIT_KEY, 0),
                        intent1.getIntExtra(NOTIF_TIME_VALUE_KEY, 1)
                        )
                notificationManager.notify(System.currentTimeMillis().hashCode(), notification)
            }
        }
    }

    fun createTriggerNotification(context: Context,
                                  locationName: String,
                                  locationId: Int,
                                  triggerName: String,
                                  notifTimeUnit: Int,
                                  notifTimeValue: Int): Notification {
        val notificationTime = NotificationTime(notifTimeValue, notifTimeUnit)
        val title = "$triggerName ${context.getString(R.string.`in`)} $locationName ${context.getString(R.string.in_time)} ${notificationTime.getNiceString(context)}"
        val contentText = context.getString(R.string.tap_to_see_weather)

        val intent = Intent(context, MainActivityView::class.java)
        intent.putExtra(MainActivityView.LOCATION_ID_KEY, locationId)
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