package soutvoid.com.DsrWeatherApp.ui.screen.editLocation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import com.agna.ferro.mvp.component.ScreenComponent
import kotlinx.android.synthetic.main.activity_edit_location.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.settings.LocationSettingsFragmentView
import soutvoid.com.DsrWeatherApp.ui.util.getDefaultPreferences
import javax.inject.Inject
import soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.settings.LocationSettingsFragmentView.Companion.NAME_KEY
import soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.settings.LocationSettingsFragmentView.Companion.FAVORITE_KEY
import soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.settings.LocationSettingsFragmentView.Companion.FORECAST_KEY
import soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.settings.LocationSettingsFragmentView.Companion.ID_KEY
import soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.settings.LocationSettingsFragmentView.Companion.LATITUDE_KEY
import soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.settings.LocationSettingsFragmentView.Companion.LONGITUDE_KEY

class EditLocationActivityView: BaseActivityView() {

    companion object {
        fun start(context: Context, name: String, isFavorite: Boolean, showForecast: Boolean,
                  id: Int, latitude: Float, longitude: Float) {
            val intent = Intent(context, EditLocationActivityView::class.java)
            intent.putExtra(NAME_KEY, name)
            intent.putExtra(FAVORITE_KEY, isFavorite)
            intent.putExtra(FORECAST_KEY, showForecast)
            intent.putExtra(ID_KEY, id)
            intent.putExtra(LATITUDE_KEY, latitude)
            intent.putExtra(LONGITUDE_KEY, longitude)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: EditLocationActivityPresenter

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun getContentView(): Int = R.layout.activity_edit_location

    override fun getName(): String = "Edit location"

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerEditLocationActivityComponent.builder()
                .activityModule(activityModule)
                .appComponent(appComponent)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)

        initToolbar()
        writeData()
        attachFragment()
    }

    private fun initToolbar() {
        setSupportActionBar(edit_location_toolbar)
        title = getString(R.string.edit_location)
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.themedBackDrawable, typedValue, true)
        edit_location_toolbar.navigationIcon = ContextCompat.getDrawable(this, typedValue.resourceId)
        edit_location_toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * сохранить данные в SharedPreferences, откуда их прочитает фрагмент с настройками
     */
    private fun writeData() {
        getDefaultPreferences().edit()
                .putString(NAME_KEY, intent.getStringExtra(NAME_KEY))
                .putBoolean(FAVORITE_KEY, intent.getBooleanExtra(FAVORITE_KEY, false))
                .putBoolean(FORECAST_KEY, intent.getBooleanExtra(FORECAST_KEY, false))
                .putInt(ID_KEY, intent.getIntExtra(ID_KEY, 0))
                .commit()
    }

    private fun attachFragment() {
        supportFragmentManager.beginTransaction().
                add(R.id.edit_location_container, LocationSettingsFragmentView.newInstance())
                .commit()
    }
}