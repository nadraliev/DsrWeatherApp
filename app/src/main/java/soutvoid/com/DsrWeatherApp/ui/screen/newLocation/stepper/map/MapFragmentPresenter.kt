package soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.map

import android.location.Geocoder
import android.location.Location
import com.agna.ferro.mvp.component.scope.PerScreen
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.model.LatLng
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.common.error.ErrorHandler
import soutvoid.com.DsrWeatherApp.ui.common.error.StandardWithActionErrorHandler
import soutvoid.com.DsrWeatherApp.ui.common.message.MessagePresenter
import soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.settings.LocationSettingsFragmentView
import soutvoid.com.DsrWeatherApp.ui.util.getDefaultPreferences
import java.io.IOException
import java.util.*
import javax.inject.Inject

@PerScreen
class MapFragmentPresenter @Inject constructor(val messagePresenter: MessagePresenter,
                                               errorHandler: ErrorHandler) :
        BasePresenter<MapFragmentView>(errorHandler) {

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
    private fun getLocationName(latLng: LatLng, locale: Locale = Locale.getDefault()): String {
        val geocoder: Geocoder = Geocoder(view.context, locale)
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
        if (!locationChanged || view.myLocationButtonClicked) {
            setPositionCurrent(location)
        }
    }

    /**
     * пользователь выбрал точку и нажал "подтвердить"
     */
    fun locationChosen(latLng: LatLng) {
        var locationName = ""
        try {
            locationName = getLocationName(latLng)
        } catch (e: IOException) {
            val errorHandler = StandardWithActionErrorHandler(messagePresenter, view.getString(R.string.try_again))
            { locationChosen(latLng) }
            errorHandler.handleError(e)
        }
        view.context.getDefaultPreferences().edit()
                .putString(LocationSettingsFragmentView.NAME_KEY, locationName)
                .putFloat(LocationSettingsFragmentView.LATITUDE_KEY, latLng.latitude.toFloat())
                .putFloat(LocationSettingsFragmentView.LONGITUDE_KEY, latLng.longitude.toFloat())
                .commit()
    }

    /**
     * пользователь ввел место в поле ввода и выбрал его
     */
    fun locationSelectedInSearchField(place: Place) {
        locationChanged = true
        view.setMarkerPosition(place.latLng)
        view.setMapPosition(place.latLng)
    }
}