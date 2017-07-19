package soutvoid.com.DsrWeatherApp.ui.util

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import soutvoid.com.DsrWeatherApp.app.log.Logger

/**
 * функции, позволяющие реализовать одну необходимую функцию, не имплементируя весь интерфейс
 */


/**
 * листенер получения нового положения через геолокацию
 */
fun LocationListener(listener: (location: Location) -> Unit): LocationListener = object : LocationListener {
    override fun onLocationChanged(p0: Location?){
        p0?.let(listener)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}
}

/**
 * callback, вызываемый при свайпе элемента RecyclerView
 * @param [swipeDirections] направления свайпа
 */
fun SimpleItemSwipeCallback(swipeDirections: Int, callback: (viewHolder: RecyclerView.ViewHolder, direction: Int) -> Unit)
        : ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, swipeDirections) {
    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        //ignored
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        viewHolder?.let { callback(it, direction) }
    }
}

/**
 * листенер, вызываемый PlaceAutocompleteFragment при выборе места пользователем
 */
fun PlaceSelectionListener(listener: (place: Place) -> Unit) : PlaceSelectionListener =
        object : PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place?) {
                p0?.let { listener(it) }
            }

            override fun onError(p0: Status?) {
                p0?.statusMessage?.let { Logger.d(it) }
            }
        }