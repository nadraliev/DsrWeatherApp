package soutvoid.com.DsrWeatherApp.ui.screen.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import com.agna.ferro.mvp.component.ScreenComponent
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.screen.main.locations.LocationsFragmentView
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.*
import soutvoid.com.DsrWeatherApp.ui.screen.main.settings.SettingsFragment
import soutvoid.com.DsrWeatherApp.ui.screen.main.triggers.TriggersFragmentView
import soutvoid.com.DsrWeatherApp.ui.screen.settings.SettingsActivityView
import soutvoid.com.DsrWeatherApp.ui.util.clearBackStack
import soutvoid.com.DsrWeatherApp.ui.util.getRandomString

class MainActivityView: BaseActivityView() {

    @Inject
    lateinit var presenter: MainActivityPresenter

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

    fun showLocationsFragment() {
        showFragment(LocationsFragmentView(), false, true)
    }

    fun showNotificationsFragment() {
        showFragment(TriggersFragmentView())
    }

    fun showSettingsFragment() {
        showFragment(SettingsFragment())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            if (it.getBooleanExtra(SettingsActivityView.RESTART_REQUIRED, false)) {
                val intent = Intent(this, LocationsFragmentView::class.java)
                finish()
                startActivity(intent)
            }
        }
    }
}