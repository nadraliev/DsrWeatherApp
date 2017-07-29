package soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.timeDialog.data

import android.content.Context
import soutvoid.com.DsrWeatherApp.R

data class NotificationTime(
        var value: Int,
        var unit: Unit
) {
    enum class Unit{
        days, hours;

        fun getNiceString(context: Context, count: Int = 10): String {
            when(this) {
                days -> return context.resources.getQuantityString(R.plurals.days, count)
                hours -> return context.resources.getQuantityString(R.plurals.hours, count)
            }
        }
    }

    fun getNiceString(context: Context): String {
        return "$value ${unit.getNiceString(context, value)}"
    }
}