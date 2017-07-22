package soutvoid.com.DsrWeatherApp.ui.screen.newLocation.stepper.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agna.ferro.mvp.component.ScreenComponent
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.activity_map.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.base.fragment.BaseFragmentView
import soutvoid.com.DsrWeatherApp.ui.util.LocationListener
import soutvoid.com.DsrWeatherApp.ui.util.PlaceSelectionListener
import soutvoid.com.DsrWeatherApp.util.SdkUtil
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

    private lateinit var map: GoogleMap

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
        val rootView = inflater?.inflate(R.layout.activity_map, container, false)
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
        initAutocompleteFragment()
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
        map_add_location.setOnClickListener { marker?.let { presenter.locationChosen(it.position) } }
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync {
            map = it
            map.setOnMapClickListener { setMarkerPosition(it) }
        }
    }

    private fun initAutocompleteFragment() {
        val autoCompleteFragment = childFragmentManager.findFragmentById(R.id.map_autocomplete_fragment) as SupportPlaceAutocompleteFragment
        autoCompleteFragment.setOnPlaceSelectedListener(PlaceSelectionListener { presenter.locationSelectedInSearchField(it) })
    }

    private fun isLocationPermissionGranted() : Boolean {
        val checkPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        return checkPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
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
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
    }

    private fun removeSubscriptionToLocationUpdates() {
        locationManager?.removeUpdates(locationListener)
    }

    override fun onSelected() {

    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onError(error: VerificationError) {

    }

    fun requestLocation() {
        if (!locationRequested) {
            locationRequested = true
            locationListener = LocationListener {
                presenter.locationChanged(it)
                removeSubscriptionToLocationUpdates()
                locationRequested = false
            }
            subscribeToLocationUpdates()
        }
    }

    fun setMapPosition(latLng: LatLng, zoom: Float = 10f) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    fun setMarkerPosition(latLng: LatLng) {
        marker?.remove()
        marker = map.addMarker(MarkerOptions().position(latLng))
    }

}