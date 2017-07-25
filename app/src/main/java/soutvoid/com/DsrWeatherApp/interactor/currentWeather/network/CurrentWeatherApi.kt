package soutvoid.com.DsrWeatherApp.interactor.currentWeather.network

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable
import soutvoid.com.DsrWeatherApp.domain.CurrentWeather
import soutvoid.com.DsrWeatherApp.interactor.common.network.ServerConstants
import soutvoid.com.DsrWeatherApp.interactor.common.network.ServerUrls

interface CurrentWeatherApi {

    @GET(ServerUrls.CURRENT_WEATHER_URL)
    fun getByCityName(@Query(ServerConstants.QUERY_PARAMETER) query: String,
                      @Query(ServerConstants.UNITS_PARAMETER) units: String = "metric",
                      @Query(ServerConstants.ACCURACY_PARAMETER) accuracyType: String = "like",
                      @Query(ServerConstants.LANG_PARAMETER) lang: String = "en")
            : Observable<CurrentWeather>

    @GET(ServerUrls.CURRENT_WEATHER_URL)
    fun getByCityId(@Query(ServerConstants.CITY_ID_PARAMETER) cityId: Int,
                    @Query(ServerConstants.UNITS_PARAMETER) units: String = "metric",
                    @Query(ServerConstants.ACCURACY_PARAMETER) accuracyType: String = "like",
                    @Query(ServerConstants.LANG_PARAMETER) lang: String = "en")
            : Observable<CurrentWeather>

    @GET(ServerUrls.CURRENT_WEATHER_URL)
    fun getByCoordinates(@Query(ServerConstants.LATITUDE_PARAMETER) latitude : Double,
                         @Query(ServerConstants.LONGITUDE_PARAMETER) longitude: Double,
                         @Query(ServerConstants.UNITS_PARAMETER) units: String = "metric",
                         @Query(ServerConstants.ACCURACY_PARAMETER) accuracyType: String = "like",
                         @Query(ServerConstants.LANG_PARAMETER) lang: String = "en")
            : Observable<CurrentWeather>
}