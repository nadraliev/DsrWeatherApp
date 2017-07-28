package soutvoid.com.DsrWeatherApp.ui.screen.newTrigger.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.util.dpToPx

class TextLinearLayout: LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    fun addLine(str: String, marginTop: Int = 0, textSize: Float = 14f, textColor: Int = -1): View {
        val view = TextView(context)
        val layoutParams = MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT)
        layoutParams.topMargin = dpToPx(marginTop.toDouble()).toInt()
        view.layoutParams = layoutParams
        view.text = str
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        if (textColor != -1)
            view.setTextColor(textColor)
        addView(view)
        return view
    }
}