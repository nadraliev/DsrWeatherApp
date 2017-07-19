package soutvoid.com.DsrWeatherApp.ui.screen.locationSettings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.agna.ferro.mvp.component.ScreenComponent
import kotlinx.android.synthetic.main.activity_location_settings.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_location_settings.view.*
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.ui.screen.locations.LocationsActivityView

class LocationSettingsActivityView: BaseActivityView() {

    companion object {
        const val NAME_KEY = "name"
        const val LATITUDE_KEY = "latitude"
        const val LONGITUDE_KEY = "longitude"
        fun start(context: Context, name: String, latitude: Double, longitude: Double) {
            val intent = Intent(context, LocationSettingsActivityView::class.java)
            intent.putExtra(NAME_KEY, name)
            intent.putExtra(LATITUDE_KEY, latitude)
            intent.putExtra(LONGITUDE_KEY, longitude)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: LocationSettingsActivityPresenter

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun getName(): String = "LocationSettings"

    override fun getContentView(): Int = R.layout.activity_location_settings

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerLocationSettingsActivityComponent.builder()
                .activityModule(activityModule)
                .appComponent(appComponent)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)

        initBackButton()
        fillInitData()
        initCheckButton()
    }

    private fun initBackButton() {
        location_settings_back_btn.setOnClickListener { onBackPressed() }
    }

    private fun fillInitData() {
        location_settings_name.setText(intent.getStringExtra(LocationSettingsActivityView.NAME_KEY))
    }

    private fun initCheckButton() {
        location_settings_check.setOnClickListener { presenter.checkPressed() }
    }

    fun getData(): SavedLocation {
        return SavedLocation(
                location_settings_name.text.toString(),
                intent.getDoubleExtra(LocationSettingsActivityView.LONGITUDE_KEY, .0),
                intent.getDoubleExtra(LocationSettingsActivityView.LATITUDE_KEY, .0),
                location_settings_favorite.isChecked,
                location_settings_show_forecast.isChecked
        )
    }

    fun returnToHome() {
        val intent = Intent(this, LocationsActivityView::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}