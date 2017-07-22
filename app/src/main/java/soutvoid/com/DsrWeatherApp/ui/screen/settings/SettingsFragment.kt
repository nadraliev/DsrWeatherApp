package soutvoid.com.DsrWeatherApp.ui.screen.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceFragment
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.common.message.MessagePresenter
import soutvoid.com.DsrWeatherApp.ui.screen.locations.LocationsActivityView

class SettingsFragment(): PreferenceFragment() {

    companion object {
        const val SHARED_PREFERENCES_NAME = "default"
        const val SHARED_PREFERENCES_THEME = "theme"
        const val SHARED_PREFERENCES_DARK_THEME = "prefer_dark"
    }

    lateinit var messagePresenter: MessagePresenter

    lateinit var sharedPreferencesListener : SharedPreferences.OnSharedPreferenceChangeListener

    constructor(messagePresenter: MessagePresenter) : this() {
        this.messagePresenter = messagePresenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesListener = SharedPreferences.OnSharedPreferenceChangeListener { preferences, s ->
            if (s == SHARED_PREFERENCES_THEME || s == SHARED_PREFERENCES_DARK_THEME) {
                messagePresenter.showWithAction(R.string.restart_to_apply, R.string.restart_now) {
                    val intent = Intent(activity, LocationsActivityView::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    activity.startActivity(intent)
                }
            }
        }

        preferenceManager.sharedPreferencesName = SettingsFragment.SHARED_PREFERENCES_NAME
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferencesListener)

        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onDestroy() {
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferencesListener)
        super.onDestroy()
    }
}