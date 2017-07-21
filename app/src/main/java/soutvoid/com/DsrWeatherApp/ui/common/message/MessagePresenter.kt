package soutvoid.com.DsrWeatherApp.ui.common.message


import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.TextView

import com.agna.ferro.mvp.component.provider.ActivityProvider
import com.agna.ferro.mvp.component.scope.PerScreen
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.interactor.util.TransformUtil
import javax.inject.Inject


@PerScreen
class MessagePresenter @Inject
constructor(private val activityProvider: ActivityProvider) {

    fun show(@StringRes stringId: Int) {
        val v = view
        val snackbar = Snackbar.make(v, stringId, Snackbar.LENGTH_LONG)
        setMultilineSnackbar(snackbar)
        snackbar.show()
    }

    fun show(message: String) {
        val v = view
        val snackbar = Snackbar.make(v, TransformUtil.sanitizeHtmlString(message), Snackbar.LENGTH_LONG)
        setMultilineSnackbar(snackbar)
        snackbar.show()
    }

    fun show(@IdRes parentViewId: Int, @StringRes stringId: Int) {
        val v = activityProvider.get().findViewById<View>(parentViewId)
        val snackbar = Snackbar.make(v, stringId, Snackbar.LENGTH_LONG)
        setMultilineSnackbar(snackbar)
        snackbar.show()
    }

    private fun setMultilineSnackbar(snackbar: Snackbar) {
        val textView = snackbar.view.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
        textView.maxLines = 4
    }

    private fun showWithAction(@StringRes stringId: Int, @StringRes actionStringId: Int, listener: (v: View) -> Unit) {
        val v = view
        val snackbar = Snackbar.make(v, stringId, Snackbar.LENGTH_LONG)
        setMultilineSnackbar(snackbar)
        snackbar.setAction(actionStringId, listener)
    }

    private fun showWithAction(message: String, actionName: String, listener: (v: View) -> Unit) {
        val v = view
        val snackbar = Snackbar.make(v, message, Snackbar.LENGTH_LONG)
        setMultilineSnackbar(snackbar)
        snackbar.setAction(actionName, listener)
    }

    /**
     * Для того, чтобы срабатывал Behavior на появление Snackbar,
     * нужно чтобы корневым контейнером экрана был CoordinatorLayout
     * с id = "coordinator_content". Если такого контейнера нет -
     * привязываемся к корневому элементу активити
     */
    private val view: View
        get() {
            var v: View? = activityProvider.get().findViewById(R.id.coordinator_content)
            if (v == null) {
                v = activityProvider.get().findViewById<View>(android.R.id.content)
            }
            return v!!
        }

}
