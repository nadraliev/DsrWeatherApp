package soutvoid.com.DsrWeatherApp.ui.util

import android.animation.Animator
import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.support.annotation.StringRes
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.ConditionExpression
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.ConditionName
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
    var themeNumber = getString(SettingsFragment.SHARED_PREFERENCES_THEME, "0").toInt()
    val preferDark = isDarkThemePreferred()
    if (preferDark)
        themeNumber += 100  //dark themes "zone" is 1xx
    when(themeNumber) {
        0 -> return R.style.AppTheme_Light
        1 -> return R.style.AppTheme_PurpleLight
        2 -> return R.style.AppTheme_GreenLight
        3 -> return R.style.AppTheme_RedLight
        4 -> return R.style.AppTheme_BlueLight
        5 -> return R.style.AppTheme_PinkLight
        6 -> return R.style.AppTheme_DeepPurpleLight
        7 -> return R.style.AppTheme_CyanLight
        8 -> return R.style.AppTheme_TealLight
        9 -> return R.style.AppTheme_YellowLight
        10 -> return R.style.AppTheme_OrangeLight
        11 -> return R.style.AppTheme_BrownLight


        100 -> return R.style.AppTheme_Dark
        101 -> return R.style.AppTheme_PurpleDark
        102 -> return R.style.AppTheme_GreenDark
        103 -> return R.style.AppTheme_RedDark
        104 -> return R.style.AppTheme_BlueDark
        105 -> return R.style.AppTheme_PinkDark
        106 -> return R.style.AppTheme_Deep_purpleDark
        107 -> return R.style.AppTheme_CyanDark
        108 -> return R.style.AppTheme_TealDark
        109 -> return R.style.AppTheme_YellowDark
        110 -> return R.style.AppTheme_OrangeDark
        111 -> return R.style.AppTheme_BrownDark

        else -> return R.style.AppTheme_Light
    }
}

fun SharedPreferences.isDarkThemePreferred(): Boolean {
    return getBoolean(SettingsFragment.SHARED_PREFERENCES_DARK_THEME, false)
}

fun View.dpToPx(dp: Double): Double {
    val scale = context.resources.displayMetrics.density
    return dp * scale + 0.5f
}

@TargetApi(21)
fun View.createFullScreenCircularReveal(startX: Int, startY: Int): Animator {
    return ViewAnimationUtils.createCircularReveal(
            this,
            startX,
            startY,
            0f,
            maxOf(this.measuredHeight, this.measuredWidth).toFloat()
    )
}

fun ConditionName.getNiceNameStringId(): Int {
    when(this) {
        ConditionName.temp -> return R.string.temperature
        ConditionName.humidity -> return R.string.humidity
        ConditionName.clouds -> return R.string.clouds
        ConditionName.pressure -> return R.string.pressure
        ConditionName.wind_direction -> return R.string.wind_direction
        ConditionName.wind_speed -> return R.string.wind_speed
    }
}

fun ConditionExpression.getNiceStringId(): Int {
    when(this) {
        ConditionExpression.gt -> return R.string.more_than
        ConditionExpression.lt -> return R.string.less_than
        else -> return R.string.equals
    }
}

/**
 * возвращает @param [alt], если this == null
 */
fun <T> T?.ifNotNullOr(alt: T): T {
    if (this == null)
        return alt
    else
        return this
}
