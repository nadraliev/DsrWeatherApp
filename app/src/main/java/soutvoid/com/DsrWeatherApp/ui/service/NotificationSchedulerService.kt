package soutvoid.com.DsrWeatherApp.ui.service

import android.content.Context
import android.content.Intent
import com.agna.ferro.mvp.component.scope.PerScreen
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import soutvoid.com.DsrWeatherApp.domain.triggers.RealmLong
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.interactor.triggers.TriggersRepository
import soutvoid.com.DsrWeatherApp.ui.base.service.BaseIntentService
import soutvoid.com.DsrWeatherApp.ui.util.TriggersUtils
import soutvoid.com.DsrWeatherApp.ui.util.realmListOf
import javax.inject.Inject

@PerScreen
class NotificationSchedulerService : BaseIntentService("NotificationSchedulerService") {

    companion object {
        const val ACTION_KEY = "action"
        const val TRIGGERS_IDS_LIST_KEY = "triggers_ids_list"
        fun startActionAdd(context: Context, triggersIds: List<Int>) {
            val intent = Intent(context, NotificationSchedulerService::class.java)
            intent.putExtra(ACTION_KEY, Action.ADD)
            intent.putExtra(TRIGGERS_IDS_LIST_KEY, triggersIds.toIntArray())
            context.startService(intent)
        }
        fun startActionDelete(context: Context, triggersIds: List<Int>) {
            val intent = Intent(context, NotificationSchedulerService::class.java)
            intent.putExtra(ACTION_KEY, Action.DELETE)
            intent.putExtra(TRIGGERS_IDS_LIST_KEY, triggersIds.toIntArray())
            context.startService(intent)
        }
        fun startActionToggle(context: Context, triggersIds: List<Int>) {
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
     */
    enum class Action { ADD, DELETE, TOGGLE }

    @Inject
    lateinit var triggersRep: TriggersRepository

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null && intent.hasExtra(ACTION_KEY) && intent.hasExtra(TRIGGERS_IDS_LIST_KEY)) {
            satisfyDependencies()
            when (intent.getSerializableExtra(ACTION_KEY)) {
                Action.ADD -> addTriggers(intent.getIntArrayExtra(TRIGGERS_IDS_LIST_KEY))
                Action.DELETE -> deleteTriggers(intent.getIntArrayExtra(TRIGGERS_IDS_LIST_KEY))
                Action.TOGGLE -> toggleTriggers(intent.getIntArrayExtra(TRIGGERS_IDS_LIST_KEY))
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
    private fun deleteTriggers(triggersIds: IntArray) {
        if (triggersIds.isNotEmpty()) {
            val savedTriggers = getSavedTriggers(triggersIds)
            deleteServerTriggers(savedTriggers)
            deleteSavedTriggers(savedTriggers)
        }
    }

    /**
     * удалить триггеры с сервера
     */
    private fun deleteServerTriggers(triggers: List<SavedTrigger>) {
        triggers.forEach { triggersRep.deleteTrigger(it.triggerId).subscribe {  } }
    }

    /**
     * удалить триггеры из бд
     */
    private fun deleteSavedTriggers(triggers: List<SavedTrigger>) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.where(SavedTrigger::class.java).
                    `in`("id", triggers.map { it.id }.toTypedArray()).findAll().deleteAllFromRealm()
        }
        realm.close()
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
            deleteServerTriggers(listOf(trigger))
        }
    }
}