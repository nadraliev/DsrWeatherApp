package soutvoid.com.DsrWeatherApp.ui.screen.locations.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.CompoundButton
import soutvoid.com.DsrWeatherApp.R

/**
 * кнопка для добавления/удаления из favorites
 * автоматически переключается при нажатии
 * чтобы использовать свой обработчик переключения используйте OnCheckedChangeListener (не OnClickListener!)
 */
class FavoriteButton : CompoundButton {

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)

    init {
        isClickable = true
    }

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)

        if (checked)
            buttonDrawable = ContextCompat.getDrawable(context, R.drawable.ic_favorite_light)
        else
            buttonDrawable = ContextCompat.getDrawable(context, R.drawable.ic_favorite_border_light)
    }
}