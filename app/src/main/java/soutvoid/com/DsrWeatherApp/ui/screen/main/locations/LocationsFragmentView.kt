package soutvoid.com.DsrWeatherApp.ui.screen.main.locations

import android.content.Intent
import android.os.Bundle
import android.view.*
import com.agna.ferro.mvp.component.ScreenComponent
import kotlinx.android.synthetic.main.fragment_main_locations.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.base.fragment.BaseFragmentView
import javax.inject.Inject
import soutvoid.com.DsrWeatherApp.ui.screen.main.locations.pager.LocationsPagerAdapter
import soutvoid.com.DsrWeatherApp.ui.screen.settings.SettingsActivityView
import soutvoid.com.DsrWeatherApp.ui.util.inflate

class LocationsFragmentView : BaseFragmentView() {

    @Inject
    lateinit var presenter: LocationsFragmentPresenter

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerLocationsFragmentComponent.builder()
                .fragmentModule(getFragmentModule())
                .appComponent(getAppComponent())
                .build()
    }

    override fun getName(): String = "LocationsActivity"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_main_locations)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity.setTitle(R.string.locations)

        initPager()
    }

    private fun initPager() {
        locations_view_pager.adapter = LocationsPagerAdapter(childFragmentManager, activity)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.locations_toolbar_menu, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        item?.let { if (it.itemId == R.id.locations_settings) SettingsActivityView.startForResult(this, 0)}
//        return super.onOptionsItemSelected(item)
//    }
}