package soutvoid.com.DsrWeatherApp.ui.common.error

import android.view.View
import com.agna.ferro.mvp.component.scope.PerScreen
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.app.log.Logger
import soutvoid.com.DsrWeatherApp.interactor.common.network.error.ConversionException
import soutvoid.com.DsrWeatherApp.interactor.common.network.error.HttpCodes
import soutvoid.com.DsrWeatherApp.interactor.common.network.error.HttpError
import soutvoid.com.DsrWeatherApp.interactor.common.network.error.NoInternetException
import soutvoid.com.DsrWeatherApp.ui.common.message.MessagePresenter
import java.util.*
import javax.inject.Inject

@PerScreen
class StandardWithActionErrorHandler
constructor(val messagePresenter: MessagePresenter, val actionString: String, val action: (v: View) -> Unit)
    : ErrorHandler() {

    override fun handleHttpError(e: HttpError) {
        if (e.code >= HttpCodes.CODE_500) {
            messagePresenter.showWithAction(R.string.server_error_message, actionString, action)
        } else {
            messagePresenter.showWithAction(String.format(Locale.getDefault(), "%d %s", e.code, e.message), actionString, action)
        }
    }

    override fun handleNoInternetError(e: NoInternetException) {
        messagePresenter.showWithAction(R.string.no_internet_connection_error_message, actionString, action)
    }

    override fun handleConversionError(e: ConversionException) {
        messagePresenter.showWithAction(R.string.bad_response_error_message, actionString, action)
    }

    override fun handleOtherError(e: Throwable) {
        messagePresenter.showWithAction(R.string.unexpected_error_error_message, actionString, action)
        Logger.e(e, "Unexpected error")
    }
}