package soutvoid.com.DsrWeatherApp.ui.screen.newTrigger

import com.agna.ferro.mvp.component.scope.PerScreen
import io.realm.Realm
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.Condition
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import soutvoid.com.DsrWeatherApp.ui.util.getNiceNameStringId
import soutvoid.com.DsrWeatherApp.ui.util.getNiceStringId
import javax.inject.Inject

@PerScreen
class NewTriggerActivityPresenter @Inject constructor(errorHandler: ErrorHandler)
    :BasePresenter<NewTriggerActivityView>(errorHandler) {

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        loadInitLocation()

    }

    private fun loadInitLocation() {
        val realm = Realm.getDefaultInstance()
        val location = realm.where(SavedLocation::class.java).findFirst()
        if (location != null)
            view.setLocationName(location.name)
        else
            view.setLocationNameChoosable()
        realm.close()
    }

    private fun getAllLocations(): List<SavedLocation> {
        val realm = Realm.getDefaultInstance()
        val locations = realm.copyFromRealm(realm.where(SavedLocation::class.java).findAll())
        return locations
    }

    fun onLocationClicked() {
        val allLocations = getAllLocations()
        if (allLocations.isNotEmpty())
            view.showLocationsDialog(allLocations.map { it.name })
        else
            view.showNoLocationsDialog()
    }

    fun onLocationChosen(position: Int) {
        view.setLocationName(getAllLocations()[position].name)
    }

    fun onAddConditionClicked() {
        view.showConditionsDialog()
    }

    fun onConditionChosen(condition: Condition) {
        view.showNewCondition(
                condition.name.getNiceNameStringId(),
                condition.expression.getNiceStringId(),
                condition.amount
        )
    }
}