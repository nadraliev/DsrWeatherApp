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

    var locationChanged = false //если пользователь еще не поменял сам точку, то поставим на нее маркер

    /**
     * установить камеру карты на Москву
     */
    private fun setPositionMoscow() {
        val latlng = LatLng(55.7512, 37.6184)
        view.setMapPosition(latlng)
        maybeSetMarker(latlng)
    }

    /**
     * утсановить камеру карты на текущее положение
     * @param [location] текущее положение
     */
    private fun setPositionCurrent(location: Location) {
        val latlng = LatLng(location.latitude, location.longitude)
        view.setMapPosition(latlng)
        maybeSetMarker(latlng)
    }

    /**
     * утсановить маркер на начальное положение, если пользователь сам не поменял положение маркера
     * @param [latLng] координаты точек
     */
    private fun maybeSetMarker(latLng: LatLng) {
        if (!locationChanged) {
            view.setMarkerPosition(latLng)
            locationChanged = true
        }
    }

    /**
     * получить название города по координатам
     */
    private fun getLocationName(latLng: LatLng, locale: Locale = Locale.getDefault()) : String {
        val geocoder: Geocoder = Geocoder(view.baseContext, locale)
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses.isNotEmpty() && addresses[0].locality != null)
            return addresses[0].locality
        return ""
    }

    /**
     * событие получения разрешения на отслеживание геолокации
     */
    fun locationPermissionGranted(granted: Boolean) {
        if (granted)
            view.requestLocation()
        else setPositionMoscow()
    }

    /**
     * событие получения новой точки из сервиса геолокации
     */
    fun locationChanged(location: Location) {
        setPositionCurrent(location)
    }

    /**
     * пользователь выбрал точку и нажал "подтвердить"
     */
    fun locationChosen(latLng: LatLng) {
        val locationName = getLocationName(latLng)
        val realm = Realm.getDefaultInstance()
        val location = SavedLocation(locationName, latLng.longitude, latLng.latitude)
        realm.executeTransaction { it.copyToRealm(location) }
        realm.close()
        view.finish()
    }
}