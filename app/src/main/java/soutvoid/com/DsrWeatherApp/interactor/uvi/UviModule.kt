package soutvoid.com.DsrWeatherApp.interactor.uvi

import com.agna.ferro.mvp.component.scope.PerApplication
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import soutvoid.com.DsrWeatherApp.interactor.uvi.network.UviApi

@Module
class UviModule {

    @Provides
    @PerApplication
    fun provideUviApi(retrofit: Retrofit) : UviApi {
        return retrofit.create(UviApi::class.java)
    }

}