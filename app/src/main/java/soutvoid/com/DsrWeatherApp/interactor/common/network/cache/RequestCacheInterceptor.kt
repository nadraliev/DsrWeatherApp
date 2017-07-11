package soutvoid.com.DsrWeatherApp.interactor.common.network.cache

import com.agna.ferro.mvp.component.scope.PerApplication
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import soutvoid.com.DsrWeatherApp.interactor.network.NetworkConnectionChecker
import java.util.concurrent.TimeUnit

@PerApplication
class RequestCacheInterceptor constructor(val networkConnectionChecker: NetworkConnectionChecker) : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        var request = chain!!.request()

        if (!networkConnectionChecker.hasInternetConnection()) {
            val cacheControl : CacheControl = CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()

            request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build()
        }

        return chain.proceed(request)
    }
}