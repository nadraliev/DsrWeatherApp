package soutvoid.com.DsrWeatherApp.interactor.common.network.request

import okhttp3.Interceptor
import okhttp3.Response

class RequestHeadersInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain!!.request().newBuilder()
                //add your headers here
                //.addHeader(KEY_API_KEY, API_KEY)
                .build()
        return chain.proceed(request)
    }
}