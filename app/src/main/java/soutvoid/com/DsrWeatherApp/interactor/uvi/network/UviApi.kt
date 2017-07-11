package soutvoid.com.DsrWeatherApp.interactor.uvi.network

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable
import soutvoid.com.DsrWeatherApp.domain.ultraviolet.Ultraviolet
import soutvoid.com.DsrWeatherApp.interactor.common.network.ServerConstants
import soutvoid.com.DsrWeatherApp.interactor.common.network.ServerUrls

interface UviApi {

    @GET(ServerUrls.UVI_URL)
    fun getByCoordinates(@Query(ServerConstants.LATITUDE_PARAMETER) latitude : Double,
                         @Query(ServerConstants.LONGITUDE_PARAMETER) longitude: Double,
                         @Query(ServerConstants.UNITS_PARAMETER) units: String = "metric",
                         @Query(ServerConstants.ACCURACY_PARAMETER) accuracyType: String = "like",
                         @Query(ServerConstants.LANG_PARAMETER) lang: String = "en")
            : Observable<Ultraviolet>

}