package soutvoid.com.DsrWeatherApp.interactor.forecast.network

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable
import soutvoid.com.DsrWeatherApp.domain.DailyForecast
import soutvoid.com.DsrWeatherApp.domain.Forecast
import soutvoid.com.DsrWeatherApp.interactor.common.network.ServerConstants
import soutvoid.com.DsrWeatherApp.interactor.common.network.ServerUrls

interface ForecastApi {

    @GET(ServerUrls.FORECAST_URL)
    fun getByCityName(@Query(ServerConstants.QUERY_PARAMETER) query: String,
                      @Query(ServerConstants.UNITS_PARAMETER) units: String = "metric",
                      @Query(ServerConstants.ACCURACY_PARAMETER) accuracyType: String = "like",
                      @Query(ServerConstants.LANG_PARAMETER) lang: String = "en")
            : Observable<Forecast>

    @GET(ServerUrls.FORECAST_URL)
    fun getByCityId(@Query(ServerConstants.CITY_ID_PARAMETER) cityId: Int,
                    @Query(ServerConstants.UNITS_PARAMETER) units: String = "metric",
                    @Query(ServerConstants.ACCURACY_PARAMETER) accuracyType: String = "like",
                    @Query(ServerConstants.LANG_PARAMETER) lang: String = "en")
            : Observable<Forecast>

    @GET(ServerUrls.FORECAST_URL)
    fun getByCoordinates(@Query(ServerConstants.LATITUDE_PARAMETER) latitude : Double,
                         @Query(ServerConstants.LONGITUDE_PARAMETER) longitude: Double,
                         @Query(ServerConstants.UNITS_PARAMETER) units: String = "metric",
                         @Query(ServerConstants.ACCURACY_PARAMETER) accuracyType: String = "like",
                         @Query(ServerConstants.LANG_PARAMETER) lang: String = "en")
            : Observable<Forecast>


    @GET(ServerUrls.DAILY_FORECAST_URL)
    fun getDailyByCityName(@Query(ServerConstants.QUERY_PARAMETER) query: String,
                      @Query(ServerConstants.UNITS_PARAMETER) units: String = "metric",
                      @Query(ServerConstants.ACCURACY_PARAMETER) accuracyType: String = "like",
                      @Query(ServerConstants.LANG_PARAMETER) lang: String = "en")
            : Observable<DailyForecast>

    @GET(ServerUrls.DAILY_FORECAST_URL)
    fun getDailyByCityId(@Query(ServerConstants.CITY_ID_PARAMETER) cityId: Int,
                         @Query(ServerConstants.UNITS_PARAMETER) units: String = "metric",
                         @Query(ServerConstants.ACCURACY_PARAMETER) accuracyType: String = "like",
                         @Query(ServerConstants.LANG_PARAMETER) lang: String = "en")
            : Observable<DailyForecast>

    @GET(ServerUrls.DAILY_FORECAST_URL)
    fun getDailyByCoordinates(@Query(ServerConstants.LATITUDE_PARAMETER) latitude : Double,
                         @Query(ServerConstants.LONGITUDE_PARAMETER) longitude: Double,
                         @Query(ServerConstants.UNITS_PARAMETER) units: String = "metric",
                         @Query(ServerConstants.ACCURACY_PARAMETER) accuracyType: String = "like",
                         @Query(ServerConstants.LANG_PARAMETER) lang: String = "en")
            : Observable<DailyForecast>

}