package soutvoid.com.DsrWeatherApp.ui.screen.main.data

import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.DailyForecast
import soutvoid.com.DsrWeatherApp.domain.Forecast

data class AllWeatherData(
        val currentWeather: CurrentWeather,
        val forecast: Forecast,
        val dailyForecast: DailyForecast
)