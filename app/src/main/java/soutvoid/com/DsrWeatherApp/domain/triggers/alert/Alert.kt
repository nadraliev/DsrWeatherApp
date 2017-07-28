package soutvoid.com.DsrWeatherApp.domain.triggers.alert

import com.google.gson.annotations.SerializedName
import soutvoid.com.DsrWeatherApp.domain.coordinates.Coordinates

data class Alert(
        @SerializedName("conditions")
        val conditions: List<AlertCondition>,
        @SerializedName("last_update")
        val lastUpdate: Long,
        @SerializedName("date")
        val date: Long,
        @SerializedName("coordinates")
        val coordinates: Coordinates
)