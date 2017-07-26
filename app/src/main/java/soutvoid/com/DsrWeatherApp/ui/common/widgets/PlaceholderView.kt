package soutvoid.com.DsrWeatherApp.ui.common.widgets

import android.content.Context
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import soutvoid.com.DsrWeatherApp.R
import kotlinx.android.synthetic.main.view_placeholder.view.*

class PlaceholderView: FrameLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        inflate(context, R.layout.view_placeholder, this)
    }

    private fun setupPlaceholder(message: String = "", @DrawableRes image: Int = 0) {
        placeholder_message.text = message

        if (image != 0)
            placeholder_image.setImageResource(image)
    }

    fun show(message: String) {
        visibility = VISIBLE
        setupPlaceholder(message)
    }

    fun show(@DrawableRes image: Int) {
        visibility = VISIBLE
        setupPlaceholder(image = image)
    }

    fun show(message: String, @DrawableRes image: Int) {
        visibility = VISIBLE
        setupPlaceholder(message, image)
    }

    fun hide() {
        visibility = INVISIBLE
    }
}