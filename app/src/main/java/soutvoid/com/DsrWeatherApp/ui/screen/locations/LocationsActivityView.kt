package soutvoid.com.DsrWeatherApp.ui.screen.locations

import android.os.Bundle
import com.agna.ferro.mvp.component.ScreenComponent
import kotlinx.android.synthetic.main.activity_locations.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import javax.inject.Inject
import soutvoid.com.DsrWeatherApp.ui.screen.locations.pager.LocationsPagerAdapter

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
}