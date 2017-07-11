package soutvoid.com.DsrWeatherApp.domain.rain

import com.google.gson.annotations.SerializedName

data class Rain(
        @SerializedName("3h")
        val rainVolume : Int
)