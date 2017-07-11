package soutvoid.com.DsrWeatherApp.domain.ultraviolet

import com.google.gson.annotations.SerializedName

data class Ultraviolet(
        @SerializedName("lat")
        val latitude: Double,
        @SerializedName("lon")
        val longitude: Double,
        @SerializedName("date_iso")
        val dateIso : String,
        @SerializedName("date")
        val date : Long,
        @SerializedName("value")
        val value : Double
)