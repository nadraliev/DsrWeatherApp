package soutvoid.com.DsrWeatherApp.ui.screen.main

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.agna.ferro.mvp.component.ScreenComponent
import com.mikepenz.iconics.IconicsDrawable
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.ultraviolet.Ultraviolet
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.base.activity.TranslucentStatusActivityView
import soutvoid.com.DsrWeatherApp.ui.screen.main.data.AllWeatherData
import soutvoid.com.DsrWeatherApp.ui.util.WeatherIconsHelper
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

    @BindView(R.id.main_pressure_tv)
    lateinit var pressureTv: TextView

    @BindView(R.id.main_humidity_tv)
    lateinit var humidityTv: TextView

    @BindView(R.id.main_wind_tv)
    lateinit var windTv: TextView
    @BindView(R.id.main_wind_icon)
    lateinit var windIcon: ImageView

    @BindView(R.id.main_uv_tv)
    lateinit var uvTv: TextView

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)
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

    fun fillAllData(allWeatherData: AllWeatherData) {
        fillCurrentWeatherData(allWeatherData.currentWeather, allWeatherData.ultraviolet)
    }

    fun fillCurrentWeatherData(currentWeather: CurrentWeather, ultraviolet: Ultraviolet) {
        with(currentWeather) {
            val primaryTextColor = getThemeColor(android.R.attr.textColorPrimary)
            cityTv.text = cityName
            temperatureTv.text = "${Math.round(main.temperature)} \u2103"
            iconIv.setImageDrawable(IconicsDrawable(this@MainActivityView)
                    .icon(WeatherIconsHelper.getWeatherIcon(weather.first().id, timeOfData, sys.sunrise, sys.sunset))
                    .color(primaryTextColor)
                    .sizeDp(100))
            descriptionTv.text = weather.first().description
            pressureTv.text = Math.round(main.pressure).toString()
            humidityTv.text = main.humidity.toString()
            windTv.text = wind.speed.toString()
            windIcon.setImageDrawable(
                    IconicsDrawable(this@MainActivityView)
                            .icon(WeatherIconsHelper.getDirectionalIcon(wind.degrees))
                            .color(primaryTextColor)
                            .sizeDp(45))
            uvTv.text = ultraviolet.value.toString()
        }

    }

    fun getThemeColor(attr: Int) : Int {
        val typedValue : TypedValue = TypedValue()
        val theme: Resources.Theme = theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }
}