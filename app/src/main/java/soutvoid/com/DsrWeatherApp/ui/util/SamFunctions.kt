package soutvoid.com.DsrWeatherApp.ui.util

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

fun LocationListener(listener: (location: Location) -> Unit): LocationListener = object : LocationListener {
    override fun onLocationChanged(p0: Location?){
        p0?.let(listener)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}
}

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