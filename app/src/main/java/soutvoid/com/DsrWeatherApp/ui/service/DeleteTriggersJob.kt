package soutvoid.com.DsrWeatherApp.ui.service

import com.birbit.android.jobqueue.Params
import com.birbit.android.jobqueue.RetryConstraint
import soutvoid.com.DsrWeatherApp.ui.util.NotificationUtils

class DeleteTriggersJob(val triggersIds: Array<String>): BaseTriggerJob(Params(1).requireNetwork()) {

    override fun onRun() {
        deleteTriggers(triggersIds)
    }

    override fun shouldReRunOnThrowable(throwable: Throwable, runCount: Int, maxRunCount: Int): RetryConstraint {
        return RetryConstraint.RETRY
    }

    override fun onAdded() {
    }

    override fun onCancel(cancelReason: Int, throwable: Throwable?) {
    }

    /**
     * удалить триггеры с сервера и из бд
     */
    private fun deleteTriggers(triggersIds: Array<String>) {
        if (triggersIds.isNotEmpty()) {
            deleteServerTriggers(triggersIds)
            NotificationUtils.cancelAllNotifications(applicationContext)
            NotificationUtils.scheduleAllNotifications(applicationContext)
        }
    }

    /**
     * удалить триггеры с сервера
     */
    private fun deleteServerTriggers(triggersIds: Array<String>) {
        triggersIds.forEach { triggerRep.deleteTrigger(it).subscribe {  } }
    }
}