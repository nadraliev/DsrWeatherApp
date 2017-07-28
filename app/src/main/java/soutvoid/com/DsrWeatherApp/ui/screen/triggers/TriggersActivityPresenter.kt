package soutvoid.com.DsrWeatherApp.ui.screen.triggers

import com.agna.ferro.mvp.component.scope.PerScreen
import io.realm.Realm
import io.realm.RealmList
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import javax.inject.Inject

@PerScreen
class TriggersActivityPresenter @Inject constructor(errorHandler: ErrorHandler)
    :BasePresenter<TriggersActivityView>(errorHandler) {

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

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
}