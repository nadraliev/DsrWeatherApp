package soutvoid.com.DsrWeatherApp.ui.screen.main.locations.pager

import com.agna.ferro.mvp.component.scope.PerScreen
import io.realm.Realm
import rx.Observable
import rx.functions.Action1
import rx.functions.FuncN
import rx.schedulers.Schedulers
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.interactor.currentWeather.CurrentWeatherRepository
import soutvoid.com.DsrWeatherApp.interactor.util.ObservableUtil
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.ui.common.error.StandardWithActionErrorHandler
import soutvoid.com.DsrWeatherApp.ui.common.message.MessagePresenter
import soutvoid.com.DsrWeatherApp.ui.screen.main.locations.pager.data.LocationWithWeather
import soutvoid.com.DsrWeatherApp.ui.screen.weather.WeatherActivityView
import soutvoid.com.DsrWeatherApp.ui.util.SnackbarDismissedListener
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import javax.inject.Inject

@PerScreen
class LocationsListFragmentPresenter @Inject constructor(errorHandler: ErrorHandler,
                                                         var messagePresenter: MessagePresenter)
    : BasePresenter<LocationsListFragmentView>(errorHandler) {

    @Inject
    lateinit var currentWeatherRep: CurrentWeatherRepository

    private var undoClicked = false
    private var removeCandidate: SavedLocation? = null

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        view.setRefreshEnable(true)
        loadData()
    }

    /**
     * загрузить данные и вывести на экран
     */
    private fun loadData() {
        Observable.just(getSavedFromDB())
                .subscribeOn(Schedulers.io())
                .subscribe { locations ->
                    if (locations.isNotEmpty()) {
                        subscribeNetworkQuery(
                                prepareObservable(locations),
                                Action1 {
                                    view.hidePlaceholder()
                                    view.showData(locations.zip(it)
                                    {a: SavedLocation, b: CurrentWeather -> LocationWithWeather(a, b) })
                                    view.setRefreshEnable(false)
                                },
                                Action1 { view.setRefreshEnable(false) },
                                StandardWithActionErrorHandler(
                                        messagePresenter,
                                        view.getString(R.string.try_again))
                                { loadData() }
                        )
                    } else {
                        view.setRefreshEnable(false)
                        view.showData(emptyList())
                        view.showMessageEmpty()
                    }
                }
    }

    /**
     * получить список сохраненных точек их базы данных
     */
    private fun getSavedFromDB() : List<SavedLocation> {
        val realm = Realm.getDefaultInstance()
        val locations : List<SavedLocation>
        if (view.isOnlyFavorite())
            locations = realm.copyFromRealm(realm.where(SavedLocation::class.java).equalTo("isFavorite", true).findAllSorted("id"))
        else locations = realm.copyFromRealm(realm.where(SavedLocation::class.java).findAllSorted("id"))
        realm.close()
        return locations
    }

    /**
     * подготовить observable для погод для всех точек, после этого объединить в один observable
     * @param [locations] список точек
     * @return observable для загрузки погоды для всех точек
     */
    private fun prepareObservable(locations: List<SavedLocation>) : Observable<List<CurrentWeather>> {
        val weathers = locations
                .map {
                    currentWeatherRep.getByCoordinates(
                            it.latitude,
                            it.longitude,
                            UnitsUtils.getPreferredUnits(view.context))
                }.toList()
        val combinedObservable = ObservableUtil.combineLatestDelayError(
                Schedulers.io(),
                weathers,
                FuncN { it.map { it as CurrentWeather }.toList() }
        )
        return combinedObservable
    }

    private fun removeLocationFromDb(location: SavedLocation) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.where(SavedTrigger::class.java).equalTo("location.id", location.id).findAll().deleteAllFromRealm()
            it.where(SavedLocation::class.java).equalTo("id", location.id).findAll().deleteAllFromRealm()
        }
        realm.close()
        view?.tryNotifyPagerDataSetChanged()
    }

    private fun onUndoClicked(locationWithWeather: LocationWithWeather, position: Int) {
        view.addLocationToPosition(locationWithWeather, position)
        undoClicked = true
    }

    /**
     * событие нажатия на элемент списка
     */
    fun onLocationClick(savedLocation: SavedLocation) {
        WeatherActivityView.start(view.context, savedLocation)
    }

    /**
     * событие нажатия на кнопку "сердце"
     */
    fun onFavoriteStateChanged(location: SavedLocation, checked: Boolean) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            location.isFavorite = checked
            it.copyToRealmOrUpdate(location)
        }
        realm.close()
        view.tryNotifyPagerDataSetChanged()
        if (view.isOnlyFavorite() && !checked)
            loadData()
    }

    /**
     * пользователь нажал кнопку "удплить" или свайпнул по месту
     */
    fun onLocationRemoveRequested(locationWithWeather: LocationWithWeather, position: Int) {
        removeCandidate = locationWithWeather.location
        messagePresenter.showWithAction(R.string.location_removed, R.string.undo,
                { onUndoClicked(locationWithWeather, position)})  //undo deleting
                .addCallback(SnackbarDismissedListener { _, _ ->
                    if (!undoClicked)
                        removeLocationFromDb(locationWithWeather.location)
                    else undoClicked = false
                })  //delete from db when snackbar disappears
    }

    fun onEditClicked(locationWithWeather: LocationWithWeather, position: Int) {
        view.openEditLocationScreen(locationWithWeather.location)
    }

    /**
     * событие pull down to refresh
     */
    fun refresh() {
        loadData()
    }

    override fun onPause() {
        removeCandidate?.let { removeLocationFromDb(it) }
        super.onPause()
    }
}