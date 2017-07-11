package soutvoid.com.DsrWeatherApp.interactor.common.network.request

import okhttp3.Interceptor
import okhttp3.Response
import soutvoid.com.DsrWeatherApp.interactor.common.network.ServerConstants

class RequestQueryParamsInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        var request = chain!!.request()
        val url = request.url().newBuilder()
                .addQueryParameter(ServerConstants.API_KEY_PARAMETER, ServerConstants.API_KEY)
                .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}