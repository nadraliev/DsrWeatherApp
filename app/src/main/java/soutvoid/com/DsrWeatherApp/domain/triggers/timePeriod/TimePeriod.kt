package soutvoid.com.DsrWeatherApp.domain.triggers.timePeriod

import com.google.gson.annotations.SerializedName

data class TimePeriod(
        @SerializedName("start")
        val start: Time,
        @SerializedName("end")
        val end: Time
)