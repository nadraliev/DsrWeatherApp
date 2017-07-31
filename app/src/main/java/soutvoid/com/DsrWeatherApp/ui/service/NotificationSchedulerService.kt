package soutvoid.com.DsrWeatherApp.ui.service

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.agna.ferro.mvp.component.scope.PerScreen
import com.birbit.android.jobqueue.JobManager
import io.realm.Realm
import io.realm.RealmResults
import soutvoid.com.DsrWeatherApp.app.App
import soutvoid.com.DsrWeatherApp.app.log.Logger
import soutvoid.com.DsrWeatherApp.domain.triggers.RealmLong
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.interactor.triggers.TriggersRepository
import soutvoid.com.DsrWeatherApp.ui.base.service.BaseIntentService
import soutvoid.com.DsrWeatherApp.ui.receivers.NotificationPublisher
import soutvoid.com.DsrWeatherApp.ui.receivers.RequestCode
import soutvoid.com.DsrWeatherApp.ui.util.NotificationUtils
import soutvoid.com.DsrWeatherApp.ui.util.TriggersUtils
import soutvoid.com.DsrWeatherApp.ui.util.realmListOf
import javax.inject.Inject

@PerScreen
class NotificationSchedulerService : BaseIntentService("NotificationSchedulerService") {

    companion object {
        const val ACTION_KEY = "action"
        const val TRIGGERS_IDS_LIST_KEY = "triggers_ids_list"
        fun startActionAdd(context: Context, triggersIds: List<Int>) {  //id
            val intent = Intent(context, NotificationSchedulerService::class.java)
            intent.putExtra(ACTION_KEY, Action.ADD)
            intent.putExtra(TRIGGERS_IDS_LIST_KEY, triggersIds.toIntArray())
            context.startService(intent)
        }
        fun startActionDelete(context: Context, triggersIds: List<String>) {   //triggerId
            val intent = Intent(context, NotificationSchedulerService::class.java)
            intent.putExtra(ACTION_KEY, Action.DELETE)
            intent.putExtra(TRIGGERS_IDS_LIST_KEY, triggersIds.toTypedArray())
            context.startService(intent)
        }
        fun startActionToggle(context: Context, triggersIds: List<Int>) {   //id
            val intent = Intent(context, NotificationSchedulerService::class.java)
            intent.putExtra(ACTION_KEY, Action.TOGGLE)
            intent.putExtra(TRIGGERS_IDS_LIST_KEY, triggersIds.toIntArray())
            context.startService(intent)
        }
    }

    /**
     * @property [ADD] создать новый триггер на сервере и создать нотификации
     * @property [DELETE] удалить нотификации, триггер с сервера и из бд
     * @property [TOGGLE] удалить нотификации, с сервера, но не из бд/создать триггер на сервере
     * @property [SCHEDULE_NOTIFICATIONS] заново планирует все нотификации после перезагрузки устройства
     */
    enum class Action { ADD, DELETE, TOGGLE, SCHEDULE_NOTIFICATIONS }

    @Inject
    lateinit var triggersRep: TriggersRepository

    @Inject
    lateinit var jobManager: JobManager

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null && intent.hasExtra(ACTION_KEY) && intent.hasExtra(TRIGGERS_IDS_LIST_KEY)) {
            satisfyDependencies()
            jobManager.addJobInBackground(AddTriggerJob())
            when (intent.getSerializableExtra(ACTION_KEY)) {
                Action.ADD -> addTriggers(intent.getIntArrayExtra(TRIGGERS_IDS_LIST_KEY))
                Action.DELETE -> deleteTriggers(intent.getStringArrayExtra(TRIGGERS_IDS_LIST_KEY))
                Action.TOGGLE -> toggleTriggers(intent.getIntArrayExtra(TRIGGERS_IDS_LIST_KEY))
                Action.SCHEDULE_NOTIFICATIONS -> scheduleAllNotifications()
            }
        }
    }

    private fun satisfyDependencies() {
        val component = DaggerNotificationSchedulerServiceComponent.builder()
                .appComponent(getAppComponent())
                .build()
        component.inject(this)
    }

    /**
     * получить сохраненные в бд триггеры по списку id
     */
    private fun getSavedTriggers(triggersIds: IntArray = intArrayOf()): List<SavedTrigger> {
        val realm = Realm.getDefaultInstance()
        var realmResults: RealmResults<SavedTrigger>?
        var list = emptyList<SavedTrigger>()
        if (triggersIds.isEmpty())
            realmResults = realm.where(SavedTrigger::class.java).findAll()
        else
            realmResults = realm.where(SavedTrigger::class.java).`in`("id", triggersIds.toTypedArray()).findAll()

        if (realmResults != null)
            list = realm.copyFromRealm(realmResults)
        realm.close()
        return list
    }

    /**
     * создать триггеры на сервере и обновить @see [SavedTrigger.triggerId] в бд, полученные от api
     */
    private fun addTriggers(triggersIds: IntArray) {
        if (triggersIds.isNotEmpty()) {
            val savedTriggers = getSavedTriggers(triggersIds)
            savedTriggers.forEach { trigger ->
                triggersRep.newTrigger(TriggersUtils.createRequest(trigger))
                        .subscribe { trigger.triggerId = it.id }
            }
            updateDbTriggers(savedTriggers)
            loadAlerts(savedTriggers)
            scheduleNotifications(savedTriggers)
        }
    }

    /**
     * сохранить внесенные изменения в бд
     */
    private fun updateDbTriggers(triggers: List<SavedTrigger>) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { it.copyToRealmOrUpdate(triggers) }
        realm.close()
    }

    /**
     * получить алерты триггеров от api, записать в бд
     */
    private fun loadAlerts(triggers: List<SavedTrigger>) {
        triggers.forEach { trigger->
            triggersRep.getTrigger(trigger.triggerId).subscribe {
                trigger.alerts = realmListOf(it.alerts.map { RealmLong(it.value.date) })
            }
        }
        updateDbTriggers(triggers)
    }

    /**
     * удалить триггеры с сервера и из бд
     */
    private fun deleteTriggers(triggersIds: Array<String>) {
        if (triggersIds.isNotEmpty()) {
            deleteServerTriggers(triggersIds)
            cancelAllNotifications()
            scheduleAllNotifications()
        }
    }

    /**
     * удалить триггеры с сервера
     */
    private fun deleteServerTriggers(triggersIds: Array<String>) {
        triggersIds.forEach { triggersRep.deleteTrigger(it).subscribe {  } }
    }

    /**
     * удалить триггеры из бд
     */
    private fun deleteSavedTriggers(triggers: List<SavedTrigger>) {
        if (triggers.isNotEmpty()) {
            val ids = triggers.map { it.id }.toTypedArray()
            if (ids.isNotEmpty()) {
                val realm = Realm.getDefaultInstance()
                realm.executeTransaction {
                    it.where(SavedTrigger::class.java).
                            `in`("id", ids).findAll().deleteAllFromRealm()
                }
                realm.close()
            }
        }
    }

    private fun toggleTriggers(triggersIds: IntArray) {
        if (triggersIds.isNotEmpty()) {
            val savedTriggers = getSavedTriggers(triggersIds)
            savedTriggers.forEach { toggleTrigger(it) }
        }
    }

    private fun toggleTrigger(trigger: SavedTrigger) {
        if (trigger.enabled) {
            addTriggers(intArrayOf(trigger.id))
        } else {
            deleteServerTriggers(arrayOf(trigger.triggerId))
            cancelAllNotifications()
            scheduleAllNotifications()
        }
    }

    private fun scheduleNotifications(triggers: List<SavedTrigger>) {
        val alarmManager = baseContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        triggers.forEach { trigger ->
            getNotificationTimesMillis(trigger).forEach { time ->
                val notification = NotificationUtils.createTriggerNotification(
                        baseContext,
                        trigger.name,
                        trigger.location.name)
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        time,
                        createPendingIntent(notification))
            }
        }
    }

    private fun cancelAllNotifications() {
        val alarmManager = baseContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        getAllRequestCodes().forEach {
            alarmManager.cancel(
                    PendingIntent.getBroadcast(baseContext, it, Intent(baseContext, NotificationPublisher::class.java), 0)
            )
        }
    }

    private fun scheduleAllNotifications() {
        val triggers = getSavedTriggers().filter { it.enabled }
        scheduleNotifications(triggers)
    }

    private fun createPendingIntent(notification: Notification): PendingIntent {
        val intent = Intent(baseContext, NotificationPublisher::class.java)
        intent.putExtra(NotificationPublisher.NOTIFICATION, notification)
        return PendingIntent.getBroadcast(baseContext, getNewRequestCode(), intent, 0)
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

    private fun getNewRequestCode(): Int {
        val realm = Realm.getDefaultInstance()
        val requestCode = RequestCode()
        realm.executeTransaction { it.copyToRealm(requestCode) }
        realm.close()
        return requestCode.value
    }

    private fun getAllRequestCodes(): List<Int> {
        val realm = Realm.getDefaultInstance()
        val realmResults = realm.where(RequestCode::class.java).findAll()
        var results = emptyList<Int>()
        realmResults?.let { results = realm.copyFromRealm(realmResults).map { it.value } }
        realm.close()
        return results
    }
}