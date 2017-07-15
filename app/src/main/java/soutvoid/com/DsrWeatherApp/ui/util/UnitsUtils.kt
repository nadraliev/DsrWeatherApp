package soutvoid.com.DsrWeatherApp.ui.util

import android.content.Context
import android.content.SharedPreferences
import soutvoid.com.DsrWeatherApp.R

object UnitsUtils {

    fun getPressureUnits(context: Context) : String {
        return context.getString(R.string.pressureUnit)
    }

    fun getVelocityUnits(context: Context) : String {
        var metric: Boolean = isMetricalPreferred(context)
        return if (metric) context.getString(R.string.metrical_velocity)
            else context.getString(R.string.imperial_velocity)
    }

    fun getDegreesUnits(context: Context) : String {
        var metric: Boolean = isMetricalPreferred(context)
        return if (metric) "\u2103" else "\u2109"
    }

    fun isMetricalPreferred(context: Context) : Boolean {
        val sharedPreferences: SharedPreferences = context.getDefaultPreferences()
        return sharedPreferences.getBoolean("units", true)
    }

}