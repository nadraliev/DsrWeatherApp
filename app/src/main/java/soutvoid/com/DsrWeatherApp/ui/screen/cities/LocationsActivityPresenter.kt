package soutvoid.com.DsrWeatherApp.ui.screen.cities

import com.agna.ferro.mvp.component.scope.PerScreen
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import javax.inject.Inject

@PerScreen
class LocationsActivityPresenter @Inject constructor(errorHandler: ErrorHandler)
    : BasePresenter<LocationsActivityView>(errorHandler) {



}