package soutvoid.com.DsrWeatherApp.ui.screen.triggers

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import com.agna.ferro.mvp.component.ScreenComponent
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_triggers.*

class TriggersActivityView: BaseActivityView() {

    @Inject
    lateinit var presenter: TriggersActivityPresenter

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun getContentView(): Int = R.layout.activity_triggers

    override fun getName(): String = "Triggers"

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerTriggersActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(activityModule)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)

        initToolbar()
    }

    fun initToolbar() {
        setSupportActionBar(triggers_toolbar)
        title = getString(R.string.notifications)
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.themedBackDrawable, typedValue, true)
        triggers_toolbar.navigationIcon = ContextCompat.getDrawable(this, typedValue.resourceId)
        triggers_toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}