package soutvoid.com.DsrWeatherApp.interactor.triggers.data

import com.google.gson.annotations.SerializedName
import soutvoid.com.DsrWeatherApp.domain.triggers.area.Area
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.Condition
import soutvoid.com.DsrWeatherApp.domain.triggers.timePeriod.TimePeriod

data class TriggerRequest(
        @SerializedName("time_period")
        val timePeriod: TimePeriod,
        @SerializedName("conditions")
        val condition: List<Condition>,
        @SerializedName("area")
        val area: List<Area>
)