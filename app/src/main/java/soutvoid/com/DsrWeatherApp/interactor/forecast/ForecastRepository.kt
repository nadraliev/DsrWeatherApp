package soutvoid.com.DsrWeatherApp.interactor.forecast

import com.agna.ferro.mvp.component.scope.PerApplication
import rx.Observable
import soutvoid.com.DsrWeatherApp.domain.Forecast
import soutvoid.com.DsrWeatherApp.interactor.forecast.network.ForecastApi
import soutvoid.com.DsrWeatherApp.interactor.util.Accuracy
import soutvoid.com.DsrWeatherApp.interactor.util.Units
import javax.inject.Inject

@PerApplication
class ForecastRepository @Inject constructor(val api : ForecastApi) {

    fun getByCityName(query: String,
                      units: Units = Units.METRIC,
                      accuracyType: Accuracy = Accuracy.LIKE,
                      lang: String = "en")
            : Observable<Forecast> {
        return api.getByCityName(query, units.name.toLowerCase(), accuracyType.name.toLowerCase(), lang)
    }

    fun getByCityId(cityId: Int,
                    units: Units = Units.METRIC,
                    accuracyType: Accuracy = Accuracy.LIKE,
                    lang: String = "en")
            : Observable<Forecast> {
        return api.getByCityId(cityId, units.name.toLowerCase(), accuracyType.name.toLowerCase(), lang)
    }

    fun getByCoordinates(latitude: Double,
                         longitude: Double,
                         units: Units = Units.METRIC,
                         accuracyType: Accuracy = Accuracy.LIKE,
                         lang: String = "en")
            : Observable<Forecast> {
        return api.getByCoordinates(latitude, longitude, units.name.toLowerCase(), accuracyType.name.toLowerCase(), lang)
    }

}