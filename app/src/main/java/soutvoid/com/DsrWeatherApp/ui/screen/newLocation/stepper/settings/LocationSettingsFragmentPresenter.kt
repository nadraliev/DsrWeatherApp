package soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.settings

import com.agna.ferro.mvp.component.scope.PerScreen
import io.realm.Realm
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import javax.inject.Inject

@PerScreen
class LocationSettingsFragmentPresenter @Inject constructor(errorHandler: ErrorHandler)
    :BasePresenter<LocationSettingsFragmentView>(errorHandler) {

    fun checkPressed() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { it.copyToRealm(view.getData()) }
        realm.close()
        view.returnToHome()
    }

}