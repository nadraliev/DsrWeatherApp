package soutvoid.com.DsrWeatherApp.ui.util

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.media.RingtoneManager
import android.provider.Settings
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import io.realm.Realm
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.domain.sys.Sys
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.ui.receivers.NotificationPublisher
import soutvoid.com.DsrWeatherApp.ui.receivers.RequestCode
import soutvoid.com.DsrWeatherApp.ui.receivers.TriggerReEnabler
import soutvoid.com.DsrWeatherApp.ui.screen.main.MainActivityView
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.timeDialog.data.NotificationTime

object NotificationUtils {

    fun createTriggerNotification(context: Context,
                                  location: SavedLocation,
                                  name: String,
                                  time: Long): Notification {
        val notifTimeBefore = NotificationTime(time)
        val title = "$name ${context.getString(R.string.`in`)} ${location.name} ${context.getString(R.string.in_time)} ${notifTimeBefore.getNiceString(context)}"
        val contentText = context.getString(R.string.tap_to_see_weather)

        val intent = Intent(context, MainActivityView::class.java)
        intent.putExtra(MainActivityView.LOCATION_KEY, location)
        val pendingIntent = PendingIntent.getActivity(
                context, System.currentTimeMillis().hashCode(), intent, 0)

        val builder = NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(contentText)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon((ContextCompat.getDrawable(context, R.mipmap.ic_launcher) as BitmapDrawable).bitmap)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
        return builder.build()
    }

    fun scheduleNotifications(context: Context, triggers: List<SavedTrigger>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        triggers.forEach { trigger ->
            val notificationTimes = getNotificationTimesMillis(trigger)
            notificationTimes.forEach { time ->
                val notification = NotificationUtils.createTriggerNotification(
                        context,
                        trigger.location,
                        trigger.name,
                        time)
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        time,
                        createPendingIntent(context, notification))
            }
            scheduleReEnableTrigger(
                    context,
                    trigger,
                    notificationTimes.max().ifNotNullOr(0),
                    alarmManager)
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

    /**
     * по завершению работы триггера, выключим и включим его, чтобы данные обновились
     */
    fun scheduleReEnableTrigger(context: Context,
                                trigger: SavedTrigger,
                                lastNotifTime: Long,
                                alarmManager: AlarmManager) {
        var reEnableTime = 0L
        if (lastNotifTime == 0L)
            reEnableTime = System.currentTimeMillis() + 43200000
        else
            reEnableTime = lastNotifTime + 3600000
        val intent = Intent(context, TriggerReEnabler::class.java)
        intent.putExtra(TriggerReEnabler.ID_KEY, trigger.id)
        intent.putExtra(TriggerReEnabler.TRIGGER_ID_KEY, trigger.triggerId)
        val pendingintent = PendingIntent.getBroadcast(context, getNewRequestCode(), intent, 0)
        alarmManager.set(AlarmManager.RTC, reEnableTime, pendingintent)
    }

}