package soutvoid.com.DsrWeatherApp.ui.util

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.screen.settings.SettingsFragment

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
    return this.getSharedPreferences(SettingsFragment.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
}

/**
 * чтобы добавить тему, напиши стиль в res-main styles, добавь записи в res-settings strings для экрана настроек, добавь опцию сюда
 * @return id текущей темы
 */
fun SharedPreferences.getPreferredThemeId(): Int {
    val themeNumber = getString(SettingsFragment.SHARED_PREFERENCES_THEME, "0").toInt()
    when(themeNumber) {
        1 -> return R.style.AppTheme_Dark
        2 -> return R.style.AppTheme_Purple
        3 -> return R.style.AppTheme_PurpleInverse
        else -> return R.style.AppTheme_Light
    }
}
