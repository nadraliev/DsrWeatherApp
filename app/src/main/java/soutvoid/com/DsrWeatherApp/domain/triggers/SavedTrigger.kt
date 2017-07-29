package soutvoid.com.DsrWeatherApp.domain.triggers

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.Condition
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.SavedCondition
import soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.timeDialog.data.NotificationTime
import java.io.Serializable

open class SavedTrigger(
        var triggerId: String = "",
        var name: String = "",
        var enabled: Boolean = false,
        var location: SavedLocation = SavedLocation(),
        var conditions: RealmList<SavedCondition> = RealmList(),
        var notificationTimes: RealmList<NotificationTime> = RealmList(),
        @PrimaryKey
        var id: Int = SavedTrigger.getNextKey()
) : RealmObject(), Serializable {
    companion object {
        private fun getNextKey() = System.currentTimeMillis().hashCode()
    }
}