package soutvoid.com.DsrWeatherApp.ui.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import soutvoid.com.DsrWeatherApp.app.log.Logger
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.ui.receivers.NotificationPublisher
import soutvoid.com.DsrWeatherApp.ui.receivers.TriggerReEnabler
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.timeDialog.data.NotificationTime

object NotificationUtils {

    /**
     * запланировать уведомления для списка триггеров по всем их алертам и желаемым временам нотификации
     * также запланировать обновление алармов триггеров (через включение/выключение)
     */
    fun scheduleNotifications(context: Context, triggers: List<SavedTrigger>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        triggers.forEach { trigger ->
            trigger.alerts.distinctBy { it.id }.forEach { alert ->
                trigger.notificationTimes.distinctBy { it.id }.forEach { notifTime ->
                    val timeMillis = getNotificationTimeMillis(alert.value, notifTime)
                    if (timeMillis > System.currentTimeMillis())
                        alarmManager.set(AlarmManager.RTC_WAKEUP,
                                timeMillis,
                                createPendingIntent(context, trigger, notifTime))
                    Logger.i("notification scheduled $timeMillis")
                }
            }
            scheduleReEnableTrigger(
                    context,
                    trigger,
                    getNotificationTimesMillis(trigger).max().ifNotNullOr(0),
                    alarmManager)
        }
    }

    /**
     * запланировать уведомления для всех сохраненных триггеров
     */
    fun scheduleAllNotifications(context: Context) {
        val triggers = getSavedTriggers().filter { it.enabled }
        scheduleNotifications(context, triggers)
    }

    private fun createPendingIntent(context: Context, trigger: SavedTrigger, notifTime: NotificationTime): PendingIntent {
        val intent = Intent(context, NotificationPublisher::class.java)
        intent.putExtra(NotificationPublisher.TRIGGER_NAME_KEY, trigger.name)
        intent.putExtra(NotificationPublisher.LOCATION_NAME_KEY, trigger.location.name)
        intent.putExtra(NotificationPublisher.LOCATION_ID_KEY, trigger.location.id)
        intent.putExtra(NotificationPublisher.NOTIF_TIME_UNIT_KEY, notifTime.unit)
        intent.putExtra(NotificationPublisher.NOTIF_TIME_VALUE_KEY, notifTime.value)
        return PendingIntent.getBroadcast(context, getNewRequestCode(), intent, 0)
    }

    /**
     * получить все времена уведемолений в миллисекундах
     * на основе времен алертов и настроек преждевременной нотификации
     */
    private fun getNotificationTimesMillis(trigger: SavedTrigger): List<Long> {
        val result = mutableListOf<Long>()
        trigger.alerts.distinctBy { it.id }.forEach { alert ->
            trigger.notificationTimes.forEach { time ->
                result.add(alert.value - time.getMilliseconds())
            }
        }
        return result.toList().filter { it > System.currentTimeMillis() }
    }

    /**
     * то же самое, что [getNotificationTimesMillis], но для одной пары алерт-настройка
     */
    private fun getNotificationTimeMillis(alert: Long, notifTime: NotificationTime): Long {
        return alert - notifTime.getMilliseconds()
    }

    /**
     * отменить все запланированные действия
     */
    fun cancelAllNotifications(context: Context) {
        Logger.i("All notifications cancel requested")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        getAllRequestCodes().forEach {
            alarmManager.cancel(
                    PendingIntent.getBroadcast(context, it, Intent(context, NotificationPublisher::class.java), 0)
            )
            alarmManager.cancel(
                    PendingIntent.getBroadcast(context, it, Intent(context, TriggerReEnabler::class.java), 0)
            )
        }
        deleteAllRequestCodes()
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