package soutvoid.com.DsrWeatherApp.ui.screen.locations

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.agna.ferro.mvp.component.ScreenComponent
import kotlinx.android.synthetic.main.activity_locations.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import javax.inject.Inject
import soutvoid.com.DsrWeatherApp.ui.screen.locations.pager.LocationsPagerAdapter
import soutvoid.com.DsrWeatherApp.ui.screen.settings.SettingsActivityView

class LocationsActivityView : BaseActivityView() {

    @Inject
    lateinit var presenter: LocationsActivityPresenter

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerLocationsActivityComponent.builder()
                .activityModule(activityModule)
                .appComponent(appComponent)
                .build()
    }

    override fun getContentView(): Int = R.layout.activity_locations

    override fun getName(): String = "LocationsActivity"

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)

        setSupportActionBar(locations_toolbar)
        setTitle(R.string.locations)

        initPager()
    }

    private fun initPager() {
        locations_view_pager.adapter = LocationsPagerAdapter(supportFragmentManager, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.locations_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let { if (it.itemId == R.id.locations_settings) SettingsActivityView.start(this)}
        return super.onOptionsItemSelected(item)
    }
}