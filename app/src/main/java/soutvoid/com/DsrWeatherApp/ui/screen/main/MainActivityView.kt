package soutvoid.com.DsrWeatherApp.ui.screen.main

import android.content.res.Resources
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.LayoutDirection
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.agna.ferro.mvp.component.ScreenComponent
import com.mikepenz.iconics.IconicsDrawable
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.DailyForecast
import soutvoid.com.DsrWeatherApp.domain.ultraviolet.Ultraviolet
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.base.activity.TranslucentStatusActivityView
import soutvoid.com.DsrWeatherApp.ui.screen.main.data.AllWeatherData
import soutvoid.com.DsrWeatherApp.ui.screen.main.list.ForecastAdapter
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import soutvoid.com.DsrWeatherApp.ui.util.WeatherIconsHelper
import soutvoid.com.DsrWeatherApp.ui.util.getThemeColor
import javax.inject.Inject

class MainActivityView : TranslucentStatusActivityView() {

    @Inject
    lateinit var presenter : MainActivityPresenter

    @BindView(R.id.main_city_tv)
    lateinit var cityTv: TextView

    @BindView(R.id.main_temp_tv)
    lateinit var temperatureTv: TextView

    @BindView(R.id.main_icon_iv)
    lateinit var iconIv: ImageView

    @BindView(R.id.main_description_tv)
    lateinit var descriptionTv: TextView

    @BindView(R.id.main_forecast_list)
    lateinit var forecastList: RecyclerView

    val forecastAdapter: ForecastAdapter = ForecastAdapter()

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)
        initList()
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

    private fun initList() {
        forecastList.adapter = forecastAdapter
        forecastList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        forecastList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    fun fillAllData(allWeatherData: AllWeatherData) {
        fillCurrentWeatherData(allWeatherData.currentWeather)
        fillForecastData(allWeatherData.dailyForecast)
    }

    fun fillCurrentWeatherData(currentWeather: CurrentWeather) {
        with(currentWeather) {
            val primaryTextColor = theme.getThemeColor(android.R.attr.textColorPrimary)
            cityTv.text = cityName
            temperatureTv.text = "${Math.round(main.temperature)} ${UnitsUtils.getDegreesUnits(this@MainActivityView)}"
            iconIv.setImageDrawable(IconicsDrawable(this@MainActivityView)
                    .icon(WeatherIconsHelper.getWeatherIcon(weather.first().id, timeOfData, sys.sunrise, sys.sunset))
                    .color(primaryTextColor)
                    .sizeDp(100))
            descriptionTv.text = weather.first().description
        }

    }

    fun fillForecastData(dailyForecast: DailyForecast) {
        forecastAdapter.dailyForecasts = dailyForecast.forecasts
        forecastAdapter.notifyDataSetChanged()
    }
}