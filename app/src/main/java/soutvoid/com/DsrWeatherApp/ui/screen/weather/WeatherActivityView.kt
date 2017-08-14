package soutvoid.com.DsrWeatherApp.ui.screen.weather

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.agna.ferro.mvp.component.ScreenComponent
import com.mikepenz.iconics.IconicsDrawable
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.layout_current_weather.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.Forecast
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.screen.weather.data.AllWeatherData
import soutvoid.com.DsrWeatherApp.ui.screen.weather.widgets.forecastList.ForecastListAdapter
import soutvoid.com.DsrWeatherApp.ui.util.*
import javax.inject.Inject

/**
 * экран для отображения погоды в определенной точке
 */
class WeatherActivityView : BaseActivityView() {

    companion object {
        const val LOCATION_KEY = "location"
        const val LOCATION_ID_KEY = "location_id"

        /**
         * метод для старта активити
         * @param [savedLocation] для какой точки загружать погоду
         */
        fun start(context: Context, savedLocation: SavedLocation) {
            val intent = Intent(context, WeatherActivityView::class.java)
            intent.putExtra(LOCATION_KEY, savedLocation)
            context.startActivity(intent)
        }
        fun start(context: Context, locationId: Int) {
            val intent = Intent(context, WeatherActivityView::class.java)
            intent.putExtra(LOCATION_ID_KEY, locationId)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter : WeatherActivityPresenter

    private val forecastAdapter: ForecastListAdapter = ForecastListAdapter()

    private var messageSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)
        initSwipeRefresh()
        initBackButton()
    }

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun getName(): String = "WeatherActivity"

    override fun getContentView(): Int = R.layout.activity_weather

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerWeatherActivityComponent.builder()
                .activityModule(activityModule)
                .appComponent(appComponent)
                .build()
    }

    private fun initSwipeRefresh() {
        weather_refresh_layout.setOnRefreshListener { presenter.refresh() }
    }

    private fun initBackButton() {
        weather_back_btn.setOnClickListener { onBackPressed() }
    }

    fun maybeInitForecastList(showForecast: Boolean) {
        if (showForecast) {
            weather_forecast_list.adapter = forecastAdapter
            weather_forecast_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            weather_forecast_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        }
    }

    fun fillCityName(name: String) {
        weather_city_tv.text = name
    }

    fun fillAllData(allWeatherData: AllWeatherData, showForecast: Boolean) {
        fillCurrentWeatherData(allWeatherData.currentWeather)
        fillForecastData(allWeatherData.forecast)
        maybeFillNextDaysForecast(allWeatherData.forecast, showForecast)
    }

    fun fillCurrentWeatherData(currentWeather: CurrentWeather) {
        with(currentWeather) {
            val primaryTextColor = getThemeColor(android.R.attr.textColorPrimary)
            weather_date_tv.text = CalendarUtils.getFormattedDate(timeOfData)
            weather_temp_tv.text = "${Math.round(main.temperature)} ${UnitsUtils.getDegreesUnits(this@WeatherActivityView)}"
            weather_icon_iv.setImageDrawable(IconicsDrawable(this@WeatherActivityView)
                    .icon(WeatherIconsHelper.getWeatherIcon(weather.first().id, timeOfData))
                    .color(primaryTextColor)
                    .sizeDp(100))
            weather_description_tv.text = weather.first().description
            weather_wind_speed_tv.text = "${wind.speed} ${UnitsUtils.getVelocityUnits(this@WeatherActivityView)}"
            weather_wind_direction_tv.text = WindUtils.getFromByDegrees(wind.degrees, this@WeatherActivityView)
            weather_pressure_tv.text = " ${main.pressure} ${UnitsUtils.getPressureUnits(this@WeatherActivityView)}"
            weather_humidity_tv.text = " ${main.humidity}%"
        }

    }

    fun fillForecastData(forecast: Forecast) {
        weather_forecast.setWeather(forecast.list.filterIndexed { index, _ -> index % 2 == 0 }.take(4))
    }

    fun maybeFillNextDaysForecast(forecast: Forecast, showForecast: Boolean) {
        if (showForecast) {
            forecastAdapter.forecasts = forecast.list
            forecastAdapter.notifyDataSetChanged()
        }
    }

    fun setProgressBarEnabled(enabled: Boolean) {
        weather_refresh_layout.isRefreshing = enabled
    }

    fun getLocationParam(): SavedLocation? {
        if (intent.hasExtra(LOCATION_KEY))
            return intent.getSerializableExtra(WeatherActivityView.LOCATION_KEY) as SavedLocation
        return null
    }

    fun getCachedDataMessage(value: Int, isDays: Boolean): String {
        val noConnection = getString(R.string.no_internet_connection_error_message)
        val lastUpdate = getString(R.string.last_update)
        var time = ""
        if (isDays)
            time = "$value ${resources.getQuantityString(R.plurals.days, value)}"
        else
            time = "$value ${resources.getQuantityString(R.plurals.hours, value)}"
        val ago = getString(R.string.ago)
        return "$noConnection \n$lastUpdate $time $ago"
    }

    fun showIndefiniteMessage(snackbar: Snackbar) {
        messageSnackbar = snackbar
    }

    fun hideIndefiniteMessage() {
        messageSnackbar?.dismiss()
    }
}