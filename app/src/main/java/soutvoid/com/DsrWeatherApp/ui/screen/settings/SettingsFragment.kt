package soutvoid.com.DsrWeatherApp.ui.screen.settings

import android.os.Bundle
import android.preference.PreferenceFragment
import soutvoid.com.DsrWeatherApp.R

class SettingsFragment: PreferenceFragment() {

    companion object {
        const val SHARED_PREFERENCES_NAME = "default"
        const val SHARED_PREFERENCES_THEME = "theme"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceManager.sharedPreferencesName = SettingsFragment.SHARED_PREFERENCES_NAME

        addPreferencesFromResource(R.xml.preferences)
    }
}