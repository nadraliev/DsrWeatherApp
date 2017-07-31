package soutvoid.com.DsrWeatherApp.ui.util

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import io.realm.Realm
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.ui.receivers.NotificationPublisher
import soutvoid.com.DsrWeatherApp.ui.receivers.RequestCode

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

    fun scheduleNotifications(context: Context, triggers: List<SavedTrigger>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        triggers.forEach { trigger ->
            getNotificationTimesMillis(trigger).forEach { time ->
                val notification = NotificationUtils.createTriggerNotification(
                        context,
                        trigger.name,
                        trigger.location.name)
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        time,
                        createPendingIntent(context, notification))
            }
        }
    }

    fun scheduleAllNotifications(context: Context) {
        val triggers = getSavedTriggers().filter { it.enabled }
        scheduleNotifications(context, triggers)
    }

    private fun createPendingIntent(context: Context, notification: Notification): PendingIntent {
        val intent = Intent(context, NotificationPublisher::class.java)
        intent.putExtra(NotificationPublisher.NOTIFICATION, notification)
        return PendingIntent.getBroadcast(context, getNewRequestCode(), intent, 0)
    }

    private fun getNotificationTimesMillis(trigger: SavedTrigger): List<Long> {
        val result = mutableListOf<Long>()
        trigger.alerts.forEach { alert ->
            trigger.notificationTimes.forEach { time ->
                result.add(alert.value - time.getMilliseconds())
            }
        }
        return result.toList().filter { it > System.currentTimeMillis() }
    }

    fun cancelAllNotifications(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        getAllRequestCodes().forEach {
            alarmManager.cancel(
                    PendingIntent.getBroadcast(context, it, Intent(context, NotificationPublisher::class.java), 0)
            )
        }
    }

}