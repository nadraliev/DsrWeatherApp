package soutvoid.com.DsrWeatherApp.ui.screen.main.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.mikepenz.iconics.IconicsDrawable
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.OneMomentForecast
import soutvoid.com.DsrWeatherApp.ui.screen.main.data.TimeOfDay
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import soutvoid.com.DsrWeatherApp.ui.util.WeatherIconsHelper
import soutvoid.com.DsrWeatherApp.ui.util.WindUtils
import soutvoid.com.DsrWeatherApp.ui.util.getThemeColor
import java.util.*

class TimeOfDayWeatherView : FrameLayout {

    @BindView(R.id.view_tod_weather_name)
    lateinit var name: TextView
    @BindView(R.id.view_tod_weather_icon)
    lateinit var icon: ImageView
    @BindView(R.id.view_tod_weather_temperature)
    lateinit var temperature: TextView
    @BindView(R.id.view_tod_weather_wind_icon)
    lateinit var windIcon: ImageView
    @BindView(R.id.view_tod_weather_wind)
    lateinit var windInfo: TextView

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    init {
        View.inflate(context, R.layout.view_time_of_day_weather, this)
        ButterKnife.bind(this)
    }

    fun setWeather(oneMomentForecast: OneMomentForecast, locale: Locale = Locale.getDefault()) {
        with(oneMomentForecast) {
            val calendar: Calendar = Calendar.getInstance(locale)
            calendar.timeInMillis = timeOfData * 1000
            name.text = TimeOfDay.getByTime(calendar.get(Calendar.HOUR_OF_DAY)).toString().toLowerCase()
            icon.setImageDrawable(IconicsDrawable(context)
                    .icon(WeatherIconsHelper.getWeatherIcon(weather[0].id, timeOfData))
                    .color(context.theme.getThemeColor(android.R.attr.textColorPrimary))
                    .sizeDp(32))
            temperature.text = "${Math.round(main.temperature)} ${UnitsUtils.getDegreesUnits(context)}"
            windIcon.setImageDrawable(IconicsDrawable(context)
                    .icon(WeatherIconsHelper.getDirectionalIcon(wind.degrees))
                    .color(context.theme.getThemeColor(android.R.attr.textColorPrimary))
                    .sizeDp(16))
            windInfo.text = "${wind.speed} ${UnitsUtils.getVelocityUnits(context)}"
        }
    }

}