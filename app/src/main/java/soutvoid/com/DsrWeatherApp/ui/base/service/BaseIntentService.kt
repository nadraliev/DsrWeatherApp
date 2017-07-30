package soutvoid.com.DsrWeatherApp.ui.base.service

import android.app.IntentService
import android.content.Intent
import soutvoid.com.DsrWeatherApp.app.App
import soutvoid.com.DsrWeatherApp.app.dagger.AppComponent
import soutvoid.com.DsrWeatherApp.app.log.LogConstants
import soutvoid.com.DsrWeatherApp.app.log.Logger
import soutvoid.com.DsrWeatherApp.app.log.RemoteLogger

abstract class BaseIntentService(val name: String): IntentService(name) {


    override fun onCreate() {
        RemoteLogger.logMessage(String.format(LogConstants.LOG_SERVICE_CREATE_FORMAT, name))
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        RemoteLogger.logMessage(String.format(LogConstants.LOG_SERVICE_START_COMMAND_FORMAT, name))
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        RemoteLogger.logMessage(String.format(LogConstants.LOG_SERVICE_DESTROY_FORMAT, name))
        super.onDestroy()
    }

    internal fun getAppComponent(): AppComponent {
        val app = application as App
        return app.appComponent
    }
}