package soutvoid.com.DsrWeatherApp.ui.screen.main.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.OneDayForecast
import soutvoid.com.DsrWeatherApp.domain.OneMomentForecast
import soutvoid.com.DsrWeatherApp.ui.screen.main.data.TimeOfDay
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import soutvoid.com.DsrWeatherApp.ui.util.getTimeOfDayForecast

class DayForecastView : FrameLayout {

    @BindView(R.id.view_day_forecast_humidity_tv)
    lateinit var humidity: TextView
    @BindView(R.id.view_day_forecast_pressure_tv)
    lateinit var pressure: TextView
    @BindView(R.id.view_day_forecast_morning)
    lateinit var morning: TimeOfDayWeatherView
    @BindView(R.id.view_day_forecast_day)
    lateinit var day: TimeOfDayWeatherView
    @BindView(R.id.view_day_forecast_evening)
    lateinit var evening: TimeOfDayWeatherView
    @BindView(R.id.view_day_forecast_night)
    lateinit var night: TimeOfDayWeatherView

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)


    init {
        View.inflate(context, R.layout.view_day_forecast, this)
        ButterKnife.bind(this)
    }

    fun setWeather(oneDayForecast: OneDayForecast) {
        humidity.text = "${oneDayForecast.humidity}%"
        pressure.text = "${oneDayForecast.pressure} ${UnitsUtils.getPressureUnits(context)}"
        morning.setWeather(oneDayForecast.detailedForecasts.getTimeOfDayForecast(TimeOfDay.MORNING))
        day.setWeather(oneDayForecast.detailedForecasts.getTimeOfDayForecast(TimeOfDay.DAY))
        evening.setWeather(oneDayForecast.detailedForecasts.getTimeOfDayForecast(TimeOfDay.EVENING))
        night.setWeather(oneDayForecast.detailedForecasts.getTimeOfDayForecast(TimeOfDay.NIGHT))
    }

}