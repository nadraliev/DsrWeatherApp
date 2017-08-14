package soutvoid.com.DsrWeatherApp.ui.util

import android.animation.Animator
import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.support.annotation.StringRes
import android.support.v4.app.FragmentManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import io.realm.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.domain.triggers.SavedTrigger
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.ConditionExpression
import soutvoid.com.DsrWeatherApp.domain.triggers.condition.ConditionName
import soutvoid.com.DsrWeatherApp.ui.receivers.RequestCode
import soutvoid.com.DsrWeatherApp.ui.screen.main.settings.SettingsFragment
import io.realm.RealmObject.deleteFromRealm
import io.realm.RealmObject
import io.realm.RealmList
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import java.math.BigInteger
import java.security.SecureRandom
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

fun View.spToPx(sp: Float): Int {
    val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics).toInt()
    return px
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

fun <T> T?.ifNotNullOr(alt: () -> T): T {
    if (this == null)
        return alt()
    else
        return this
}

fun <T> List<T>.plusElementFront(element: T) : List<T> {
    val newList = this.toMutableList()
    newList.add(0, element)
    return newList.toList()
}

fun <T> realmListOf(elements: Iterable<T>): RealmList<T> where T: RealmModel{
    val list = RealmList<T>()
    list.addAll(elements)
    return list
}

/**
 * получить сохраненные в бд триггеры по списку id
 */
fun getSavedTriggers(triggersIds: IntArray = intArrayOf()): List<SavedTrigger> {
    val realm = Realm.getDefaultInstance()
    var realmResults: RealmResults<SavedTrigger>?
    var list = emptyList<SavedTrigger>()
    if (triggersIds.isEmpty())
        realmResults = realm.where(SavedTrigger::class.java).findAll()
    else
        realmResults = realm.where(SavedTrigger::class.java).`in`("id", triggersIds.toTypedArray()).findAll()

    if (realmResults != null)
        list = realm.copyFromRealm(realmResults)
    realm.close()
    return list
}

/**
 * сохранить внесенные изменения в бд
 */
private fun updateDbTriggers(triggers: List<SavedTrigger>) {
    val realm = Realm.getDefaultInstance()
    realm.executeTransaction { it.copyToRealmOrUpdate(triggers) }
    realm.close()
}

fun getNewRequestCode(): Int {
    val realm = Realm.getDefaultInstance()
    val requestCode = RequestCode()
    realm.executeTransaction { it.copyToRealm(requestCode) }
    realm.close()
    return requestCode.value
}

fun getAllRequestCodes(): List<Int> {
    val realm = Realm.getDefaultInstance()
    val realmResults = realm.where(RequestCode::class.java).findAll()
    var results = emptyList<Int>()
    realmResults?.let { results = realm.copyFromRealm(realmResults).map { it.value } }
    realm.close()
    return results
}

fun deleteAllRequestCodes() {
    val realm = Realm.getDefaultInstance()
    realm.executeTransaction { realm.where(RequestCode::class.java).findAll().deleteAllFromRealm() }
    realm.close()
}

fun getAllSavedLocations(): List<SavedLocation> {
    val realm = Realm.getDefaultInstance()
    val realmResults = realm.where(SavedLocation::class.java).findAll()
    var results: List<SavedLocation> = emptyList()
    realmResults?.let { results = realm.copyFromRealm(realmResults) }
    realm.close()
    return results
}

fun getRandomString(): String {
    val secureRandom = SecureRandom()
    return BigInteger(130, secureRandom).toString(32)
}

fun FragmentManager.clearBackStack() {
    kotlin.repeat(backStackEntryCount) {popBackStack()}
}

fun secondsToHours(seconds: Long): Long =
        seconds / 60 / 60

fun secondsToDays(seconds: Long): Long =
        secondsToHours(seconds) / 24

fun getHourOfDay(seconds: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = seconds * 1000
    return calendar.get(Calendar.HOUR_OF_DAY)
}

