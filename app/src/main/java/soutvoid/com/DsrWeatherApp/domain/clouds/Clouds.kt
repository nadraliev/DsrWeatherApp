package soutvoid.com.DsrWeatherApp.domain.clouds

import com.google.gson.annotations.SerializedName

data class Clouds(
        @SerializedName("all")
        val cloudiness : Int
)