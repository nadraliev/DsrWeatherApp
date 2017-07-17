package soutvoid.com.DsrWeatherApp.ui.screen.cities.pager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agna.ferro.mvp.component.ScreenComponent
import com.agna.ferro.mvp.presenter.MvpPresenter
import kotlinx.android.synthetic.main.fragment_locations.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.location.Location
import soutvoid.com.DsrWeatherApp.ui.base.fragment.BaseFragmentView
import soutvoid.com.DsrWeatherApp.ui.screen.cities.list.LocationsRecyclerAdapter
import soutvoid.com.DsrWeatherApp.ui.screen.map.MapActivityView
import soutvoid.com.DsrWeatherApp.ui.util.inflate
import javax.inject.Inject

class LocationsFragmentView : BaseFragmentView() {

    companion object {
        private const val ONLY_FAVORITE_KEY = "only_favorite"

        fun newInstance(showOnlyFavorite: Boolean = false) : LocationsFragmentView {
            val citiesFragment = LocationsFragmentView()
            citiesFragment.arguments = Bundle()
            citiesFragment.arguments.putBoolean(ONLY_FAVORITE_KEY, showOnlyFavorite)
            return citiesFragment
        }
    }

    @Inject
    lateinit var presenter: LocationsFragmentPresenter

    private lateinit var adapter: LocationsRecyclerAdapter

    override fun getName(): String {
        if (isOnlyFavorite())
            return "Favorite locations"
        else return "Locations"
    }

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerLocationsFragmentComponent.builder()
                .appComponent(getAppComponent())
                .fragmentModule(getFragmentModule())
                .build()
    }

    override fun getPresenter(): MvpPresenter<*> = presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = container?.inflate(R.layout.fragment_locations)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        initSwipeRefresh()
        initFab()
    }

    private fun initSwipeRefresh() {
        locations_swipe_refresh.setOnRefreshListener { presenter.refresh() }
    }

    fun setRefreshEnable(enabled: Boolean) {
        locations_swipe_refresh.isRefreshing = enabled
    }

    fun initFab() {
        locations_fab.setOnClickListener { MapActivityView.start(context) }
    }

    private fun initList() {
        adapter = LocationsRecyclerAdapter { presenter.onLocationClick(adapter.locations[it]) }
        locations_list.adapter = adapter
        locations_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    fun showData(locations: List<Location>, currentWeathers: List<CurrentWeather>) {
        adapter.locations = locations
        adapter.currentWeathers = currentWeathers
        adapter.notifyDataSetChanged()
    }

    fun isOnlyFavorite() : Boolean {
        return arguments.getBoolean(LocationsFragmentView.ONLY_FAVORITE_KEY)
    }
}