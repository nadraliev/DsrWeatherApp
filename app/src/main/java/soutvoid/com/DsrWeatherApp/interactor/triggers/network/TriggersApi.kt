package soutvoid.com.DsrWeatherApp.interactor.triggers.network

import retrofit2.http.*
import rx.Observable
import soutvoid.com.DsrWeatherApp.interactor.common.network.ServerUrls
import soutvoid.com.DsrWeatherApp.interactor.triggers.data.NewTriggerRequest
import soutvoid.com.DsrWeatherApp.interactor.triggers.data.TriggerResponse

interface TriggersApi {

    @POST(ServerUrls.TRIGGERS_URL)
    fun newTrigger(@Body newTriggerRequest: NewTriggerRequest) : Observable<TriggerResponse>

    @GET("${ServerUrls.TRIGGERS_URL}/{id}")
    fun getTrigger(@Path("id") id: String) : Observable<TriggerResponse>

    @DELETE("${ServerUrls.TRIGGERS_URL}/{id}")
    fun deleteTrigger(@Path("id") id: String) : Observable<Void>

}