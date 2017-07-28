package soutvoid.com.DsrWeatherApp.interactor.triggers

import com.agna.ferro.mvp.component.scope.PerApplication
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import soutvoid.com.DsrWeatherApp.interactor.triggers.network.TriggersApi

@Module
class TriggersModule {

    @PerApplication
    @Provides
    fun provideTriggersApi(retrofit: Retrofit) : TriggersApi {
        return retrofit.create(TriggersApi::class.java)
    }

}