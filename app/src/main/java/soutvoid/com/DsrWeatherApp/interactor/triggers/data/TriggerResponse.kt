package soutvoid.com.DsrWeatherApp.interactor.triggers.data

import com.google.gson.annotations.SerializedName
import soutvoid.com.DsrWeatherApp.domain.triggers.alert.Alert
import soutvoid.com.DsrWeatherApp.domain.triggers.area.Area
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.Condition
import soutvoid.com.DsrWeatherApp.domain.triggers.timePeriod.TimePeriod

data class TriggerResponse(
        @SerializedName("_id")
        val id: String,
        @SerializedName("alerts")
        val alerts: Map<String, Alert>,
        @SerializedName("area")
        val area: List<Area>,
        @SerializedName("conditions")
        val conditions: List<Condition>,
        @SerializedName("time_period")
        val timePeriod: TimePeriod
)