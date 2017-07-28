package soutvoid.com.DsrWeatherApp.domain.triggers.timePeriod

import com.google.gson.annotations.SerializedName

data class Time(
        @SerializedName("expression")
        val expression: TimeExpression,
        @SerializedName("amount")
        val amount: Long
)