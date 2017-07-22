package soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agna.ferro.mvp.component.ScreenComponent
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_location_settings.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import javax.inject.Inject
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.ui.base.fragment.BaseFragmentView
import soutvoid.com.DsrWeatherApp.ui.screen.locations.LocationsActivityView
import soutvoid.com.DsrWeatherApp.ui.util.getDefaultPreferences

class LocationSettingsFragmentView : BaseFragmentView(), Step {

    companion object {
        const val NAME_KEY = "name"
        const val LATITUDE_KEY = "latitude"
        const val LONGITUDE_KEY = "longitude"
        fun newInstance(): LocationSettingsFragmentView {
            val locationSettingsView = LocationSettingsFragmentView()
            return locationSettingsView
        }
    }

    @Inject
    lateinit var presenter: LocationSettingsFragmentPresenter

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun getName(): String = "LocationSettings"

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerLocationSettingsFragmentComponent.builder()
                .appComponent(getAppComponent())
                .fragmentModule(getFragmentModule())
                .build()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_location_settings, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCheckButton()
    }

    private fun fillInitData() {
        location_settings_name.setText(context.getDefaultPreferences().getString(NAME_KEY, ""))
    }

    private fun initCheckButton() {
        location_settings_check.setOnClickListener { presenter.checkPressed() }
    }

    override fun onSelected() {
        fillInitData()
    }

    override fun verifyStep(): VerificationError? {
        presenter.checkPressed()
        return null
    }

    override fun onError(error: VerificationError) {
    }

    fun getData(): SavedLocation {
        return SavedLocation(
                location_settings_name.text.toString(),
                context.getDefaultPreferences().getFloat(LONGITUDE_KEY, 0f).toDouble(),
                context.getDefaultPreferences().getFloat(LATITUDE_KEY, 0f).toDouble(),
                location_settings_favorite.isChecked,
                location_settings_show_forecast.isChecked
        )
    }

    fun returnToHome() {
        val intent = Intent(activity, LocationsActivityView::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}