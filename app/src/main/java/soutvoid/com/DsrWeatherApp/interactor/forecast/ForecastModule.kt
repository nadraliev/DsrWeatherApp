package soutvoid.com.DsrWeatherApp.interactor.forecast

import com.agna.ferro.mvp.component.scope.PerApplication
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import soutvoid.com.DsrWeatherApp.interactor.forecast.network.ForecastApi

@Module
class ForecastModule {

    @Provides
    @PerApplication
    fun provideForecastApi(retrofit: Retrofit) : ForecastApi {
        return retrofit.create(ForecastApi::class.java)
    }

}