package soutvoid.com.DsrWeatherApp.ui.common.error


import rx.exceptions.CompositeException
import soutvoid.com.DsrWeatherApp.app.log.Logger
import soutvoid.com.DsrWeatherApp.interactor.common.network.error.ConversionException
import soutvoid.com.DsrWeatherApp.interactor.common.network.error.HttpError
import soutvoid.com.DsrWeatherApp.interactor.common.network.error.NetworkException
import soutvoid.com.DsrWeatherApp.interactor.common.network.error.NoInternetException

abstract class ErrorHandler {

    fun handleError(t: Throwable) {
        Logger.i(t, "ErrorHandler handle error")
        if (t is CompositeException) {
            handleCompositeException(t)
        } else if (t is HttpError) {
            handleHttpError(t)
        } else if (t is NoInternetException) {
            handleNoInternetError(t)
        } else if (t is ConversionException) {
            handleConversionError(t)
        } else {
            handleOtherError(t)
        }
    }

    /**
     * @param err - CompositeException может возникать при комбинировании Observable
     */
    private fun handleCompositeException(err: CompositeException) {
        val exceptions = err.exceptions
        var networkException: NetworkException? = null
        var otherException: Throwable? = null
        for (e in exceptions) {
            if (e is NetworkException) {
                if (networkException == null) {
                    networkException = e
                }
            } else if (otherException == null) {
                otherException = e
            }
        }
        if (networkException != null) {
            handleError(networkException)
        }
        if (otherException != null) {
            handleOtherError(otherException)
        }
    }


    protected abstract fun handleHttpError(e: HttpError)

    protected abstract fun handleNoInternetError(e: NoInternetException)

    protected abstract fun handleConversionError(e: ConversionException)

    protected abstract fun handleOtherError(e: Throwable)
}
