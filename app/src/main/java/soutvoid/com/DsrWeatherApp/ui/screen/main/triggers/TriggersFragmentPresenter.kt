package soutvoid.com.DsrWeatherApp.ui.screen.main.triggers

import com.agna.ferro.mvp.component.scope.PerScreen
import com.birbit.android.jobqueue.JobManager
import io.realm.Realm
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.ui.common.message.MessagePresenter
import soutvoid.com.DsrWeatherApp.interactor.triggers.jobs.AddTriggersJob
import soutvoid.com.DsrWeatherApp.interactor.triggers.jobs.DeleteTriggersJob
import soutvoid.com.DsrWeatherApp.ui.util.SnackbarDismissedListener
import javax.inject.Inject

@PerScreen
class TriggersFragmentPresenter @Inject constructor(errorHandler: ErrorHandler,
                                                    val messagePresenter: MessagePresenter)
    :BasePresenter<TriggersFragmentView>(errorHandler) {

    @Inject
    lateinit var jobManager: JobManager

    private var undoClicked = false
    private var removeCandidate: SavedTrigger? = null

    override fun onResume() {
        super.onResume()

        loadData()
    }

    private fun loadData() {
        val realm = Realm.getDefaultInstance()
        val results = realm.copyFromRealm(realm.where(SavedTrigger::class.java).findAll())
        if (results.isNotEmpty()) {
            view?.hidePlaceholder()
            view?.showData(results)
        }
        else
            view?.showNoNotificationsMessage()
        realm.close()
    }

    fun onTriggerClicked(savedTrigger: SavedTrigger) {

    }

    fun onSwitchClicked(savedTrigger: SavedTrigger, position: Int) {
        var newState = false
        var newTrigger = savedTrigger
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val triggerRealm = it.where(SavedTrigger::class.java).equalTo("id", savedTrigger.id).findFirst()
            triggerRealm.enabled = !triggerRealm.enabled
            newState = triggerRealm.enabled
            newTrigger = it.copyFromRealm(triggerRealm)
        }
        realm.close()
        if (newState)
            jobManager.addJobInBackground(AddTriggersJob(intArrayOf(newTrigger.id)))
        else
            jobManager.addJobInBackground(DeleteTriggersJob(arrayOf(newTrigger.triggerId)))
    }

    fun onTriggerRemoveRequested(savedTrigger: SavedTrigger, position: Int) {
        removeCandidate = savedTrigger
        messagePresenter.showWithAction(R.string.trigger_removed, R.string.undo,
                { onUndoClicked(savedTrigger, position)})  //undo deleting
                .addCallback(SnackbarDismissedListener { _, _ ->
                    if (!undoClicked)
                        removeNotificationFromDb(savedTrigger)
                    else undoClicked = false
                })  //delete from db when snackbar disappears
    }

    private fun onUndoClicked(savedTrigger: SavedTrigger, position: Int) {
        view.addLocationToPosition(savedTrigger, position)
        undoClicked = true
    }

    private fun removeNotificationFromDb(savedTrigger: SavedTrigger) {
        jobManager.addJobInBackground(DeleteTriggersJob(arrayOf(savedTrigger.triggerId)))
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.where(SavedTrigger::class.java)
                    .equalTo("id", savedTrigger.id).findFirst()?.deleteCascade()
        }
    }

    override fun onPause() {
        removeCandidate?.let { removeNotificationFromDb(it) }
        super.onPause()
    }
}