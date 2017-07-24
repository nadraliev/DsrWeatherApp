package soutvoid.com.DsrWeatherApp.ui.screen.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.agna.ferro.mvp.component.ScreenComponent
import com.mikepenz.iconics.IconicsDrawable
import kotlinx.android.synthetic.main.activity_main.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.Forecast
import soutvoid.com.DsrWeatherApp.domain.ultraviolet.Ultraviolet
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.base.activity.TranslucentStatusActivityView
import soutvoid.com.DsrWeatherApp.ui.screen.main.data.AllWeatherData
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import soutvoid.com.DsrWeatherApp.ui.util.WeatherIconsHelper
import soutvoid.com.DsrWeatherApp.ui.util.WindUtils
import soutvoid.com.DsrWeatherApp.ui.util.getThemeColor
import javax.inject.Inject

import kotlinx.android.synthetic.main.layout_current_weather.*
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.screen.main.widgets.forecastList.ForecastListAdapter
import soutvoid.com.DsrWeatherApp.ui.screen.settings.SettingsActivityView

/**
 * экран для отображения погоды в определенной точке
 */
class MainActivityView : BaseActivityView() {

    companion object {
        const val LOCATION_KEY = "location"

        /**
         * метод для старта активити
         * @param [savedLocation] для какой точки загружать погоду
         */
        fun start(context: Context, savedLocation: SavedLocation) {
            val intent = Intent(context, MainActivityView::class.java)
            intent.putExtra(LOCATION_KEY, savedLocation)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter : MainActivityPresenter

    private val forecastAdapter: ForecastListAdapter = ForecastListAdapter()

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)
        initSwipeRefresh()
        initSettingsButton()
        initBackButton()
        fillCityName()
        maybeInitForecastList()
    }

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun getName(): String = "MainActivity"

    override fun getContentView(): Int = R.layout.activity_main

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerMainActivityComponent.builder()
                .activityModule(activityModule)
                .appComponent(appComponent)
                .build()
    }

    private fun initSwipeRefresh() {
        main_refresh_layout.setOnRefreshListener { presenter.refresh() }
    }

    private fun initSettingsButton() {
        main_settings_btn.setOnClickListener { SettingsActivityView.start(this) }
    }

    private fun initBackButton() {
        main_back_btn.setOnClickListener { onBackPressed() }
    }

    private fun maybeInitForecastList() {
        if (getLocationParam().showForecast) {
            main_forecast_list.adapter = forecastAdapter
            main_forecast_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            main_forecast_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        }
    }

    private fun fillCityName() {
        main_city_tv.text = getLocationParam().name
    }

    fun fillAllData(allWeatherData: AllWeatherData) {
        fillCurrentWeatherData(allWeatherData.currentWeather)
        fillForecastData(allWeatherData.forecast)
        maybeFillNextDaysForecast(allWeatherData.forecast)
    }

    fun fillCurrentWeatherData(currentWeather: CurrentWeather) {
        with(currentWeather) {
            val primaryTextColor = theme.getThemeColor(android.R.attr.textColorPrimary)
            main_temp_tv.text = "${Math.round(main.temperature)} ${UnitsUtils.getDegreesUnits(this@MainActivityView)}"
            main_icon_iv.setImageDrawable(IconicsDrawable(this@MainActivityView)
                    .icon(WeatherIconsHelper.getWeatherIcon(weather.first().id, timeOfData, sys.sunrise, sys.sunset))
                    .color(primaryTextColor)
                    .sizeDp(100))
            main_description_tv.text = weather.first().description
            main_wind_speed_tv.text = "${wind.speed} ${UnitsUtils.getVelocityUnits(this@MainActivityView)}"
            main_wind_direction_tv.text = WindUtils.getByDegrees(wind.degrees, this@MainActivityView)
            main_pressure_tv.text = ": ${main.pressure} ${UnitsUtils.getPressureUnits(this@MainActivityView)}"
            main_humidity_tv.text = ": ${main.humidity}%"
        }

    }

    fun fillForecastData(forecast: Forecast) {
        main_forecast.setWeather(forecast.list.filterIndexed { index, _ -> index % 2 == 0 }.take(4))
    }

    fun maybeFillNextDaysForecast(forecast: Forecast) {
        if (getLocationParam().showForecast) {
            forecastAdapter.forecasts = forecast.list
            forecastAdapter.notifyDataSetChanged()
        }
    }

    fun setProgressBarEnabled(enabled: Boolean) {
        main_refresh_layout.isRefreshing = enabled
    }

    fun getLocationParam(): SavedLocation {
        return intent.getSerializableExtra(MainActivityView.LOCATION_KEY) as SavedLocation
    }
}