package soutvoid.com.DsrWeatherApp.ui.screen.locationSettings

import com.agna.ferro.mvp.component.scope.PerScreen
import io.realm.Realm
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import javax.inject.Inject

@PerScreen
class LocationSettingsActivityPresenter @Inject constructor(errorHandler: ErrorHandler)
    :BasePresenter<LocationSettingsActivityView>(errorHandler) {

    fun checkPressed() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { it.copyToRealm(view.getData()) }
        realm.close()
        view.returnToHome()
    }

}