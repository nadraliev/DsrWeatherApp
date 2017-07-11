package soutvoid.com.DsrWeatherApp.interactor.currentWeather

import com.agna.ferro.mvp.component.scope.PerApplication
import rx.Observable
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.interactor.currentWeather.network.CurrentWeatherApi
import soutvoid.com.DsrWeatherApp.interactor.util.Accuracy
import soutvoid.com.DsrWeatherApp.interactor.util.Units
import javax.inject.Inject

@PerApplication
class CurrentWeatherRepository @Inject constructor(val api : CurrentWeatherApi) {

    fun getByCityName(query : String,
                      units : Units = Units.METRIC,
                      accuracyType : Accuracy = Accuracy.LIKE,
                      lang : String = "en")
            : Observable<CurrentWeather>
    {
        return api.getByCityName(query, units.name.toLowerCase(), accuracyType.name.toLowerCase(), lang)
    }



}