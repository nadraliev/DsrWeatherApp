package soutvoid.com.DsrWeatherApp.ui.screen.weather

import com.agna.ferro.mvp.component.scope.PerScreen
import io.realm.Realm
import rx.Observable
import rx.functions.Action1
import rx.schedulers.Schedulers
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.interactor.currentWeather.CurrentWeatherRepository
import soutvoid.com.DsrWeatherApp.interactor.forecast.ForecastRepository
import soutvoid.com.DsrWeatherApp.interactor.util.ObservableUtil
import soutvoid.com.DsrWeatherApp.interactor.uvi.UviRepository
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.ui.common.error.StandardWithActionErrorHandler
import soutvoid.com.DsrWeatherApp.ui.common.message.MessagePresenter
import soutvoid.com.DsrWeatherApp.ui.screen.weather.data.AllWeatherData
import soutvoid.com.DsrWeatherApp.ui.util.UnitsUtils
import soutvoid.com.DsrWeatherApp.ui.util.ifNotNullOr
import javax.inject.Inject

@PerScreen
class WeatherActivityPresenter
@Inject constructor(val messagePresenter: MessagePresenter, errorHandler: ErrorHandler)
    : BasePresenter<WeatherActivityView>(errorHandler) {

    @Inject
    lateinit var currentWeatherRep : CurrentWeatherRepository

    @Inject
    lateinit var forecastRep : ForecastRepository

    @Inject
    lateinit var uviRep : UviRepository

    lateinit var savedLocation: SavedLocation


    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)



        savedLocation = view.getLocationParam().ifNotNullOr{
            val realm = Realm.getDefaultInstance()
            val result = realm.copyFromRealm(realm.where(SavedLocation::class.java).findFirst())
            realm.close()
            return@ifNotNullOr result
        }

        view.maybeInitForecastList(savedLocation.showForecast)
        view.fillCityName(savedLocation.name)

        view.setProgressBarEnabled(true)
        loadData()
    }

    /**
     * загрузить данные и вывести на экран
     */
    private fun loadData() {
        subscribeNetworkQuery(
                prepareObservable(),
                Action1 {
                    view.fillAllData(it, savedLocation.showForecast)
                    view.setProgressBarEnabled(false)
                },
                Action1 { view.setProgressBarEnabled(false) },
                StandardWithActionErrorHandler(messagePresenter,
                        view.getString(R.string.try_again))
                        { loadData() }
        )
    }

    /**
     * подготовить observable для всех загружаемых данных и объединить в один
     * @return observable для загрузки всех данных
     */
    private fun prepareObservable() : Observable<AllWeatherData> {
        val units = UnitsUtils.getPreferredUnits(view.baseContext)
        return ObservableUtil.combineLatestDelayError(
                Schedulers.io(),
                currentWeatherRep.getByCoordinates(savedLocation.latitude, savedLocation.longitude, units),
                forecastRep.getByCoordinates(savedLocation.latitude, savedLocation.longitude, units),
                forecastRep.getDailyByCoordinates(savedLocation.latitude, savedLocation.longitude, units)
        ) { current, forecast, dailyForecast -> AllWeatherData(current, forecast, dailyForecast) }
    }

    /**
     * событие pull down to refresh
     */
    fun refresh() {
        loadData()
    }
}