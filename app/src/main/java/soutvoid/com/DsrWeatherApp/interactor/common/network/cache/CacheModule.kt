package soutvoid.com.DsrWeatherApp.interactor.common.network.cache

import android.content.Context
import com.agna.ferro.mvp.component.scope.PerApplication
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import java.io.File

@Module
class CacheModule {

    @Provides
    @PerApplication
    fun provideCache(context : Context) = Cache(getCacheDirectory(context), 1024*1024*10)

    fun getCacheDirectory(context: Context) = File(context.cacheDir, "http-cache")

}