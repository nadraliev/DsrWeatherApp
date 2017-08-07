package soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agna.ferro.mvp.component.ScreenComponent
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_map.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.base.fragment.BaseFragmentView
import soutvoid.com.DsrWeatherApp.ui.util.LocationListener
import soutvoid.com.DsrWeatherApp.util.SdkUtil
import java.lang.IllegalArgumentException
import javax.inject.Inject


/**
 * активити для отображения карты и выбора точки
 */
class MapFragmentView : BaseFragmentView(), Step {

    companion object {
        fun newInstance(): MapFragmentView {
            return MapFragmentView()
        }
    }

    @Inject
    lateinit var presenter: MapFragmentPresenter

    private var map: GoogleMap? = null

    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null
    var marker: Marker? = null
    var locationRequested = false
    var myLocationButtonClicked = false

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun getName(): String = "Map"

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerMapFragmentComponent.builder()
                .appComponent(getAppComponent())
                .fragmentModule(getFragmentModule())
                .build()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_map, container, false)
        return rootView
    }

    override fun onStart() {
        super.onStart()

        initButtons()

        //начиная с android M запросить у пользователя разрешение на геолокацию
        if (SdkUtil.supportsM() && !isLocationPermissionGranted())
            requestLocationPermission()
        else locationPermissionGranted(true)

        initMap()
        initAutocompleteSearchField()
    }

    override fun onPause() {
        super.onPause()
        removeSubscriptionToLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        subscribeToLocationUpdates()
    }

    /**
     * инициализация fab кнопок
     */
    private fun initButtons() {
        map_my_location.setOnClickListener {
            requestLocation()
            myLocationButtonClicked = true
        }
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync {
            map = it
            map?.setOnMapClickListener { setMarkerPosition(it) }
        }
    }

    private fun initAutocompleteSearchField() {
        val view = LayoutInflater.from(activity)
                .inflate(R.layout.search_field_layout, map_autocomplete_background, true)
        view.setOnClickListener {
            try {
                val intent =
                        PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .build(activity)
                startActivityForResult(intent, 0)
                map_autocomplete_background.visibility = View.INVISIBLE
            } catch (e: GooglePlayServicesRepairableException) { }
            catch (e: GooglePlayServicesNotAvailableException) { }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0) {
            map_autocomplete_background.visibility = View.VISIBLE
            if (resultCode == Activity.RESULT_OK) {
                presenter.locationSelectedInSearchField(PlaceAutocomplete.getPlace(activity, data))
            }
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        val checkPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        return checkPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        locationPermissionGranted(
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        )
    }

    private fun locationPermissionGranted(granted: Boolean) {
        if (granted)
            locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        presenter.locationPermissionGranted(granted)
    }

    private fun subscribeToLocationUpdates() {
        try {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        } catch (e: IllegalArgumentException) {
            //ignored
        }
        try {
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
        } catch (e: IllegalArgumentException) {
            //ignored
        }
    }

    private fun removeSubscriptionToLocationUpdates() {
        map_location_progress_bar.visibility = View.INVISIBLE
        locationManager?.removeUpdates(locationListener)
    }

    override fun onSelected() {

    }

    override fun verifyStep(): VerificationError? {
        marker?.let { presenter.locationChosen(it.position) }
        if (marker == null)
            presenter.locationChosen(LatLng(55.45, 37.36))
        return null
    }

    override fun onError(error: VerificationError) {

    }

    fun requestLocation() {
        if (!locationRequested) {
            map_location_progress_bar.visibility = View.VISIBLE
            locationRequested = true
            locationListener = LocationListener {
                map_location_progress_bar.visibility = View.INVISIBLE
                presenter.locationChanged(it)
                removeSubscriptionToLocationUpdates()
                locationRequested = false
            }
            subscribeToLocationUpdates()
        }
    }

    fun setMapPosition(latLng: LatLng, zoom: Float = 10f) {
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    fun setMarkerPosition(latLng: LatLng) {
        marker?.remove()
        marker = map?.addMarker(MarkerOptions().position(latLng))
    }
}