package soutvoid.com.DsrWeatherApp.interactor.triggers.jobs

import com.birbit.android.jobqueue.Params
import com.birbit.android.jobqueue.RetryConstraint
import io.realm.Realm
import soutvoid.com.DsrWeatherApp.app.dagger.AppComponent
import soutvoid.com.DsrWeatherApp.app.log.Logger
import soutvoid.com.DsrWeatherApp.domain.triggers.RealmLong
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.ui.util.NotificationUtils
import soutvoid.com.DsrWeatherApp.ui.util.TriggersUtils
import soutvoid.com.DsrWeatherApp.ui.util.getSavedTriggers
import soutvoid.com.DsrWeatherApp.ui.util.realmListOf

class AddTriggersJob(val triggersIds: IntArray) : BaseTriggerJob(Params(1).requireNetwork()) {

    override fun onRun() {
        addTriggers(triggersIds)
    }

    override fun shouldReRunOnThrowable(throwable: Throwable, runCount: Int, maxRunCount: Int): RetryConstraint {
        return RetryConstraint.RETRY
    }

    override fun onAdded() {

    }

    override fun onCancel(cancelReason: Int, throwable: Throwable?) {

    }

    override fun inject(appComponent: AppComponent) {
        super.inject(appComponent)
        appComponent.inject(this)
    }


    /**
     * создать триггеры на сервере и обновить @see [SavedTrigger.triggerId] в бд, полученные от api
     */
    private fun addTriggers(triggersIds: IntArray) {
        if (triggersIds.isNotEmpty()) {
            val savedTriggers = getSavedTriggers(triggersIds)
            savedTriggers.forEach { trigger ->
                triggerRep.newTrigger(TriggersUtils.createRequest(trigger))
                        .subscribe { trigger.triggerId = it.id }
            }
            updateDbTriggers(savedTriggers)
            Thread.sleep(1000)  //почему-то если не подождать, самый первый триггер, созданный пользователем, возвращает 0 алертов
            loadAlerts(savedTriggers)
            NotificationUtils.scheduleNotifications(applicationContext, savedTriggers)
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
            triggerRep.getTrigger(trigger.triggerId).subscribe {
                trigger.alerts = realmListOf(it.alerts.map { RealmLong(it.value.date) })
            }
        }
        updateDbTriggers(triggers)
    }
}