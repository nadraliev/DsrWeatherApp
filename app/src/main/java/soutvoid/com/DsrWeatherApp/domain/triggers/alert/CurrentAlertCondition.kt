package soutvoid.com.DsrWeatherApp.domain.triggers.alert

import com.google.gson.annotations.SerializedName

data class CurrentAlertCondition(
        @SerializedName("min")
        val min: Double,
        @SerializedName("max")
        val max: Double
)