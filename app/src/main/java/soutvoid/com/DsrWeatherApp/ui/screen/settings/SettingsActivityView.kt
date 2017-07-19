package soutvoid.com.DsrWeatherApp.ui.screen.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.agna.ferro.mvp.component.ScreenComponent
import kotlinx.android.synthetic.main.activity_settings.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_settings.view.*

class SettingsActivityView: BaseActivityView() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SettingsActivityView::class.java))
        }
    }

    @Inject
    lateinit var presenter: SettingsActivityPresenter

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun getName(): String = "Settings"

    override fun createScreenComponent(): ScreenComponent<*>? {
        return DaggerSettingsActivityComponent.builder()
                .activityModule(activityModule)
                .appComponent(appComponent)
                .build()
    }

    override fun getContentView(): Int = R.layout.activity_settings

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)

        initToolbar()
        initPreferencesFragment()
    }

    private fun initToolbar() {
        setSupportActionBar(settings_toolbar)
        title = getString(R.string.settings)
        settings_toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_light)
        settings_toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initPreferencesFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.settings_content, SettingsFragment())
                .commit()
    }
}