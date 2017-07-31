package soutvoid.com.DsrWeatherApp.ui.screen.newTrigger

import com.agna.ferro.mvp.component.scope.PerScreen
import com.birbit.android.jobqueue.JobManager
import io.realm.Realm
import io.realm.RealmList
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.Condition
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.ConditionExpression
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.ConditionName
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.SavedCondition
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.timeDialog.data.NotificationTime
import soutvoid.com.DsrWeatherApp.ui.service.AddTriggersJob
import soutvoid.com.DsrWeatherApp.ui.util.getNiceNameStringId
import soutvoid.com.DsrWeatherApp.ui.util.getNiceStringId
import javax.inject.Inject

@PerScreen
class NewTriggerActivityPresenter @Inject constructor(errorHandler: ErrorHandler)
    : BasePresenter<NewTriggerActivityView>(errorHandler) {

    @Inject
    lateinit var jobManager: JobManager

    private var location: SavedLocation? = null
    private val conditions = mutableListOf<Condition>()
    private val notificationTimes = mutableListOf<NotificationTime>()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        loadInitLocation()
        showInitCondition()
        showInitNotificationTime()
    }

    private fun loadInitLocation() {
        val realm = Realm.getDefaultInstance()
        val locationRealm = realm.where(SavedLocation::class.java).findFirst()
        location = locationRealm?.let { realm.copyFromRealm(locationRealm) }
        if (location != null) {
            this.location = location
            view.setLocationName(location!!.name)
        } else
            view.setLocationNameChoosable()
        realm.close()
    }

    private fun getAllLocations(): List<SavedLocation> {
        val realm = Realm.getDefaultInstance()
        val locations = realm.copyFromRealm(realm.where(SavedLocation::class.java).findAll())
        realm.close()
        return locations
    }

    private fun showInitCondition() {
        onNewConditionChosen(Condition(ConditionName.temp, ConditionExpression.gt, 303.0))
    }

    private fun showInitNotificationTime() {
        onNewTimeChosen(NotificationTime(1, 0))
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
        location?.let { view.setLocationName(it.name) }
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
                    it.amount.toInt(),
                    it.name == ConditionName.temp
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
                    condition.amount.toInt(),
                    condition.name == ConditionName.temp)
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

    fun onCheckClicked() {
        if (location != null && conditions.size != 0 && notificationTimes.size != 0) {
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction {
                val savedConditions = RealmList<SavedCondition>()
                savedConditions.addAll(conditions.map { SavedCondition(it.name.toString(), it.expression.toString(), it.amount) })
                val savedNotificationTimes = RealmList<NotificationTime>()
                savedNotificationTimes.addAll(notificationTimes)
                val trigger = SavedTrigger(
                        name = view.getTypedName(),
                        enabled = true,
                        location = location!!,
                        conditions = savedConditions,
                        notificationTimes = savedNotificationTimes
                )
                it.copyToRealmOrUpdate(trigger)
                jobManager.addJobInBackground(AddTriggersJob(intArrayOf(trigger.id)))
            }
            realm.close()
            view.finish()
        } else
            view.showEmptyFieldsMessage()
    }
}