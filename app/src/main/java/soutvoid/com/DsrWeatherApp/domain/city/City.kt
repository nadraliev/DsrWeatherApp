package soutvoid.com.DsrWeatherApp.domain.city

import io.realm.RealmObject

open class City(
        var name: String = "",
        var longitude: Double = .0,
        var latitude: Double = .0,
        var isFavorite: Boolean = false
) : RealmObject()