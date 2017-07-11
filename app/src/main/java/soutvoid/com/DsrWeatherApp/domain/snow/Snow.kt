package soutvoid.com.DsrWeatherApp.domain.snow

import com.google.gson.annotations.SerializedName

data class Snow(
        @SerializedName("3h")
        val snowVolume : Int
)