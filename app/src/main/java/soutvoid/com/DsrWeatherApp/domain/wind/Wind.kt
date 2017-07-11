package soutvoid.com.DsrWeatherApp.domain.wind

import com.google.gson.annotations.SerializedName

data class Wind(
        @SerializedName("speed")
        val speed : Double,
        @SerializedName("deg")
        val degrees : Double
)