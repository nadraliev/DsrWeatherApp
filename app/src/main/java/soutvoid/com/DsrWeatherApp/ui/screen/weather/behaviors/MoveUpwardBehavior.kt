package soutvoid.com.DsrWeatherApp.ui.screen.weather.behaviors

import android.content.Context
import android.os.Build
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import soutvoid.com.DsrWeatherApp.ui.util.SnackbarDismissedListener
import soutvoid.com.DsrWeatherApp.ui.util.ViewDetachedListener
import soutvoid.com.DsrWeatherApp.ui.util.ifNotNull

class MoveUpwardBehavior: CoordinatorLayout.Behavior<View> {

    constructor() : super()
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    override fun layoutDependsOn(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean {
        ifNotNull(child, dependency) {child, dependency ->
            val translationY = minOf(0, -dependency.height)
            setBottomMargin(-translationY, child)
            (dependency as? Snackbar.SnackbarLayout)?.let { setSnackbarListener(it, child, dependency) }
            return true
        }
        return false
    }

    private fun setBottomMargin(value: Int, view: View) {
        val lp = CoordinatorLayout.LayoutParams(view.layoutParams)
        lp.bottomMargin = value
        view.layoutParams = lp
    }

    private fun setSnackbarListener(snackbar: Snackbar.SnackbarLayout,
                                    child: View?,
                                    dependency: View?) {
        snackbar.addOnAttachStateChangeListener(ViewDetachedListener {
            ifNotNull(child, dependency) {child, dependency ->
                setBottomMargin(0, child)
            }
        })
    }
}