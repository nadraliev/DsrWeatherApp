package soutvoid.com.DsrWeatherApp.ui.screen.cities.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.CompoundButton
import soutvoid.com.DsrWeatherApp.R

class FavoriteButton : CompoundButton {

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)

    init {
        buttonDrawable = ContextCompat.getDrawable(context, R.drawable.ic_settings)
    }

    override fun toggle() {
        super.toggle()
    }
}