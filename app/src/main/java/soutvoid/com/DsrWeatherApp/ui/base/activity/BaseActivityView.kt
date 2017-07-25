package soutvoid.com.DsrWeatherApp.ui.base.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.ActivityManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import butterknife.ButterKnife
import com.agna.ferro.mvp.component.ScreenComponent
import com.agna.ferro.mvp.view.activity.MvpActivityView
import com.mikepenz.iconics.context.IconicsContextWrapper
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.app.App
import soutvoid.com.DsrWeatherApp.app.dagger.AppComponent
import soutvoid.com.DsrWeatherApp.app.log.LogConstants
import soutvoid.com.DsrWeatherApp.app.log.RemoteLogger
import soutvoid.com.DsrWeatherApp.ui.util.getDefaultPreferences
import soutvoid.com.DsrWeatherApp.ui.util.getPreferredThemeId
import soutvoid.com.DsrWeatherApp.util.SdkUtil


abstract class BaseActivityView : MvpActivityView() {


    abstract override fun getPresenter(): BasePresenter<*>

    val appComponent: AppComponent
        get() = (application as App).appComponent

    @SuppressLint("NewApi")
    override fun onPreCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        setTheme(getDefaultPreferences().getPreferredThemeId())
        if (SdkUtil.supportsLollipop())
            initTaskDescription()
        super.onPreCreate(savedInstanceState, viewRecreated)
    }

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

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase))
    }

    @TargetApi(21)
    private fun initTaskDescription() {
        val bm = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        setTaskDescription(ActivityManager.TaskDescription(
                getString(R.string.app_name),
                bm,
                ContextCompat.getColor(this, typedValue.resourceId)))
    }
}
