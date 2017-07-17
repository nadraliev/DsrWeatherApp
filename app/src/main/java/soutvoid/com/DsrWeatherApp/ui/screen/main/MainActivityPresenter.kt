package soutvoid.com.DsrWeatherApp.ui.screen.main

import com.agna.ferro.mvp.component.scope.PerScreen
import rx.Observable
import rx.functions.Action1
import rx.functions.Func3
import rx.schedulers.Schedulers
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.Forecast
import soutvoid.com.DsrWeatherApp.domain.location.Location
import soutvoid.com.DsrWeatherApp.domain.ultraviolet.Ultraviolet
import soutvoid.com.DsrWeatherApp.interactor.currentWeather.CurrentWeatherRepository
import soutvoid.com.DsrWeatherApp.interactor.forecast.ForecastRepository
import soutvoid.com.DsrWeatherApp.interactor.util.ObservableUtil
import soutvoid.com.DsrWeatherApp.interactor.util.Units
import soutvoid.com.DsrWeatherApp.interactor.uvi.UviRepository
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.ui.screen.main.data.AllWeatherData
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import soutvoid.com.DsrWeatherApp.ui.util.getDefaultPreferences
import javax.inject.Inject

@PerScreen
class MainActivityPresenter @Inject constructor(errorHandler: ErrorHandler) : BasePresenter<MainActivityView>(errorHandler) {

    @Inject
    lateinit var currentWeatherRep : CurrentWeatherRepository

    @Inject
    lateinit var forecastRep : ForecastRepository

    @Inject
    lateinit var uviRep : UviRepository

    lateinit var location: Location


    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        location = view.getLocationParam()
        view.setProgressBarEnabled(true)
        loadData()
    }

    private fun loadData() {
        subscribeNetworkQuery(
                prepareObservable(),
                Action1 {
                    view.fillAllData(it)
                    view.setProgressBarEnabled(false)
                }
        )
    }

    private fun prepareObservable() : Observable<AllWeatherData> {
        val units = UnitsUtils.getPreferredUnits(view.baseContext)
        return ObservableUtil.combineLatestDelayError(
                Schedulers.io(),
                currentWeatherRep.getByCoordinates(location.latitude, location.longitude, units),
                forecastRep.getByCoordinates(location.latitude, location.longitude, units),
                uviRep.getByCoordinates(location.latitude, location.longitude, units),
                forecastRep.getDailyByCoordinates(location.latitude, location.longitude, units)
        ) { current, forecast, ultraviolet, dailyForecast -> AllWeatherData(current, forecast, ultraviolet, dailyForecast) }
    }

    fun refresh() {
        loadData()
    }
}