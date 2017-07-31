package soutvoid.com.DsrWeatherApp.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.agna.ferro.mvp.component.scope.PerApplication
import com.birbit.android.jobqueue.JobManager
import soutvoid.com.DsrWeatherApp.app.App
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.interactor.triggers.jobs.AddTriggersJob
import soutvoid.com.DsrWeatherApp.interactor.triggers.jobs.DeleteTriggersJob
import soutvoid.com.DsrWeatherApp.ui.util.ifNotNull
import javax.inject.Inject

@PerApplication
class TriggerReEnabler: BroadcastReceiver() {

    companion object {
        const val TRIGGER_KEY = "trigger"
    }

    @Inject
    lateinit var jobManager: JobManager

    override fun onReceive(context: Context?, intent: Intent?) {
        ifNotNull(context, intent) { context, intent ->
            satisfyDependencies(context)
            if (intent.hasExtra(TRIGGER_KEY)) {
                val trigger = intent.getSerializableExtra(TRIGGER_KEY) as SavedTrigger
                jobManager.addJobInBackground(
                        DeleteTriggersJob(arrayOf(trigger.triggerId))) {
                    jobManager.addJobInBackground(AddTriggersJob(intArrayOf(trigger.id)))
                }
            }
        }
    }

    fun satisfyDependencies(context: Context) {
        (context.applicationContext as App).appComponent.triggerReEnabler(this)
    }
}