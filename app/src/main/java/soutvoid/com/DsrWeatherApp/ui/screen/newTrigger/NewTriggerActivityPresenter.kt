package soutvoid.com.DsrWeatherApp.ui.screen.newTrigger

import com.agna.ferro.mvp.component.scope.PerScreen
import io.realm.Realm
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.Condition
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.timeDialog.data.NotificationTime
import soutvoid.com.DsrWeatherApp.ui.util.getNiceNameStringId
import soutvoid.com.DsrWeatherApp.ui.util.getNiceStringId
import javax.inject.Inject

@PerScreen
class NewTriggerActivityPresenter @Inject constructor(errorHandler: ErrorHandler)
    :BasePresenter<NewTriggerActivityView>(errorHandler) {

    private lateinit var location: SavedLocation
    private val conditions = mutableListOf<Condition>()
    private val notificationTimes = mutableListOf<NotificationTime>()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        loadInitLocation()

    }

    private fun loadInitLocation() {
        val realm = Realm.getDefaultInstance()
        val location = realm.where(SavedLocation::class.java).findFirst()
        if (location != null) {
            this.location = location
            view.setLocationName(location.name)
        } else
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
        location = getAllLocations()[position]
        view.setLocationName(location.name)
    }

    fun onAddConditionClicked() {
        view.showNewConditionsDialog()
    }

    fun onConditionClicked(position: Int) {
        view.showEditConditionDialog(position)
    }

    fun onNewConditionChosen(condition: Condition?) {
        condition?.let {
            conditions.add(it)
            view.showNewCondition(
                    it.name.getNiceNameStringId(),
                    it.expression.getNiceStringId(),
                    it.amount
            )
        }
    }

    fun onConditionEdited(position: Int, condition: Condition?) {
        if (condition != null) {
            conditions[position] = condition
            view.editCondition(
                    position,
                    condition.name.getNiceNameStringId(),
                    condition.expression.getNiceStringId(),
                    condition.amount)
        } else {
            conditions.removeAt(position)
            view.removeCondition(position)
        }
    }

    fun onAddTimeClicked() {
        view.showNewTimeDialog()
    }

    fun onTimeClicked(position: Int) {
        view.showEditTimeDialog(position)
    }

    fun onNewTimeChosen(before: NotificationTime?) {
        before?.let {
            notificationTimes.add(it)
            view.showNewTime(it)
        }
    }

    fun onTimeEdited(position: Int, before: NotificationTime?) {
        if (before != null) {
            notificationTimes[position] = before
            view.editTime(position, before)
        } else {
            notificationTimes.removeAt(position)
            view.removeTime(position)
        }
    }
}