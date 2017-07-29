package soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets

import android.content.Context
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import soutvoid.com.DsrWeatherApp.ui.util.dpToPx

class TextLinearLayout : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun createLine(str: String,
                           marginBottom: Int = 14,
                           textSizeSp: Float = 14f,
                           textColor: Int = -1): TextView {
        val view = TextView(context)
        val layoutParams = MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT)
        layoutParams.bottomMargin = dpToPx(marginBottom.toDouble()).toInt()
        view.layoutParams = layoutParams
        view.text = str
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSp)
        if (textColor != -1)
            view.setTextColor(textColor)
        return view
    }

    fun addLineToIndex(index: Int,
                       str: String,
                       marginBottom: Int = 14,
                       textSize: Float = 14f,
                       textColor: Int = -1)
            : TextView {
        val view = createLine(str, marginBottom, textSize, textColor)
        addView(view, index)
        return view
    }

    fun addLineToIndex(index: Int,
                       @StringRes strId: Int,
                       marginBottom: Int = 14,
                       textSize: Float = 14f,
                       textColor: Int = -1)
            : TextView {
        return addLineToIndex(index,
                context.getString(strId),
                marginBottom,
                textSize,
                textColor)
    }

    fun addLineToTop(str: String,
                     marginBottom: Int = 14,
                     textSizeSp: Float = 14f,
                     textColor: Int = -1)
            : TextView {
        return addLineToIndex(0, str, marginBottom, textSizeSp, textColor)
    }

    fun addLineToTop(@StringRes strId: Int,
                     marginBottom: Int = 14,
                     textSize: Float = 14f,
                     textColor: Int = -1)
            : TextView {
        return addLineToTop(context.getString(strId),
                marginBottom,
                textSize,
                textColor)
    }

    fun getLine(index: Int): TextView {
        return getChildAt(index) as TextView
    }

    fun removeLine(index: Int) {
        removeViewAt(index)
    }

    fun size() = childCount
}