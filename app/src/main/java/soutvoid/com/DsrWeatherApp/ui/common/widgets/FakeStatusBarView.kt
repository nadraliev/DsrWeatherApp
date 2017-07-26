package soutvoid.com.DsrWeatherApp.ui.common.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import soutvoid.com.DsrWeatherApp.ui.util.dpToPx
import soutvoid.com.DsrWeatherApp.util.SdkUtil

class FakeStatusBarView: View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()
        setSelfHeight()
    }

    init {
        if (SdkUtil.supportsKitkat())
            visibility = VISIBLE
        else visibility = GONE
    }

    private fun setSelfHeight() {
        if (layoutParams == null)
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(25.0).toInt())
        else layoutParams.height = dpToPx(25.0).toInt()
    }

}
