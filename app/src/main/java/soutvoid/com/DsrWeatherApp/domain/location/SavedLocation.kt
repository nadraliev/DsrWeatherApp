package soutvoid.com.DsrWeatherApp.domain.location

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class SavedLocation(
        var name: String = "",
        var longitude: Double = .0,
        var latitude: Double = .0,
        var isFavorite: Boolean = false,
        @PrimaryKey
        var id: Int = SavedLocation.getNextKey()
) : RealmObject(), Serializable {
    companion object {
        private fun getNextKey() = System.currentTimeMillis().hashCode()
    }
}