package soutvoid.com.DsrWeatherApp.domain.city

import io.realm.RealmObject

data class City(
        val name: String,
        val longitude: Double,
        val latitude: Double,
        val isFavorite: Boolean
) : RealmObject()