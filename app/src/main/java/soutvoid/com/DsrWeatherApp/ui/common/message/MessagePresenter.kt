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
import soutvoid.com.DsrWeatherApp.ui.util.getDefaultPreferences
import soutvoid.com.DsrWeatherApp.ui.util.isDarkThemePreferred
import javax.inject.Inject


@PerScreen
class MessagePresenter @Inject
constructor(private val activityProvider: ActivityProvider) {

    var backgroundColorId: Int

    init {
        if (activityProvider.get().getDefaultPreferences().isDarkThemePreferred())
            backgroundColorId = R.color.grey_900
        else
            backgroundColorId = R.color.grey_400
    }

    fun show(@StringRes stringId: Int): Snackbar {
        val v = view
        val snackbar = Snackbar.make(v, stringId, Snackbar.LENGTH_LONG)
        setMultilineSnackbar(snackbar)
        setSnackbarColor(snackbar)
        snackbar.show()
        return snackbar
    }

    fun show(message: String): Snackbar {
        val v = view
        val snackbar = Snackbar.make(v, TransformUtil.sanitizeHtmlString(message), Snackbar.LENGTH_LONG)
        setMultilineSnackbar(snackbar)
        setSnackbarColor(snackbar)
        snackbar.show()
        return snackbar
    }

    fun show(@IdRes parentViewId: Int, @StringRes stringId: Int): Snackbar {
        val v = activityProvider.get().findViewById<View>(parentViewId)
        val snackbar = Snackbar.make(v, stringId, Snackbar.LENGTH_LONG)
        setMultilineSnackbar(snackbar)
        setSnackbarColor(snackbar)
        snackbar.show()
        return snackbar
    }

    private fun setMultilineSnackbar(snackbar: Snackbar) {
        val textView = snackbar.view.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
        textView.maxLines = 4
    }

    private fun setSnackbarColor(snackbar: Snackbar) {
        snackbar.view.setBackgroundResource(backgroundColorId)
    }

    fun showWithAction(@StringRes stringId: Int, @StringRes actionStringId: Int, listener: (v: View) -> Unit): Snackbar {
        val v = view
        val snackbar = Snackbar.make(v, stringId, Snackbar.LENGTH_LONG)
        setMultilineSnackbar(snackbar)
        setSnackbarColor(snackbar)
        snackbar.setAction(actionStringId, listener)
        snackbar.show()
        return snackbar
    }

    fun showWithAction(message: String, actionName: String, listener: (v: View) -> Unit): Snackbar {
        val v = view
        val snackbar = Snackbar.make(v, message, Snackbar.LENGTH_LONG)
        setMultilineSnackbar(snackbar)
        setSnackbarColor(snackbar)
        snackbar.setAction(actionName, listener)
        snackbar.show()
        return snackbar
    }

    fun showWithAction(@StringRes stringId: Int, actionName: String, listener: (v: View) -> Unit): Snackbar {
        val v = view
        val snackbar = Snackbar.make(v, stringId, Snackbar.LENGTH_LONG)
        setMultilineSnackbar(snackbar)
        setSnackbarColor(snackbar)
        snackbar.setAction(actionName, listener)
        snackbar.show()
        return snackbar
    }

    fun showIndefinite(@StringRes stringId: Int): Snackbar {
        val v = view
        val snackbar = Snackbar.make(v, stringId, Snackbar.LENGTH_INDEFINITE)
        setMultilineSnackbar(snackbar)
        setSnackbarColor(snackbar)
        snackbar.show()
        return snackbar
    }

    fun showIndefinite(string: String): Snackbar {
        val v = view
        val snackbar = Snackbar.make(v, string, Snackbar.LENGTH_INDEFINITE)
        setMultilineSnackbar(snackbar)
        setSnackbarColor(snackbar)
        snackbar.show()
        return snackbar
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
