package soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets.timeDialog.data

import android.content.Context
import io.realm.RealmObject
import soutvoid.com.DsrWeatherApp.R
import java.io.Serializable

open class NotificationTime(
        var value: Int = 0,
        var unit: Int = 0   //пришлось хранить индекс enum вместо самого enum для хранения в бд
) : RealmObject(), Serializable
{
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
        return "$value ${NotificationTime.Unit.values()[unit].getNiceString(context, value)}"
    }
}