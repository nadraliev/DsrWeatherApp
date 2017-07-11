package soutvoid.com.DsrWeatherApp.ui.base.activity

import android.os.Bundle
import butterknife.ButterKnife
import com.agna.ferro.mvp.component.ScreenComponent
import com.agna.ferro.mvp.view.activity.MvpActivityView
import soutvoid.com.DsrWeatherApp.app.App
import soutvoid.com.DsrWeatherApp.app.dagger.AppComponent
import soutvoid.com.DsrWeatherApp.app.log.LogConstants
import soutvoid.com.DsrWeatherApp.app.log.RemoteLogger


abstract class BaseActivityView : MvpActivityView() {


    abstract override fun getPresenter(): BasePresenter<*>

    val appComponent: AppComponent
        get() = (application as App).appComponent

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)

        ButterKnife.bind(this)
    }

    override fun onResume() {
        super.onResume()
        RemoteLogger.logMessage(String.format(LogConstants.LOG_SCREEN_RESUME_FORMAT, name))
    }

    override fun onPause() {
        super.onPause()
        RemoteLogger.logMessage(String.format(LogConstants.LOG_SCREEN_PAUSE_FORMAT, name))
    }

    val activityModule: ActivityModule
        get() = ActivityModule(persistentScreenScope)

    override fun getScreenComponent(): ScreenComponent<*>? {
        return persistentScreenScope.getObject(ScreenComponent::class.java)
    }


}
