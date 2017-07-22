package soutvoid.com.DsrWeatherApp.ui.screen.locations.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.CompoundButton
import soutvoid.com.DsrWeatherApp.R

/**
 * кнопка для добавления/удаления из favorites
 * автоматически переключается при нажатии
 * чтобы использовать свой обработчик переключения используйте OnCheckedChangeListener (не OnClickListener!)
 */
class FavoriteButton : CompoundButton {

    var favoriteDrawableId: Int = 0

    var favoriteBorderDrawableId: Int = 0

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        isClickable = true
    }

    private fun maybeInitDrawables() {
        val typedValue = TypedValue()
        if (favoriteDrawableId == 0) {
            context.theme.resolveAttribute(R.attr.themedFavoriteDrawable, typedValue, true)
            favoriteDrawableId = typedValue.resourceId
        }
        if (favoriteBorderDrawableId == 0) {
            context.theme.resolveAttribute(R.attr.themedFavoriteBorderDrawable, typedValue, true)
            favoriteBorderDrawableId = typedValue.resourceId
        }
    }

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)

        maybeInitDrawables()

        if (checked)
            buttonDrawable = ContextCompat.getDrawable(context, favoriteDrawableId)
        else
            buttonDrawable = ContextCompat.getDrawable(context, favoriteBorderDrawableId)
    }
}