package soutvoid.com.DsrWeatherApp.ui.screen.triggers

import com.agna.ferro.mvp.component.scope.PerScreen
import io.realm.Realm
import io.realm.RealmList
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.ui.common.message.MessagePresenter
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.NewTriggerActivityView
import soutvoid.com.DsrWeatherApp.ui.util.SnackbarDismissedListener
import javax.inject.Inject

@PerScreen
class TriggersActivityPresenter @Inject constructor(errorHandler: ErrorHandler,
                                                    val messagePresenter: MessagePresenter)
    :BasePresenter<TriggersActivityView>(errorHandler) {

    private var undoClicked = false

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

    fun onSwitchClicked(savedTrigger: SavedTrigger, state: Boolean) {
        savedTrigger.enabled = state
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.copyToRealmOrUpdate(savedTrigger)
        }
        realm.close()
    }

    fun onTriggerRemoveRequested(savedTrigger: SavedTrigger, position: Int) {
        messagePresenter.showWithAction(R.string.trigger_removed, R.string.undo,
                { onUndoClicked(savedTrigger, position)})  //undo deleting
                .addCallback(SnackbarDismissedListener { _, _ ->
                    if (!undoClicked)
                        removeLocationFromDb(savedTrigger)
                    else undoClicked = false
                })  //delete from db when snackbar disappears
    }

    private fun onUndoClicked(savedTrigger: SavedTrigger, position: Int) {
        view.addLocationToPosition(savedTrigger, position)
        undoClicked = true
    }

    private fun removeLocationFromDb(savedTrigger: SavedTrigger) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.where(SavedTrigger::class.java).equalTo("id", savedTrigger.id).findAll().deleteAllFromRealm()
        }
        realm.close()
    }
}