package soutvoid.com.DsrWeatherApp.ui.util

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import soutvoid.com.DsrWeatherApp.domain.OneDayForecast
import soutvoid.com.DsrWeatherApp.domain.ThreeHoursForecast
import soutvoid.com.DsrWeatherApp.ui.screen.main.data.TimeOfDay
import java.util.*

fun ViewGroup.inflate(resId: Int): View {
    return LayoutInflater.from(context).inflate(resId, this, false)
}

/**
 * позволяет получить цвет из текущей темы
 * @param [attr] имя аттрибута из темы
 * @return цвет, полученный из темы
 */
fun Resources.Theme.getThemeColor(attr: Int): Int {
    val typedValue: TypedValue = TypedValue()
    this.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}

/**
 * @return общий SharedPreferences для любого контекста
 */
fun Context.getDefaultPreferences(): SharedPreferences {
    return this.getSharedPreferences("default", Context.MODE_PRIVATE)
}
