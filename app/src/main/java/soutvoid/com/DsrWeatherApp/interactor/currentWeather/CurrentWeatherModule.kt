package soutvoid.com.DsrWeatherApp.interactor.currentWeather

import com.agna.ferro.mvp.component.scope.PerApplication
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import soutvoid.com.DsrWeatherApp.interactor.currentWeather.network.CurrentWeatherApi

@Module
class CurrentWeatherModule {

    @Provides
    @PerApplication
    fun provideCurrentWeatherApi(retrofit: Retrofit) : CurrentWeatherApi {
        return retrofit.create(CurrentWeatherApi::class.java)
    }

}