package soutvoid.com.DsrWeatherApp.ui.util

import com.mikepenz.weather_icons_typeface_library.WeatherIcons

object WeatherIconsHelper {

    fun getWeatherIcon(id: Int, dt: Long, sunrise: Long, sunset: Long) : WeatherIcons.Icon {
        var night: Boolean = dt in sunset..sunrise

        if (night)
            return getNightWeatherIcon(id)
        else return getDayWeatherIcon(id)
    }

    fun getDayWeatherIcon(id : Int) : WeatherIcons.Icon {
        return WeatherIcons.Icon.valueOf("wic_owm_day_$id")
    }

    fun getNightWeatherIcon(id : Int) : WeatherIcons.Icon {
        return WeatherIcons.Icon.valueOf("wic_owm_night_$id")
    }

    fun getNeutralWeatherIcon(id : Int) : WeatherIcons.Icon {
        return WeatherIcons.Icon.valueOf("wic_owm_$id")
    }

    fun getDirectionalIcon(degrees: Double) : WeatherIcons.Icon {
        when (degrees) {
            in 27..71 -> return WeatherIcons.Icon.wic_direction_down_right
            in 72..116 -> return WeatherIcons.Icon.wic_cloud_down
            in 117..162 -> return WeatherIcons.Icon.wic_direction_down_left
            in 163..208 -> return WeatherIcons.Icon.wic_direction_left
            in 209..254 -> return WeatherIcons.Icon.wic_direction_up_left
            in 255..300 -> return WeatherIcons.Icon.wic_direction_up
            in 301..345 -> return WeatherIcons.Icon.wic_direction_up_right
        }
        return WeatherIcons.Icon.wic_direction_right
    }

}