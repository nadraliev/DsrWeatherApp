package soutvoid.com.DsrWeatherApp.ui.screen.main.locations.pager

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agna.ferro.mvp.component.ScreenComponent
import com.agna.ferro.mvp.presenter.MvpPresenter
import kotlinx.android.synthetic.main.fragment_locations.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.ui.base.fragment.BaseFragmentView
import soutvoid.com.DsrWeatherApp.ui.screen.editLocation.EditLocationActivityView
import soutvoid.com.DsrWeatherApp.ui.screen.main.locations.list.LocationsRecyclerAdapter
import soutvoid.com.DsrWeatherApp.ui.screen.main.locations.pager.data.LocationWithWeather
import soutvoid.com.DsrWeatherApp.ui.screen.newLocation.NewLocationActivityView
import soutvoid.com.DsrWeatherApp.ui.util.SimpleItemSwipeCallback
import soutvoid.com.DsrWeatherApp.ui.util.inflate
import javax.inject.Inject

class LocationsListFragmentView : BaseFragmentView() {

    companion object {
        private const val ONLY_FAVORITE_KEY = "only_favorite"

        /**
         * исползовать этот метод для получения объекта фрагмента
         * @param [showOnlyFavorite] показывать ли в списке только любимые точки или все
         */
        fun newInstance(showOnlyFavorite: Boolean = false) : LocationsListFragmentView {
            val citiesFragment = LocationsListFragmentView()
            citiesFragment.arguments = Bundle()
            citiesFragment.arguments.putBoolean(ONLY_FAVORITE_KEY, showOnlyFavorite)
            return citiesFragment
        }
    }

    @Inject
    lateinit var presenterList: LocationsListFragmentPresenter

    private lateinit var adapter: LocationsRecyclerAdapter

    override fun getName(): String {
        if (isOnlyFavorite())
            return "Favorite locations"
        else return "Locations"
    }

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerLocationsListFragmentComponent.builder()
                .appComponent(getAppComponent())
                .fragmentModule(getFragmentModule())
                .build()
    }

    override fun getPresenter(): MvpPresenter<*> = presenterList

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
        locations_swipe_refresh.setOnRefreshListener { presenterList.refresh() }
    }

    private fun initFab() {
        if (isOnlyFavorite())
            locations_fab.visibility = View.GONE    //убрать кнопку добавления точки в списке с только любимыми
        else
            locations_fab.setOnClickListener { NewLocationActivityView.start(context) }
    }

    private fun initList() {
        adapter = LocationsRecyclerAdapter(
                onClick = { presenterList.onLocationClick(adapter.locations[it].location) },
                favoriteStateChangedListener = {
                    position, state ->
                    presenterList.onFavoriteStateChanged(adapter.locations[position].location, state)
                },
                editBtnClickedListener = { presenterList.onEditClicked(adapter.locations[it], it) },
                removeBtnClickedListener = { locationRemoveRequest(it) }
        )
        locations_list.adapter = adapter
        locations_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val simpleItemTouchCallback = SimpleItemSwipeCallback(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                {viewHolder, direction -> locationRemoveRequest(viewHolder.adapterPosition)})
        ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(locations_list)
    }

    private fun locationRemoveRequest(position: Int) {
        presenterList.onLocationRemoveRequested(adapter.locations[position], position)
        adapter.locations.removeAt(position)
        adapter.notifyItemRemoved(position)
    }


    fun setRefreshEnable(enabled: Boolean) {
        locations_swipe_refresh.isRefreshing = enabled
    }

    fun showData(locations: List<LocationWithWeather>) {
        adapter.locations = locations.toMutableList()
        adapter.notifyDataSetChanged()
    }

    fun isOnlyFavorite() : Boolean {
        return arguments.getBoolean(ONLY_FAVORITE_KEY)
    }

    /**
     * оповестить родительский viewpager, что данные в списке изменились, чтобы другой список обновился
     * @see LocationsPagerAdapter.notifyOtherFragmentDataSetChanged
     */
    fun tryNotifyPagerDataSetChanged() {
        val pager = activity.findViewById<ViewPager>(R.id.locations_view_pager)
        val adapter = pager?.adapter as? LocationsPagerAdapter
        adapter?.notifyOtherFragmentDataSetChanged(pager.currentItem)
    }

    /**
     * метод для сообщения фрагменту, что данные изменились (от родительского viewpager)
     */
    fun onDataSetChanged() {
        try {
            presenterList.refresh()
        } catch (e: UninitializedPropertyAccessException) {
            //на случай, если lateinit presenter еще не был инициализирован
        }
    }

    fun addLocationToPosition(location: LocationWithWeather, position: Int) {
        adapter.locations.add(position, location)
        adapter.notifyItemInserted(position)
    }

    fun openEditLocationScreen(location: SavedLocation) {
        EditLocationActivityView.start(context, location.name, location.isFavorite, location.showForecast,
                location.id, location.latitude.toFloat(), location.longitude.toFloat())
    }

    fun showMessageEmpty() {
        locations_placeholder.show(getEmptyMessage(), getEmptyImageId())
    }

    fun hidePlaceholder() {
        locations_placeholder.hide()
    }

    fun getEmptyMessage(): String {
        if (isOnlyFavorite())
            return getString(R.string.no_favorites_message)
        else return getString(R.string.no_locations_message)
    }

    fun getEmptyImageId(): Int {
        val typedValue = TypedValue()
        if (isOnlyFavorite()) {
            context.theme.resolveAttribute(R.attr.themedNoFavoritesDrawable, typedValue, true)
        } else {
            context.theme.resolveAttribute(R.attr.themedEmptyDrawable, typedValue, true)
        }
        return typedValue.resourceId
    }
}