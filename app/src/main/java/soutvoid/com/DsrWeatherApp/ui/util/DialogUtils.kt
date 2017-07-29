package soutvoid.com.DsrWeatherApp.ui.util

import android.content.Context
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import soutvoid.com.DsrWeatherApp.R

object DialogUtils {

    fun showSimpleListDialog(context: Context,
                             @StringRes titleId: Int,
                             items: List<String>,
                             listener: (position: Int) -> Unit) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle(titleId)
        dialogBuilder.setAdapter(
                ArrayAdapter(context, android.R.layout.simple_list_item_1, items)
        ) {
            dialogInterface, i -> listener(i)
        }
        dialogBuilder.create().show()
    }

    fun showNoLocationsDialog(context: Context, listener: (position: Int) -> Unit ) {
        val message = context.getString(R.string.add_location)
        showSimpleListDialog(context, R.string.no_locations, listOf(message), listener)
    }

    fun showLocationsDialog(context: Context,
                            locationsNames: List<String>,
                            listener: (position: Int) -> Unit) {
        showSimpleListDialog(context, R.string.choose_location, locationsNames, listener)
    }

}