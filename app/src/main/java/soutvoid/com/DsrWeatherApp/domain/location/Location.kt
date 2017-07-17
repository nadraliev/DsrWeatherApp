package soutvoid.com.DsrWeatherApp.domain.location

import io.realm.RealmObject

open class Location(
        var name: String = "",
        var longitude: Double = .0,
        var latitude: Double = .0,
        var isFavorite: Boolean = false
) : RealmObject()