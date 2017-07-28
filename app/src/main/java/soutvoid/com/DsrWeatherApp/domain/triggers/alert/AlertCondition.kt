package soutvoid.com.DsrWeatherApp.domain.triggers.alert

import com.google.gson.annotations.SerializedName
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.Condition

data class AlertCondition(
        @SerializedName("current_value")
        val current: CurrentAlertCondition,
        @SerializedName("condition")
        val requested: Condition
)