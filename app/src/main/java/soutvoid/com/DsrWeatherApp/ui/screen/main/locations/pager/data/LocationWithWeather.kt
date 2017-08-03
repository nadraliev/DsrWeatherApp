package soutvoid.com.DsrWeatherApp.ui.screen.main.locations.pager.data

import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation

data class LocationWithWeather(
        val location: SavedLocation,
        val currentWeather: CurrentWeather
)