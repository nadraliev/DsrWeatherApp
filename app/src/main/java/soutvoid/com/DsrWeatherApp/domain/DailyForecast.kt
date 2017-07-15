package soutvoid.com.DsrWeatherApp.domain

import com.google.gson.annotations.SerializedName

data class DailyForecast(
        @SerializedName("list")
        var forecasts: List<OneDayForecast>
)