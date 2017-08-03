package soutvoid.com.DsrWeatherApp.ui.screen.main

import android.content.Intent
import android.os.Bundle
import com.agna.ferro.mvp.component.ScreenComponent
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.screen.main.locations.LocationsFragmentView
import soutvoid.com.DsrWeatherApp.ui.screen.settings.SettingsActivityView
import javax.inject.Inject

class MainActivityView: BaseActivityView() {

    @Inject
    lateinit var presenter: MainActivityPresenter

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun getContentView(): Int = R.layout.activity_main

    override fun getName(): String = "MainActivity"

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerMainActivityComponent.builder()
                .activityModule(activityModule)
                .appComponent(appComponent)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)
    }

    fun showLocationsFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.main_container, LocationsFragmentView()).commit()
    }

    fun showNotificationsFragment() {

    }

    fun showSettingsFragment() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            if (it.getBooleanExtra(SettingsActivityView.RESTART_REQUIRED, false)) {
                val intent = Intent(this, LocationsFragmentView::class.java)
                finish()
                startActivity(intent)
            }
        }
    }
}