package soutvoid.com.DsrWeatherApp.domain.city

import com.google.gson.annotations.SerializedName
import soutvoid.com.DsrWeatherApp.domain.coordinates.Coordinates

data class CityOwm(
        @SerializedName("id")
        val id: Long,
        @SerializedName("name")
        val name: String,
        @SerializedName("coord")
        val coordinates: Coordinates,
        @SerializedName("country")
        val country : String
)