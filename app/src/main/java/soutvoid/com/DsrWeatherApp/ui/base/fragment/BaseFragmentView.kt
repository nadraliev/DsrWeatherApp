package soutvoid.com.DsrWeatherApp.ui.base.fragment

import android.os.Handler
import com.agna.ferro.mvp.component.ScreenComponent
import com.agna.ferro.mvp.view.fragment.MvpFragmentV4View
import soutvoid.com.DsrWeatherApp.app.App
import soutvoid.com.DsrWeatherApp.app.dagger.AppComponent
import soutvoid.com.DsrWeatherApp.app.log.LogConstants
import soutvoid.com.DsrWeatherApp.app.log.RemoteLogger

abstract class BaseFragmentView: MvpFragmentV4View() {

    override fun onPause() {
        super.onPause()
        RemoteLogger.logMessage(String.format(LogConstants.LOG_SCREEN_PAUSE_FORMAT, name))
    }

    override fun onResume() {
        super.onResume()
        RemoteLogger.logMessage(String.format(LogConstants.LOG_SCREEN_RESUME_FORMAT, name))
    }


    override fun getScreenComponent(): ScreenComponent<*>? {
        return persistentScreenScope.getObject(ScreenComponent::class.java)
    }

    fun getFragmentModule(): FragmentModule {
        return FragmentModule(persistentScreenScope)
    }

    fun getAppComponent(): AppComponent {
        return (activity.application as App).appComponent
    }

}