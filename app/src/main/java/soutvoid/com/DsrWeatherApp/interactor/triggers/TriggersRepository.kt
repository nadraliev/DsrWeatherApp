package soutvoid.com.DsrWeatherApp.interactor.triggers

import com.agna.ferro.mvp.component.scope.PerApplication
import rx.Observable
import soutvoid.com.DsrWeatherApp.interactor.triggers.data.NewTriggerRequest
import soutvoid.com.DsrWeatherApp.interactor.triggers.data.TriggerResponse
import soutvoid.com.DsrWeatherApp.interactor.triggers.network.TriggersApi
import javax.inject.Inject

@PerApplication
class TriggersRepository @Inject constructor(val api: TriggersApi) {

    fun newTrigger(newTriggerRequest: NewTriggerRequest): Observable<TriggerResponse> {
        return api.newTrigger(newTriggerRequest)
    }

}