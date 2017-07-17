package soutvoid.com.DsrWeatherApp.ui.screen.map

import android.location.Location
import com.agna.ferro.mvp.component.scope.PerScreen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.util.SdkUtil
import javax.inject.Inject

@PerScreen
class MapActivityPresenter @Inject constructor(errorHandler: ErrorHandler):
    BasePresenter<MapActivityView>(errorHandler) {

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

    }

    fun locationPermissionGranted(granted: Boolean) {
        if (granted)
            view.requestLocation()
        else setPositionMoscow()
    }

    fun locationChanged(location: Location) {
        setPositionCurrent(location)
    }

    fun setPositionMoscow() {
        view.setMapPosition(LatLng(55.7512, 37.6184))
    }

    fun setPositionCurrent(location: Location) {
        view.setMapPosition(LatLng(location.latitude, location.longitude))
    }

    fun locationChoosed() {

    }
}