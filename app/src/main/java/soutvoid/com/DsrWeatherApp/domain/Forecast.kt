package soutvoid.com.DsrWeatherApp.domain

import com.google.gson.annotations.SerializedName
import soutvoid.com.DsrWeatherApp.domain.city.City

data class Forecast(
        @SerializedName("city")
        val city: City,
        @SerializedName("list")
        val list : List<OneMomentForecast>
)