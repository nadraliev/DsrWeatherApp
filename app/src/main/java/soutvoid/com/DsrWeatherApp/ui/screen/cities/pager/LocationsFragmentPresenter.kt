package soutvoid.com.DsrWeatherApp.ui.screen.cities.pager

import com.agna.ferro.mvp.component.scope.PerScreen
import rx.functions.Action1
import rx.functions.FuncN
import rx.schedulers.Schedulers
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.location.Location
import soutvoid.com.DsrWeatherApp.interactor.currentWeather.CurrentWeatherRepository
import soutvoid.com.DsrWeatherApp.interactor.util.ObservableUtil
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import javax.inject.Inject

@PerScreen
class LocationsFragmentPresenter @Inject constructor(errorHandler: ErrorHandler)
    : BasePresenter<LocationsFragmentView>(errorHandler) {

    @Inject
    lateinit var currentWeatherRep: CurrentWeatherRepository

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        loadData()
    }

    private fun loadData() {
        val locations = arrayListOf(Location("Voronezh", 37.40, 51.00)) //Todo load from database
        val weathers = locations
                .map { currentWeatherRep.getByCoordinates(
                        it.latitude,
                        it.longitude,
                        UnitsUtils.getPreferredUnits(view.context))
                }.toList()
        val combinedObservable = ObservableUtil.combineLatestDelayError(
                Schedulers.io(),
                weathers,
                FuncN { it.map { it as CurrentWeather }.toList() }
        )
        subscribeNetworkQuery(
                combinedObservable,
                Action1 { view.showData(locations, it) }
        )
    }

    fun onLocationClick(location: Location) {
        //TODO launch weather activity
    }
}