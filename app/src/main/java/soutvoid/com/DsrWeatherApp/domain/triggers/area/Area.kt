package soutvoid.com.DsrWeatherApp.domain.triggers.area

import com.google.gson.annotations.SerializedName

data class Area(
        @SerializedName("coordinates")
        val coordinates: List<Double>
)