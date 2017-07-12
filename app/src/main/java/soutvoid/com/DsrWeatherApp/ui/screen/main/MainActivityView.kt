package soutvoid.com.DsrWeatherApp.ui.screen.main

import android.content.res.Resources
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.agna.ferro.mvp.component.ScreenComponent
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.utils.IconicsUtils
import com.mikepenz.weather_icons_typeface_library.WeatherIcons
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.base.activity.TranslucentStatusActivityView
import soutvoid.com.DsrWeatherApp.ui.screen.main.data.AllWeatherData
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
        fillCurrentWeatherData(allWeatherData.currentWeather)
    }

    fun fillCurrentWeatherData(currentWeather: CurrentWeather) {
        cityTv.text = currentWeather.cityName
        temperatureTv.text = "${Math.round(currentWeather.main.temperature)} \u2103"
        val typedValue : TypedValue = TypedValue()
        val theme: Resources.Theme = theme
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)
        iconIv.setImageDrawable(IconicsDrawable(this).icon(WeatherIcons.Icon.wic_cloudy).color(typedValue.data).sizeDp(100))
    }
}