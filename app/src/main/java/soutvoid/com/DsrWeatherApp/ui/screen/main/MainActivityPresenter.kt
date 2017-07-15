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
import soutvoid.com.DsrWeatherApp.interactor.util.Units
import soutvoid.com.DsrWeatherApp.interactor.uvi.UviRepository
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.ui.screen.main.data.AllWeatherData
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import soutvoid.com.DsrWeatherApp.ui.util.fillDetails
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


    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        view.setProgressBarEnabled(true)
        loadData()
    }

    private fun loadData() {
        subscribeNetworkQuery(
                prepareObservable(),
                Action1 {
                    it.dailyForecast.forecasts.fillDetails(it.forecast.list)
                    it.dailyForecast.forecasts = it.dailyForecast.forecasts.filter { it.detailedForecasts.size > 0 }
                    view.fillAllData(it)
                    view.setProgressBarEnabled(false)
                }
        )
    }

    private fun prepareObservable() : Observable<AllWeatherData> {
        val isMetrical = UnitsUtils.isMetricalPreferred(view.baseContext)
        val units = if (isMetrical) Units.METRIC else Units.IMPERIAL
        return ObservableUtil.combineLatestDelayError(
                Schedulers.io(),
                currentWeatherRep.getByCityName("Voronezh", units),
                forecastRep.getByCityName("Voronezh", units),
                uviRep.getByCoordinates(51.65,39.21, units),
                forecastRep.getDailyByCityName("Voronezh", units)
        ) { current, forecast, ultraviolet, dailyForecast -> AllWeatherData(current, forecast, ultraviolet, dailyForecast) }
    }

    fun refresh() {
        loadData()
    }
}