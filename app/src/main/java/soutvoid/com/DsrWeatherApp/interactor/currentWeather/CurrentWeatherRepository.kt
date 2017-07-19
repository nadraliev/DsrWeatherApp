package soutvoid.com.DsrWeatherApp.interactor.currentWeather

import com.agna.ferro.mvp.component.scope.PerApplication
import rx.Observable
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.interactor.currentWeather.network.CurrentWeatherApi
import soutvoid.com.DsrWeatherApp.interactor.util.Accuracy
import soutvoid.com.DsrWeatherApp.interactor.util.Units
import java.util.*
import javax.inject.Inject

@PerApplication
class CurrentWeatherRepository @Inject constructor(val api: CurrentWeatherApi) {

    fun getByCityName(query: String,
                      units: Units = Units.METRIC,
                      accuracyType: Accuracy = Accuracy.LIKE,
                      lang: String = Locale.getDefault().language)
            : Observable<CurrentWeather> {
        return api.getByCityName(query, units.name.toLowerCase(), accuracyType.name.toLowerCase(), lang)
    }

    fun getByCityId(cityId: Int,
                    units: Units = Units.METRIC,
                    accuracyType: Accuracy = Accuracy.LIKE,
                    lang: String = Locale.getDefault().language)
            : Observable<CurrentWeather> {
        return api.getByCityId(cityId, units.name.toLowerCase(), accuracyType.name.toLowerCase(), lang)
    }

    fun getByCoordinates(latitude: Double,
                         longitude: Double,
                         units: Units = Units.METRIC,
                         accuracyType: Accuracy = Accuracy.LIKE,
                         lang: String = Locale.getDefault().language)
            : Observable<CurrentWeather> {
        return api.getByCoordinates(latitude, longitude, units.name.toLowerCase(), accuracyType.name.toLowerCase(), lang)
    }


}