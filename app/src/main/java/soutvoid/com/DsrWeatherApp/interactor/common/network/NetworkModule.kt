package soutvoid.com.DsrWeatherApp.interactor.common.network

import com.agna.ferro.mvp.component.scope.PerApplication
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import soutvoid.com.DsrWeatherApp.BuildConfig
import soutvoid.com.DsrWeatherApp.interactor.common.network.cache.RequestCacheInterceptor
import soutvoid.com.DsrWeatherApp.interactor.common.network.cache.ResponseCacheInterceptor
import soutvoid.com.DsrWeatherApp.interactor.common.network.request.RequestHeadersInterceptor
import soutvoid.com.DsrWeatherApp.interactor.common.network.request.RequestQueryParamsInterceptor
import soutvoid.com.DsrWeatherApp.interactor.network.NetworkConnectionChecker
import timber.log.Timber

@Module
class NetworkModule {

    val HTTP_LOG_TAG = "OkHttp"

    @Provides
    @PerApplication
    internal fun provideRetrofit(okHttpClient: OkHttpClient,
                                 gson: Gson): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ServerUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }

    @Provides
    @PerApplication
    internal fun provideGson(): Gson = Gson()

    @Provides
    @PerApplication
    fun provideHttpLoggingInterceptor() : HttpLoggingInterceptor {
        val httpLoggingInterceptor : HttpLoggingInterceptor = HttpLoggingInterceptor{message -> Timber.tag(HTTP_LOG_TAG).d(message) }
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        }
        return httpLoggingInterceptor
    }

    @Provides
    @PerApplication
    internal fun provideRequestHeadersInterceptor(): RequestHeadersInterceptor = RequestHeadersInterceptor()

    @Provides
    @PerApplication
    internal fun provideRequestQueryParamsInterceptor() : RequestQueryParamsInterceptor = RequestQueryParamsInterceptor()

    @Provides
    @PerApplication
    internal fun provideResponseCacheInterceptor(): ResponseCacheInterceptor = ResponseCacheInterceptor()

    @Provides
    @PerApplication
    internal fun provideRequestCacheInterceptor(networkConnectionChecker: NetworkConnectionChecker): RequestCacheInterceptor = RequestCacheInterceptor(networkConnectionChecker)

}