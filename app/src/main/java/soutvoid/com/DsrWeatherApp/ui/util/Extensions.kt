package soutvoid.com.DsrWeatherApp.ui.util

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import soutvoid.com.DsrWeatherApp.domain.OneDayForecast
import soutvoid.com.DsrWeatherApp.domain.OneMomentForecast
import soutvoid.com.DsrWeatherApp.ui.screen.main.data.TimeOfDay
import java.util.*

fun ViewGroup.inflate(resId: Int): View {
    return LayoutInflater.from(context).inflate(resId, this, false)
}

fun Resources.Theme.getThemeColor(attr: Int): Int {
    val typedValue: TypedValue = TypedValue()
    this.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}

fun Context.getDefaultPreferences(): SharedPreferences {
    return this.getSharedPreferences("default", Context.MODE_PRIVATE)
}

/**
 * разобрать моментные данные по дням
 */
fun List<OneDayForecast>.fillDetails(oneMomentForecasts: List<OneMomentForecast>, timeZone: TimeZone = TimeZone.getDefault()) {
    this.forEach { it.detailedForecasts = ArrayList() }
    var currentDayIndex = 0
    var currentDay: Long = (this[currentDayIndex].dateTime + timeZone.rawOffset / 1000) / 60 / 60 / 24
    var iteratedDay: Long = 0
    oneMomentForecasts.forEach {
        iteratedDay = (it.timeOfData + timeZone.rawOffset / 1000) / 60 / 60 / 24
        if (currentDay < iteratedDay) {
            currentDayIndex++
            currentDay = (this[currentDayIndex].dateTime + timeZone.rawOffset / 1000) / 60 / 60 / 24
        }
        this[currentDayIndex].detailedForecasts.add(it)
    }
}

/**
 * возвращает самый ближний к выбранному времени (утро, день, вечер, ночь) прогноз (время по @see TimeOfDay)
 * предполагается, что все прогнозы в листе относятся к одному дню
 */
fun List<OneMomentForecast>.getTimeOfDayForecast(timeOfDay: TimeOfDay,
                                                 locale: Locale = Locale.getDefault())
        : OneMomentForecast {
    val calendar = Calendar.getInstance(locale)
    val result = this.minBy {
        calendar.timeInMillis = it.timeOfData * 1000
        return@minBy Math.abs(timeOfDay.hours - calendar.get(Calendar.HOUR_OF_DAY))
    }
    return if (result != null) result else this[0]
}
