package soutvoid.com.DsrWeatherApp.domain

import com.google.gson.annotations.SerializedName
import soutvoid.com.DsrWeatherApp.domain.clouds.Clouds
import soutvoid.com.DsrWeatherApp.domain.coordinates.Coordinates
import soutvoid.com.DsrWeatherApp.domain.main.Main
import soutvoid.com.DsrWeatherApp.domain.rain.Rain
import soutvoid.com.DsrWeatherApp.domain.snow.Snow
import soutvoid.com.DsrWeatherApp.domain.sys.Sys
import soutvoid.com.DsrWeatherApp.domain.weather.Weather
import soutvoid.com.DsrWeatherApp.domain.wind.Wind

data class CurrentWeather(
        @SerializedName("coord")
        val coordinates: Coordinates,
        @SerializedName("weather")
        val weather : List<Weather>,
        @SerializedName("main")
        val main: Main,
        @SerializedName("wind")
        val wind : Wind,
        @SerializedName("clouds")
        val clouds: Clouds,
        @SerializedName("rain")
        val rain: Rain,
        @SerializedName("snow")
        val snow: Snow,
        @SerializedName("dt")
        val timeOfData : Long,
        @SerializedName("sys")
        val sys : Sys,
        @SerializedName("id")
        val cityId : Long,
        @SerializedName("name")
        val cityName : String
)