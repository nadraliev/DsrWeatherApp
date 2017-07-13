package soutvoid.com.DsrWeatherApp.ui.util

import android.content.Context
import soutvoid.com.DsrWeatherApp.R

object WindUtils {

    fun getByDegrees(degrees: Double, context: Context) : String {
        return context.resources.getStringArray(R.array.directions)[ Math.round(degrees % 360 / 45).toInt() % 8 ]
    }

}