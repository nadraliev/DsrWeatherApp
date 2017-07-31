package soutvoid.com.DsrWeatherApp.ui.screen.settings

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View
import android.view.ViewAnimationUtils
import com.agna.ferro.mvp.component.ScreenComponent
import kotlinx.android.synthetic.main.activity_settings.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.activity.TranslucentStatusActivityView
import soutvoid.com.DsrWeatherApp.ui.util.*
import soutvoid.com.DsrWeatherApp.util.SdkUtil
import javax.inject.Inject

class SettingsActivityView: TranslucentStatusActivityView() {

    companion object {
        const val RESTART_REQUIRED = "restart_required"
        fun start(context: Context) {
            context.startActivity(Intent(context, SettingsActivityView::class.java))
        }
        fun startForResult(activity: Activity, requestCode: Int) {
            activity.startActivityForResult(Intent(activity, SettingsActivityView::class.java), requestCode)
        }
    }

    @Inject
    lateinit var presenter: SettingsActivityPresenter

    lateinit var sharedPreferencesListener : SharedPreferences.OnSharedPreferenceChangeListener

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

        maybeWriteInitData()

        initToolbar()
        initPreferencesFragment()

        sharedPreferencesListener = SharedPreferences.OnSharedPreferenceChangeListener { preferences, s ->
            if (s == SettingsFragment.SHARED_PREFERENCES_THEME || s == SettingsFragment.SHARED_PREFERENCES_DARK_THEME) {
                val intentData = Intent()
                intentData.putExtra(RESTART_REQUIRED, true)
                setResult(0, intentData)

                @SuppressLint("NewApi")
                if (SdkUtil.supportsLollipop()) {
                    restartWithReveal()
                } else {
                    restart()
                }
            }
        }

        getDefaultPreferences().registerOnSharedPreferenceChangeListener(sharedPreferencesListener)
    }

    private fun maybeWriteInitData() {
        val editor = getDefaultPreferences().edit()
        if (!getDefaultPreferences().contains(SettingsFragment.SHARED_PREFERENCES_THEME))
            editor.putString(SettingsFragment.SHARED_PREFERENCES_THEME, "0")
        if (!getDefaultPreferences().contains(SettingsFragment.SHARED_PREFERENCES_DARK_THEME))
            editor.putBoolean(SettingsFragment.SHARED_PREFERENCES_DARK_THEME, false)
        editor.commit()
    }

    private fun initToolbar() {
        setSupportActionBar(settings_toolbar)
        title = getString(R.string.settings)
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.themedBackDrawable, typedValue, true)
        settings_toolbar.navigationIcon = ContextCompat.getDrawable(this, typedValue.resourceId)
        settings_toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initPreferencesFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.settings_content, SettingsFragment(presenter.messagePresenter))
                .commit()
    }

    private fun restart() {
        finish()
        start(this)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    @TargetApi(21)
    private fun restartWithReveal() {
        try {
            setTheme(getDefaultPreferences().getPreferredThemeId())
            settings_reveal_view.setBackgroundColor(theme.getThemeColor(android.R.attr.colorBackground))
            val animation = settings_reveal_view.createFullScreenCircularReveal(
                    settings_reveal_view.measuredWidth / 2,
                    settings_reveal_view.measuredHeight / 2
            )
            animation.duration = 300
            animation.addListener(AnimationEndedListener { restart() })
            settings_reveal_view.visibility = View.VISIBLE
            animation.start()
        } catch (e: NoClassDefFoundError) {
            restart()
        }
    }

    override fun onDestroy() {
        getDefaultPreferences().unregisterOnSharedPreferenceChangeListener(sharedPreferencesListener)
        super.onDestroy()
    }
}