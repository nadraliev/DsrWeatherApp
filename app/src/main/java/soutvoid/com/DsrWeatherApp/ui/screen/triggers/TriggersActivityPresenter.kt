package soutvoid.com.DsrWeatherApp.ui.screen.triggers

import com.agna.ferro.mvp.component.scope.PerScreen
import io.realm.Realm
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.ui.common.message.MessagePresenter
import soutvoid.com.DsrWeatherApp.ui.util.SnackbarDismissedListener
import javax.inject.Inject

@PerScreen
class TriggersActivityPresenter @Inject constructor(errorHandler: ErrorHandler,
                                                    val messagePresenter: MessagePresenter)
    :BasePresenter<TriggersActivityView>(errorHandler) {

    private var undoClicked = false
    private var removeCandidate: SavedTrigger? = null

    override fun onResume() {
        super.onResume()

        loadData()
    }

    private fun loadData() {
        val realm = Realm.getDefaultInstance()
        view.showData(realm.copyFromRealm(realm.where(SavedTrigger::class.java).findAll()))
        realm.close()
    }

    fun onTriggerClicked(savedTrigger: SavedTrigger) {

    }

    fun onSwitchClicked(savedTrigger: SavedTrigger) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val triggerRealm = it.where(SavedTrigger::class.java).equalTo("id", savedTrigger.id).findFirst()
            triggerRealm.enabled = !triggerRealm.enabled
        }
        realm.close()
        view.notifyServiceTriggerToggled(savedTrigger.id)
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
        view?.notifyServiceTriggerDeleted(savedTrigger.triggerId)
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.where(SavedTrigger::class.java)
                    .equalTo("id", savedTrigger.id).findAll().deleteAllFromRealm()
        }
    }

    override fun onPause() {
        removeCandidate?.let { removeNotificationFromDb(it) }
        super.onPause()
    }
}