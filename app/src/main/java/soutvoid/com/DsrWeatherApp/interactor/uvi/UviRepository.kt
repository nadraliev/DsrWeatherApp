package soutvoid.com.DsrWeatherApp.interactor.uvi

import com.agna.ferro.mvp.component.scope.PerApplication
import rx.Observable
import soutvoid.com.DsrWeatherApp.domain.ultraviolet.Ultraviolet
import soutvoid.com.DsrWeatherApp.interactor.util.Accuracy
import soutvoid.com.DsrWeatherApp.interactor.util.Units
import soutvoid.com.DsrWeatherApp.interactor.uvi.network.UviApi
import java.util.*
import javax.inject.Inject

@PerApplication
class UviRepository @Inject constructor(val api : UviApi) {

    fun getByCoordinates(latitude: Double,
                         longitude: Double,
                         units: Units = Units.METRIC,
                         accuracyType: Accuracy = Accuracy.LIKE,
                         lang: String = Locale.getDefault().language)
            : Observable<Ultraviolet> {
        return api.getByCoordinates(latitude, longitude, units.name.toLowerCase(), accuracyType.name.toLowerCase(), lang)
    }

}