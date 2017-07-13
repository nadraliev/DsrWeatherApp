package soutvoid.com.DsrWeatherApp.ui.screen.main.data

import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.DailyForecast
import soutvoid.com.DsrWeatherApp.domain.Forecast
import soutvoid.com.DsrWeatherApp.domain.ultraviolet.Ultraviolet

data class AllWeatherData(
        val currentWeather: CurrentWeather,
        val forecast: Forecast,
        val ultraviolet: Ultraviolet,
        val dailyForecast: DailyForecast
)