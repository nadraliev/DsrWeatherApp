package soutvoid.com.DsrWeatherApp.interactor.common.network.cache

import com.agna.ferro.mvp.component.scope.PerApplication
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

@PerApplication
class ResponseCacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val response : Response = chain!!.proceed(chain.request())

        val cacheControl : CacheControl = CacheControl.Builder()
                .maxAge(10, TimeUnit.MINUTES)
                .build()

        return response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build()
    }
}