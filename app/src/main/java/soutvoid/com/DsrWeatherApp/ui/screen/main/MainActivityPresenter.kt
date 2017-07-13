package soutvoid.com.DsrWeatherApp.ui.screen.main

import com.agna.ferro.mvp.component.scope.PerScreen
import rx.Observable
import rx.functions.Action1
import rx.functions.Func3
import rx.schedulers.Schedulers
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.Forecast
import soutvoid.com.DsrWeatherApp.domain.ultraviolet.Ultraviolet
import soutvoid.com.DsrWeatherApp.interactor.currentWeather.CurrentWeatherRepository
import soutvoid.com.DsrWeatherApp.interactor.forecast.ForecastRepository
import soutvoid.com.DsrWeatherApp.interactor.util.ObservableUtil
import soutvoid.com.DsrWeatherApp.interactor.uvi.UviRepository
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.ui.screen.main.data.AllWeatherData
import javax.inject.Inject

@PerScreen
class MainActivityPresenter @Inject constructor(errorHandler: ErrorHandler) : BasePresenter<MainActivityView>(errorHandler) {

    @Inject
    lateinit var currentWeatherRep : CurrentWeatherRepository

    @Inject
    lateinit var forecastRep : ForecastRepository

    @Inject
    lateinit var uviRep : UviRepository


    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        loadData()
    }

    private fun loadData() {
        subscribeNetworkQuery(
                prepareObservable(),
                Action1 { view.fillAllData(it) }
        )
    }

    private fun prepareObservable() : Observable<AllWeatherData> {
        return ObservableUtil.combineLatestDelayError(
                Schedulers.io(),
                currentWeatherRep.getByCityName("Voronezh"),
                forecastRep.getByCityName("Voronezh"),
                uviRep.getByCoordinates(51.65,39.21),
                forecastRep.getDailyByCityName("Voronezh")
        ) { current, forecast, ultraviolet, dailyForecast -> AllWeatherData(current, forecast, ultraviolet, dailyForecast) }
    }
}