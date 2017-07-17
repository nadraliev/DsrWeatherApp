package soutvoid.com.DsrWeatherApp.ui.util

import android.content.Context
import android.content.SharedPreferences
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.interactor.util.Units

object UnitsUtils {

    /**
     * @return строковое представление единицы измерения атмосферного давления
     */
    fun getPressureUnits(context: Context) : String {
        return context.getString(R.string.pressureUnit)
    }

    /**
     * @return строковое представление единицы измерения скорости
     * в зависимости от предпочтительной системы измерения
     */
    fun getVelocityUnits(context: Context) : String {
        val metric: Boolean = isMetricalPreferred(context)
        return if (metric) context.getString(R.string.metrical_velocity)
            else context.getString(R.string.imperial_velocity)
    }

    /**
     * @return строковое представление единицы измерения температуры
     * в зависимости от предпочтительной системы измерения
     */
    fun getDegreesUnits(context: Context) : String {
        val metric: Boolean = isMetricalPreferred(context)
        return if (metric) "\u2103" else "\u2109"
    }

    /**
     * @return является ли метрическая система предпочтительной
     */
    fun isMetricalPreferred(context: Context) : Boolean {
        val sharedPreferences: SharedPreferences = context.getDefaultPreferences()
        return sharedPreferences.getBoolean("units", true)
    }

    fun getPreferredUnits(context: Context) : Units {
        return if (isMetricalPreferred(context)) Units.METRIC else Units.IMPERIAL
    }

}