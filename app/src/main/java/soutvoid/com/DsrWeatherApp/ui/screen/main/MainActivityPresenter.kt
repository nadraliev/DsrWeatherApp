package soutvoid.com.DsrWeatherApp.ui.screen.main

import com.agna.ferro.mvp.component.scope.PerScreen
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import javax.inject.Inject

@PerScreen
class MainActivityPresenter @Inject constructor(errorHandler: ErrorHandler)
    :BasePresenter<MainActivityView>(errorHandler) {

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        view.showLocationsFragment()
        if (view.isThemeJustChanged()) {
            view.showSettingsFragment()
            view.setThemeJustChanged(false)
        }
    }
}