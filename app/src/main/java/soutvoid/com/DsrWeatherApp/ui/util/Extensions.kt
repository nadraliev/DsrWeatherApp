package soutvoid.com.DsrWeatherApp.ui.util

import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(resId: Int) : View {
        return LayoutInflater.from(context).inflate(resId, this, false)
}

fun Resources.Theme.getThemeColor(attr: Int) : Int {
        val typedValue : TypedValue = TypedValue()
        this.resolveAttribute(attr, typedValue, true)
        return typedValue.data
}
