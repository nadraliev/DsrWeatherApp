package soutvoid.com.DsrWeatherApp.ui.screen.settings

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceFragment
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.screen.locations.LocationsActivityView

class SettingsFragment: PreferenceFragment() {

    companion object {
        const val SHARED_PREFERENCES_NAME = "default"
        const val SHARED_PREFERENCES_THEME = "theme"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceManager.sharedPreferencesName = SettingsFragment.SHARED_PREFERENCES_NAME
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener { preferences, s ->
            if (s == SHARED_PREFERENCES_THEME) {
                val intent = Intent(activity, LocationsActivityView::class.java)
                activity.startActivity(intent
                )
            }
        }

        addPreferencesFromResource(R.xml.preferences)
    }
}