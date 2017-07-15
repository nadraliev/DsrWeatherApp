package soutvoid.com.DsrWeatherApp.domain

import com.google.gson.annotations.SerializedName
import soutvoid.com.DsrWeatherApp.domain.city.CityOwm

data class Forecast(
        @SerializedName("city")
        val cityOwm: CityOwm,
        @SerializedName("list")
        val list : List<ThreeHoursForecast>
)