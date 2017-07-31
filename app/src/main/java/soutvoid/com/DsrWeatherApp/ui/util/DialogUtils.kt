package soutvoid.com.DsrWeatherApp.ui.util

import android.app.Dialog
import android.content.Context
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.screen.newLocation.NewLocationActivityView

object DialogUtils {

    fun createSimpleListDialog(context: Context,
                               @StringRes titleId: Int = -1,
                               items: List<String>,
                               listener: (position: Int) -> Unit) : Dialog {
        val dialogBuilder = AlertDialog.Builder(context)
        if (titleId != -1)
            dialogBuilder.setTitle(titleId)
        dialogBuilder.setAdapter(
                ArrayAdapter(context, android.R.layout.simple_list_item_1, items)
        ) {
            dialogInterface, i -> listener(i)
        }
        return dialogBuilder.create()
    }

    fun showSimpleListDialog(context: Context,
                             @StringRes titleId: Int = -1,
                             items: List<String>,
                             listener: (position: Int) -> Unit) {
        createSimpleListDialog(context, titleId, items, listener).show()
    }

    fun showNoLocationsDialog(context: Context) {
        val message = context.getString(R.string.add_location)
        showSimpleListDialog(context, items = listOf(message))  { NewLocationActivityView.start(context) }
    }

    fun showLocationsDialog(context: Context,
                            locationsNames: List<String>,
                            listener: (position: Int) -> Unit) {
        showSimpleListDialog(context, items =  locationsNames, listener = listener)
    }

}