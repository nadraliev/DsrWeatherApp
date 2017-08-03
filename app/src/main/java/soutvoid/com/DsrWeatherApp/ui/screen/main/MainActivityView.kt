package soutvoid.com.DsrWeatherApp.ui.screen.main

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import com.agna.ferro.mvp.component.ScreenComponent
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.screen.main.locations.LocationsFragmentView
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.*
import soutvoid.com.DsrWeatherApp.ui.common.activity.TranslucentStatusActivityView
import soutvoid.com.DsrWeatherApp.ui.screen.main.settings.SettingsFragment
import soutvoid.com.DsrWeatherApp.ui.screen.main.triggers.TriggersFragmentView
import soutvoid.com.DsrWeatherApp.ui.util.*
import soutvoid.com.DsrWeatherApp.util.SdkUtil

class MainActivityView: TranslucentStatusActivityView() {

    companion object {
        const val THEME_CHANGE_RESTARTED = "theme_change"
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivityView::class.java))
        }
    }

    @Inject
    lateinit var presenter: MainActivityPresenter

    lateinit var sharedPreferencesListener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun getContentView(): Int = R.layout.activity_main

    override fun getName(): String = "MainActivity"

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerMainActivityComponent.builder()
                .activityModule(activityModule)
                .appComponent(appComponent)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)

        initToolbar()
        initDrawer()
        maybeWriteInitData()
        initSharedPreferencesListener()
    }

    private fun initToolbar() {
        setSupportActionBar(main_toolbar)
        val actionBarToggle = ActionBarDrawerToggle(
                this,
                main_drawer,
                main_toolbar,
                R.string.open,
                R.string.close
        )
        actionBarToggle.isDrawerIndicatorEnabled = false
        actionBarToggle.isDrawerIndicatorEnabled = true //если этого не слелеать, сэндвич кнопка не появится
        main_drawer.addDrawerListener(actionBarToggle)
    }

    private fun initDrawer() {
        main_navigation.setNavigationItemSelectedListener {
            main_drawer.closeDrawers()
            when(it.itemId) {
                R.id.drawer_locations -> showLocationsFragment()
                R.id.drawer_notifications -> showNotificationsFragment()
                R.id.drawer_settings -> showSettingsFragment()
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true, clearBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, fragment)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        if (clearBackStack)
            supportFragmentManager.clearBackStack()
        if (addToBackStack)
            transaction.addToBackStack(getRandomString())
        transaction.commit()
    }

    private fun maybeWriteInitData() {
        val editor = getDefaultPreferences().edit()
        if (!getDefaultPreferences().contains(SettingsFragment.SHARED_PREFERENCES_THEME))
            editor.putString(SettingsFragment.SHARED_PREFERENCES_THEME, "0")
        if (!getDefaultPreferences().contains(SettingsFragment.SHARED_PREFERENCES_DARK_THEME))
            editor.putBoolean(SettingsFragment.SHARED_PREFERENCES_DARK_THEME, false)
        editor.commit()
    }

    private fun initSharedPreferencesListener() {
        sharedPreferencesListener = SharedPreferences.OnSharedPreferenceChangeListener { preferences, s ->
            if (s == SettingsFragment.SHARED_PREFERENCES_THEME || s == SettingsFragment.SHARED_PREFERENCES_DARK_THEME) {
                setThemeJustChanged(true)
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

    private fun restart() {
        finish()
        MainActivityView.start(this)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    @TargetApi(21)
    private fun restartWithReveal() {
        try {
            setTheme(getDefaultPreferences().getPreferredThemeId())
            main_reveal_view.setBackgroundColor(theme.getThemeColor(android.R.attr.colorBackground))
            val animation = main_reveal_view.createFullScreenCircularReveal(
                    main_drawer.measuredWidth / 2,
                    main_drawer.measuredHeight / 2
            )
            animation.duration = 300
            animation.addListener(AnimationEndedListener { restart() })
            main_reveal_view.visibility = View.VISIBLE
            animation.start()
        } catch (e: NoClassDefFoundError) {
            restart()
        }
    }

    override fun onDestroy() {
        getDefaultPreferences().unregisterOnSharedPreferenceChangeListener(sharedPreferencesListener)
        super.onDestroy()
    }

    fun showLocationsFragment() {
        showFragment(LocationsFragmentView(), false, true)
    }

    fun showNotificationsFragment() {
        showFragment(TriggersFragmentView())
    }

    fun showSettingsFragment() {
        showFragment(SettingsFragment())
    }

    fun isThemeJustChanged(): Boolean {
        return getDefaultPreferences().getBoolean(THEME_CHANGE_RESTARTED, false)
    }

    fun setThemeJustChanged(justChanged: Boolean) {
        getDefaultPreferences().edit().putBoolean(THEME_CHANGE_RESTARTED, justChanged).apply()
    }
}