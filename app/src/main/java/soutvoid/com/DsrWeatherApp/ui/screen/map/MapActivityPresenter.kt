package soutvoid.com.DsrWeatherApp.ui.screen.map

import android.location.Geocoder
import android.location.Location
import com.agna.ferro.mvp.component.scope.PerScreen
import com.google.android.gms.maps.model.LatLng
import io.realm.Realm
import soutvoid.com.DsrWeatherApp.domain.location.SavedLocation
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import java.util.*
import javax.inject.Inject

@PerScreen
class MapActivityPresenter @Inject constructor(errorHandler: ErrorHandler):
    BasePresenter<MapActivityView>(errorHandler) {

    var locationChanged = false //если пользователь не поменял сам точку, то поставим на нее маркер

    fun locationPermissionGranted(granted: Boolean) {
        if (granted)
            view.requestLocation()
        else setPositionMoscow()
    }

    fun locationChanged(location: Location) {
        setPositionCurrent(location)
    }

    private fun setPositionMoscow() {
        val latlng = LatLng(55.7512, 37.6184)
        view.setMapPosition(latlng)
        maybeSetMarker(latlng)
    }

    private fun setPositionCurrent(location: Location) {
        val latlng = LatLng(location.latitude, location.longitude)
        view.setMapPosition(latlng)
        maybeSetMarker(latlng)
    }

    private fun maybeSetMarker(latLng: LatLng) {
        if (!locationChanged) {
            view.setMarkerPosition(latLng)
            locationChanged = true
        }
    }

    fun locationChosen(latLng: LatLng) {
        val locationName = getLocationName(latLng)
        val realm = Realm.getDefaultInstance()
        val location = SavedLocation(locationName, latLng.longitude, latLng.latitude)
        realm.executeTransaction { it.copyToRealm(location) }
        realm.close()
        view.finish()
    }

    private fun getLocationName(latLng: LatLng, locale: Locale = Locale.getDefault()) : String {
        val geocoder: Geocoder = Geocoder(view.baseContext, locale)
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses.isNotEmpty())
            return addresses[0].locality
        return ""
    }
}