package soutvoid.com.DsrWeatherApp.ui.screen.locations.pager

import android.widget.Toast
import com.agna.ferro.mvp.component.scope.PerScreen
import io.realm.Realm
import rx.Observable
import rx.functions.Action1
import rx.functions.FuncN
import rx.schedulers.Schedulers
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.interactor.currentWeather.CurrentWeatherRepository
import soutvoid.com.DsrWeatherApp.interactor.util.ObservableUtil
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.ui.screen.main.MainActivityView
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import javax.inject.Inject

@PerScreen
class LocationsFragmentPresenter @Inject constructor(errorHandler: ErrorHandler)
    : BasePresenter<LocationsFragmentView>(errorHandler) {

    @Inject
    lateinit var currentWeatherRep: CurrentWeatherRepository

    override fun onResume() {
        super.onResume()

        view.setRefreshEnable(true)
        loadData()
    }

    private fun loadData() {
        val locations = getSavedFromDB()
        if (locations.isNotEmpty()) {
            subscribeNetworkQuery(
                    prepareObservable(locations),
                    Action1 {
                        view.showData(locations, it)
                        view.setRefreshEnable(false)
                    }
            )
        } else {
            view.setRefreshEnable(false)
        }
    }

    private fun getSavedFromDB() : List<SavedLocation> {
        val realm = Realm.getDefaultInstance()
        var locations : List<SavedLocation>
        if (view.isOnlyFavorite())
            locations = realm.copyFromRealm(realm.where(SavedLocation::class.java).equalTo("isFavorite", true).findAll())
        else locations = realm.copyFromRealm(realm.where(SavedLocation::class.java).findAll())
        realm.close()
        return locations
    }

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

    fun onLocationClick(savedLocation: SavedLocation) {
        MainActivityView.start(view.context, savedLocation)
    }

    fun onFavoriteStateChanged(location: SavedLocation, checked: Boolean) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            location.isFavorite = checked
            it.copyToRealmOrUpdate(location)
        }
        realm.close()
    }

    fun refresh() {
        loadData()
    }
}