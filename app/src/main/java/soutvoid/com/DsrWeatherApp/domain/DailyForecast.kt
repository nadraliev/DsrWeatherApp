package soutvoid.com.DsrWeatherApp.domain

import com.google.gson.annotations.SerializedName

data class DailyForecast(
        @SerializedName("list")
        val forecasts: List<OneDayForecast>
)