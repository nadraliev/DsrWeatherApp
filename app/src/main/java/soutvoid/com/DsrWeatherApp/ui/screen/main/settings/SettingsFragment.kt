package soutvoid.com.DsrWeatherApp.ui.screen.main.settings

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import soutvoid.com.DsrWeatherApp.R

class SettingsFragment: PreferenceFragmentCompat() {

    companion object {
        const val SHARED_PREFERENCES_NAME = "default"
        const val SHARED_PREFERENCES_THEME = "theme"
        const val SHARED_PREFERENCES_DARK_THEME = "prefer_dark"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity.title = getString(R.string.settings)

        preferenceManager.sharedPreferencesName = SHARED_PREFERENCES_NAME
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    }
}