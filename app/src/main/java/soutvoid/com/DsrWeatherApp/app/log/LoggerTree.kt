package soutvoid.com.DsrWeatherApp.app.log

import android.util.Log

import timber.log.Timber

/**
 * логгирует в logcat
 * логи уровня DEBUG и выше логируется в [RemoteLogger]
 */
class LoggerTree @JvmOverloads constructor(private val mLogPriority: Int = Log.DEBUG) : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        try {
            if (priority >= mLogPriority) {
                RemoteLogger.logMessage(String.format(REMOTE_LOGGER_LOG_FORMAT, tag, message))
                if (t != null && priority >= Log.ERROR) {
                    RemoteLogger.logError(t)
                }
            }
        } catch (e: Exception) {
            super.log(priority, tag, "Remote logger error", t)
        }

    }

    companion object {

        val REMOTE_LOGGER_LOG_FORMAT = "%s: %s"
        val REMOTE_LOGGER_SEND_LOG_ERROR = "error sending to RemoteLogger"
    }
}
/**
 * приоритет по умолчанию - DEBUG
 */
