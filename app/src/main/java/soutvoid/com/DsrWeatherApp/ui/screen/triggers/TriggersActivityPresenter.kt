package soutvoid.com.DsrWeatherApp.ui.screen.triggers

import com.agna.ferro.mvp.component.scope.PerScreen
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import javax.inject.Inject

@PerScreen
class TriggersActivityPresenter @Inject constructor(errorHandler: ErrorHandler)
    :BasePresenter<TriggersActivityView>(errorHandler) {



}