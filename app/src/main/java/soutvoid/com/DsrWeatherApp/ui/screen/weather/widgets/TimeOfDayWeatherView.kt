package soutvoid.com.DsrWeatherApp.ui.screen.weather.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import butterknife.ButterKnife
import com.mikepenz.iconics.IconicsDrawable
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.ThreeHoursForecast
import soutvoid.com.DsrWeatherApp.ui.screen.weather.data.TimeOfDay
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import soutvoid.com.DsrWeatherApp.ui.util.WeatherIconsHelper
import soutvoid.com.DsrWeatherApp.ui.util.getThemeColor
import java.util.*

import kotlinx.android.synthetic.main.view_time_of_day_weather.view.*
import soutvoid.com.DsrWeatherApp.ui.util.CalendarUtils

/**
 * view для отображения погоды в определенное время дня (утро, день и тд)
 */
class TimeOfDayWeatherView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
        View.inflate(context, R.layout.view_time_of_day_weather, this)
        ButterKnife.bind(this)
    }

    fun setWeather(threeHoursForecast: ThreeHoursForecast, locale: Locale = Locale.getDefault()) {
        with(threeHoursForecast) {
            val calendar: Calendar = Calendar.getInstance(locale)
            calendar.timeInMillis = timeOfData * 1000

            view_tod_weather_date.text = getDateString(threeHoursForecast.timeOfData)

            view_tod_weather_name.text = getTimeOfDayNameByHour(calendar.get(Calendar.HOUR_OF_DAY))
            view_tod_weather_icon.setImageDrawable(IconicsDrawable(context)
                    .icon(WeatherIconsHelper.getWeatherIcon(weather[0].id, timeOfData))
                    .color(context.theme.getThemeColor(android.R.attr.textColorPrimary))
                    .sizeDp(32))
            view_tod_weather_temperature.text = "${Math.round(main.temperature)} ${UnitsUtils.getDegreesUnits(context)}"
            view_tod_weather_wind_icon.setImageDrawable(IconicsDrawable(context)
                    .icon(WeatherIconsHelper.getDirectionalIcon(wind.degrees))
                    .color(context.theme.getThemeColor(android.R.attr.textColorPrimary))
                    .sizeDp(16))
            view_tod_weather_wind.text = wind.speed.toString()
            view_tod_weather_wind_units.text = UnitsUtils.getVelocityUnits(context)
        }
    }

    private fun getTimeOfDayNameByHour(hour: Int): String {
        val timeOfDay = TimeOfDay.getByTime(hour)
        when(timeOfDay) {
            TimeOfDay.MORNING -> return context.getString(R.string.morning)
            TimeOfDay.DAY -> return context.getString(R.string.day)
            TimeOfDay.EVENING -> return context.getString(R.string.evening)
            else -> return context.getString(R.string.night)
        }
    }

    private fun getDateString(dt: Long): String {
        var dateStr = CalendarUtils.getNumericDate(dt)
        if (CalendarUtils.isToday(dt))
            dateStr = context.getString(R.string.today)
        else if (CalendarUtils.isTomorrow(dt))
            dateStr = context.getString(R.string.tomorrow)
        return dateStr
    }

}