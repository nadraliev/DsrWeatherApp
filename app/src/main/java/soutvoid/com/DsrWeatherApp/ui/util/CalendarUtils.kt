package soutvoid.com.DsrWeatherApp.ui.util

import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

object CalendarUtils {

    fun getFormattedDate(seconds: Long, locale: Locale = Locale.getDefault()) : String {
        val calendar: Calendar = Calendar.getInstance(locale)
        calendar.timeInMillis = seconds*1000
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("EEE, MMM d", locale)
        return simpleDateFormat.format(calendar.time)
    }

}