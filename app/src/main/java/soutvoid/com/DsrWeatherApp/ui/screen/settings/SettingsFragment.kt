package soutvoid.com.DsrWeatherApp.ui.screen.settings

import android.os.Bundle
import android.preference.PreferenceFragment
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.common.message.MessagePresenter

class SettingsFragment(): PreferenceFragment() {

    companion object {
        const val SHARED_PREFERENCES_NAME = "default"
        const val SHARED_PREFERENCES_THEME = "theme"
        const val SHARED_PREFERENCES_DARK_THEME = "prefer_dark"
    }

    lateinit var messagePresenter: MessagePresenter


    constructor(messagePresenter: MessagePresenter) : this() {
        this.messagePresenter = messagePresenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceManager.sharedPreferencesName = SettingsFragment.SHARED_PREFERENCES_NAME

        addPreferencesFromResource(R.xml.preferences)
    }
}