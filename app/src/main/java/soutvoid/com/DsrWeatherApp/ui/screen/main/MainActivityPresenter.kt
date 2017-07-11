package soutvoid.com.DsrWeatherApp.ui.screen.main

import com.agna.ferro.mvp.component.scope.PerScreen
import rx.functions.Action1
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.domain.Forecast
import soutvoid.com.DsrWeatherApp.interactor.currentWeather.CurrentWeatherRepository
import soutvoid.com.DsrWeatherApp.interactor.forecast.ForecastRepository
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import javax.inject.Inject

@PerScreen
class MainActivityPresenter @Inject constructor(errorHandler: ErrorHandler) : BasePresenter<MainActivityView>(errorHandler) {

    @Inject
    lateinit var currentWeatherRep : CurrentWeatherRepository

    @Inject
    lateinit var forecastRep : ForecastRepository


    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        subscribeNetworkQuery(
                forecastRep.getByCityName("Voronezh"),
                Action1 {response : Forecast -> view.testTv.text = response.toString() },
                Action1 { e ->  })
    }
}