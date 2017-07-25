package soutvoid.com.DsrWeatherApp.ui.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object CalendarUtils {

    /**
     * возвращает дату в виде "Пон, Июл 12"
     */
    fun getFormattedDate(seconds: Long, locale: Locale = Locale.getDefault()) : String {
        val calendar: Calendar = Calendar.getInstance(locale)
        calendar.timeInMillis = seconds*1000
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("EEE, MMM d", locale)
        return simpleDateFormat.format(calendar.time)
    }


    /**
     * возвращает день недели в виде "Пон"
     */
    fun getShortDayOfWeek(seconds: Long, locale: Locale = Locale.getDefault()): String {
        val calendar: Calendar = Calendar.getInstance(locale)
        calendar.timeInMillis = seconds*1000
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("EEE", locale)
        return simpleDateFormat.format(calendar.time)
    }

    /**
     * возввращает дату в виде "31/12"
     */
    fun getNumericDate(seconds: Long, locale: Locale = Locale.getDefault()): String {
        val calendar: Calendar = Calendar.getInstance(locale)
        calendar.timeInMillis = seconds*1000
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("d/MM", locale)
        return simpleDateFormat.format(calendar.time)
    }

    /**
     * возвращает время в виде "2PM" или "14:00"
     */
    fun getFormattedHour(seconds: Long, locale: Locale = Locale.getDefault()): String {
        val date = Date(seconds * 1000)
        return DateFormat.getTimeInstance(DateFormat.SHORT, locale).format(date)
    }

    fun isToday(seconds: Long, locale: Locale = Locale.getDefault()): Boolean {
        val calendarNow = Calendar.getInstance(locale)
        val calendarSubject = Calendar.getInstance(locale)
        calendarSubject.timeInMillis = seconds * 1000
        return calendarNow.get(Calendar.YEAR) == calendarSubject.get(Calendar.YEAR) &&
                calendarNow.get(Calendar.DAY_OF_YEAR) == calendarSubject.get(Calendar.DAY_OF_YEAR)
    }

    fun isTomorrow(seconds: Long, locale: Locale = Locale.getDefault()): Boolean {
        val calendarNow = Calendar.getInstance(locale)
        val calendarSubject = Calendar.getInstance(locale)
        calendarSubject.timeInMillis = seconds * 1000

        val daysInYear = getDaysInYear(calendarNow.get(Calendar.YEAR))

        return calendarNow.get(Calendar.YEAR) == calendarSubject.get(Calendar.YEAR) &&
                (calendarNow.get(Calendar.DAY_OF_YEAR) + 1) % daysInYear == calendarSubject.get(Calendar.DAY_OF_YEAR)
    }

    fun getDaysInYear(year: Int): Int {
        if (year % 4 == 0)
            return 366
        else return 365
    }

}