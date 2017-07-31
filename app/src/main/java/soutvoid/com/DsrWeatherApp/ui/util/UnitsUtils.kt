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
        return getPreferredUnits(context).toString().toLowerCase() == "metric"
    }

    fun getPreferredUnits(context: Context) : Units {
        val sharedPreferences: SharedPreferences = context.getDefaultPreferences()
        val str = sharedPreferences.getString("units", "0")
        return Units.values()[str.toInt()]
    }

    fun celsiusToKelvin(value: Double): Double {
        return value + 273.15
    }

    fun fahrenheitToKelvin(value: Double): Double {
        return (value + 459.67) * 5 / 9
    }

    fun preferredUnitToKelvin(context: Context, value: Double): Double {
        if (isMetricalPreferred(context))
            return celsiusToKelvin(value)
        else
            return fahrenheitToKelvin(value)
    }

    fun kelvinToCelsius(value: Double): Double {
        return value - 273.15
    }

    fun kelvinToFahrenheit(value: Double): Double {
        return value * 9 / 5 - 459.67
    }

    fun kelvinToPreferredUnit(context: Context, value: Double): Double {
        if (isMetricalPreferred(context))
            return kelvinToCelsius(value)
        else
            return kelvinToFahrenheit(value)
    }
}