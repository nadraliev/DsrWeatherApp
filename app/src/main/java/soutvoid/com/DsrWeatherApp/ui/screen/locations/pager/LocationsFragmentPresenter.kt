package soutvoid.com.DsrWeatherApp.ui.screen.locations.pager

import com.agna.ferro.mvp.component.scope.PerScreen
import io.realm.Realm
import rx.Observable
import rx.functions.Action1
import rx.functions.FuncN
import rx.schedulers.Schedulers
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.interactor.currentWeather.CurrentWeatherRepository
import soutvoid.com.DsrWeatherApp.interactor.util.ObservableUtil
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.ui.common.error.StandardWithActionErrorHandler
import soutvoid.com.DsrWeatherApp.ui.common.message.MessagePresenter
import soutvoid.com.DsrWeatherApp.ui.screen.main.MainActivityView
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import javax.inject.Inject

@PerScreen
class LocationsFragmentPresenter @Inject constructor(errorHandler: ErrorHandler,
                                                     var messagePresenter: MessagePresenter)
    : BasePresenter<LocationsFragmentView>(errorHandler) {

    @Inject
    lateinit var currentWeatherRep: CurrentWeatherRepository

    override fun onResume() {
        super.onResume()

        view.setRefreshEnable(true)
        loadData()
    }

    /**
     * загрузить данные и вывести на экран
     */
    private fun loadData() {
        val locations = getSavedFromDB()
        if (locations.isNotEmpty()) {
            subscribeNetworkQuery(
                    prepareObservable(locations),
                    Action1 {
                        view.showData(locations, it)
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
            view.showData(locations, emptyList())
        }
    }

    /**
     * получить список сохраненных точек их базы данных
     */
    private fun getSavedFromDB() : List<SavedLocation> {
        val realm = Realm.getDefaultInstance()
        var locations : List<SavedLocation>
        if (view.isOnlyFavorite())
            locations = realm.copyFromRealm(realm.where(SavedLocation::class.java).equalTo("isFavorite", true).findAll())
        else locations = realm.copyFromRealm(realm.where(SavedLocation::class.java).findAll())
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

    /**
     * событие нажатия на элемент списка
     */
    fun onLocationClick(savedLocation: SavedLocation) {
        MainActivityView.start(view.context, savedLocation)
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
     * событие свайпа элемента списка
     */
    fun onLocationSwiped(location: SavedLocation) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.where(SavedLocation::class.java).equalTo("id", location.id).findAll().deleteAllFromRealm()
        }
        view.tryNotifyPagerDataSetChanged()
        realm.close()
    }

    /**
     * событие pull down to refresh
     */
    fun refresh() {
        loadData()
    }
}