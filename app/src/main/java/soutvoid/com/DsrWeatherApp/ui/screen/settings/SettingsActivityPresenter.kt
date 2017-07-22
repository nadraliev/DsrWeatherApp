package soutvoid.com.DsrWeatherApp.ui.screen.settings

import com.agna.ferro.mvp.component.scope.PerScreen
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.ui.common.message.MessagePresenter
import javax.inject.Inject

@PerScreen
class SettingsActivityPresenter @Inject constructor(val messagePresenter: MessagePresenter, errorHandler: ErrorHandler):
    BasePresenter<SettingsActivityView>(errorHandler)