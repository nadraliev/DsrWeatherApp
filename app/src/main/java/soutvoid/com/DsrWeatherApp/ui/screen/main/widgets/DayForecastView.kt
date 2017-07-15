package soutvoid.com.DsrWeatherApp.ui.screen.main.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.view_day_forecast.view.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.ThreeHoursForecast

class DayForecastView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)


    init {
        View.inflate(context, R.layout.view_day_forecast, this)
        ButterKnife.bind(this)
    }

    fun setWeather(forecasts: List<ThreeHoursForecast>) {
        view_day_forecast_morning.setWeather(forecasts[0])
        view_day_forecast_day.setWeather(forecasts[1])
        view_day_forecast_evening.setWeather(forecasts[2])
        view_day_forecast_night.setWeather(forecasts[3])
    }

}