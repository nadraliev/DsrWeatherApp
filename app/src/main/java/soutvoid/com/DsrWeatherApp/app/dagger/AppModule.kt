package soutvoid.com.DsrWeatherApp.app.dagger

import android.content.Context
import com.agna.ferro.mvp.component.scope.PerApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule(val appContext: Context) {

    @Provides
    @PerApplication
    fun provideContext() : Context = appContext

}