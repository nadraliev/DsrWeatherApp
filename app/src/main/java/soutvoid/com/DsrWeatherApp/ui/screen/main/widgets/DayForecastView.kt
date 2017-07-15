package soutvoid.com.DsrWeatherApp.ui.screen.main.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.ThreeHoursForecast
import soutvoid.com.DsrWeatherApp.ui.screen.main.data.TimeOfDay
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils

class DayForecastView : FrameLayout {

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

    fun setWeather(forecasts: List<ThreeHoursForecast>) {
        morning.setWeather(forecasts[0])
        day.setWeather(forecasts[1])
        evening.setWeather(forecasts[2])
        night.setWeather(forecasts[3])
    }

}