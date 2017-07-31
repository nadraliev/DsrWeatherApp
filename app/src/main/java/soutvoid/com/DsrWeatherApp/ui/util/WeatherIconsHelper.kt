package soutvoid.com.DsrWeatherApp.ui.util

import com.mikepenz.weather_icons_typeface_library.WeatherIcons
import soutvoid.com.DsrWeatherApp.ui.screen.main.data.TimeOfDay
import java.util.*

object WeatherIconsHelper {

    /**
     * позволяет получить [WeatherIcons.Icon] по openweathermap id
     * в зависимости от текущего времени, времени заката и рассвета, возвращает разные иконки
     * @param [id] id погоды в openweathermap
     * @param [dt] время получения прогноза (обычно текущее время +- 10 мин) в секундах
     */
    fun getWeatherIcon(id: Int, dt: Long, locale: Locale = Locale.getDefault()) : WeatherIcons.Icon {
        val calendar = Calendar.getInstance(locale)
        calendar.timeInMillis = dt * 1000
        val timeOfDay = TimeOfDay.getByTime(calendar.get(Calendar.HOUR_OF_DAY))
        val day: Boolean = timeOfDay == TimeOfDay.MORNING || timeOfDay == TimeOfDay.DAY

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

    val windIcons = listOf<WeatherIcons.Icon>(
            WeatherIcons.Icon.wic_direction_down,
            WeatherIcons.Icon.wic_direction_down_left,
            WeatherIcons.Icon.wic_direction_right,
            WeatherIcons.Icon.wic_direction_up_left,
            WeatherIcons.Icon.wic_direction_up,
            WeatherIcons.Icon.wic_direction_up_right,
            WeatherIcons.Icon.wic_direction_left,
            WeatherIcons.Icon.wic_direction_down_right)

    /**
     * позволяет получить [WeatherIcons.Icon] для ветра в зависимости от направления ветра
     */
    fun getDirectionalIcon(degrees: Double) : WeatherIcons.Icon {
        return windIcons[Math.round((degrees + 180) % 360 / 45).toInt() % 8]
    }

}