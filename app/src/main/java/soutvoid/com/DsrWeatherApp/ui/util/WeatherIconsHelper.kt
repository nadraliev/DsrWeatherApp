package soutvoid.com.DsrWeatherApp.ui.util

import com.mikepenz.weather_icons_typeface_library.WeatherIcons

object WeatherIconsHelper {

    /**
     * позволяет получить [WeatherIcons.Icon] по openweathermap id
     * в зависимости от текущего времени, времени заката и рассвета, возвращает разные иконки
     * @param [id] id погоды в openweathermap
     * @param [dt] время получения прогноза (обычно текущее время +- 10 мин) в секундах
     * @param [sunrise] время восхода в секундах
     * @param [sunset] время заката в секундах
     */
    fun getWeatherIcon(id: Int, dt: Long, sunrise: Long = dt, sunset: Long = dt + 1) : WeatherIcons.Icon {
        val day: Boolean = dt in sunrise..sunset

        if (day)
            return getDayWeatherIcon(id)
        else return getNightWeatherIcon(id)
    }

    /**
     * то же, что и [getWeatherIcon], но только дневные иконки
     */
    fun getDayWeatherIcon(id : Int) : WeatherIcons.Icon {
        return WeatherIcons.Icon.valueOf("wic_owm_day_$id")
    }

    /**
     * то же, что и [getWeatherIcon], но только ночные иконки
     */
    fun getNightWeatherIcon(id : Int) : WeatherIcons.Icon {
        return WeatherIcons.Icon.valueOf("wic_owm_night_$id")
    }

    /**
     * то же, что и [getWeatherIcon], но только нейтральные иконки
     */
    fun getNeutralWeatherIcon(id : Int) : WeatherIcons.Icon {
        return WeatherIcons.Icon.valueOf("wic_owm_$id")
    }

    /**
     * позволяет получить [WeatherIcons.Icon] для ветра в зависимости от направления ветра
     */
    fun getDirectionalIcon(degrees: Double) : WeatherIcons.Icon {
        when (degrees) {
            in 27..71 -> return WeatherIcons.Icon.wic_direction_down_right
            in 72..116 -> return WeatherIcons.Icon.wic_direction_down
            in 117..162 -> return WeatherIcons.Icon.wic_direction_down_left
            in 163..208 -> return WeatherIcons.Icon.wic_direction_left
            in 209..254 -> return WeatherIcons.Icon.wic_direction_up_left
            in 255..300 -> return WeatherIcons.Icon.wic_direction_up
            in 301..345 -> return WeatherIcons.Icon.wic_direction_up_right
        }
        return WeatherIcons.Icon.wic_direction_right
    }

}