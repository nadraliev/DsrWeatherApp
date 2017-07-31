package soutvoid.com.DsrWeatherApp.ui.screen.widgetConfiguration

import android.os.Bundle
import android.os.PersistableBundle
import com.agna.ferro.mvp.component.ScreenComponent
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import javax.inject.Inject

class WidgetConfigurationActivityView : BaseActivityView() {

    @Inject
    lateinit var presenter: WidgetConfigurationActivityPresenter

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun getContentView(): Int = R.layout.activity_widget_configuration

    override fun getName(): String = "WidgetConfiguration"

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerWidgetConfigurationActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(activityModule)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setTheme(android.R.style.Theme_Dialog)
    }
}