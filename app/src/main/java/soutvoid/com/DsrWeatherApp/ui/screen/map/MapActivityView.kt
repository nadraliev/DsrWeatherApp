package soutvoid.com.DsrWeatherApp.ui.screen.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.agna.ferro.mvp.component.ScreenComponent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*
import soutvoid.com.DsrWeatherApp.R
import soutvoid.com.DsrWeatherApp.ui.base.activity.BaseActivityView
import soutvoid.com.DsrWeatherApp.ui.base.activity.BasePresenter
import soutvoid.com.DsrWeatherApp.ui.util.LocationListener
import soutvoid.com.DsrWeatherApp.util.SdkUtil
import javax.inject.Inject


/**
 * активити для отображения карты и выбора точки
 */
class MapActivityView: BaseActivityView() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MapActivityView::class.java))
        }
    }

    @Inject
    lateinit var presenter: MapActivityPresenter

    private lateinit var map: GoogleMap

    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null
    var marker: Marker? = null
    var locatonRequested = false

    override fun getPresenter(): BasePresenter<*> = presenter

    override fun getName(): String = "Map"

    override fun createScreenComponent(): ScreenComponent<*> {
        return DaggerMapActivityComponent.builder()
                .activityModule(activityModule)
                .appComponent(appComponent)
                .build()
    }

    override fun getContentView(): Int = R.layout.activity_map

    override fun onCreate(savedInstanceState: Bundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, viewRecreated)

        initToolbar()
        initButtons()

        //начиная с android M запросить у пользователя разрешение на геолокацию
        if (SdkUtil.supportsM() && !isLocationPermissionGranted())
            requestLocationPermission()
        else locationPermissionGranted(true)

        initMap()
    }

    override fun onPause() {
        super.onPause()
        removeSubscriptionToLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        subscribeToLocationUpdates()
    }

    private fun initToolbar() {
        setSupportActionBar(map_toolbar)
        title = getString(R.string.choose_location)
        map_toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_light)
        map_toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * инициализация fab кнопок
     */
    private fun initButtons() {
        map_my_location.setOnClickListener { requestLocation() }
        map_add_location.setOnClickListener { marker?.let { presenter.locationChosen(it.position) } }
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync {
            map = it
            map.setOnMapClickListener { setMarkerPosition(it) }
        }
    }

    private fun isLocationPermissionGranted() : Boolean {
        val checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        return checkPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        locationPermissionGranted(
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        )
    }

    private fun locationPermissionGranted(granted: Boolean) {
        if (granted)
            locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        presenter.locationPermissionGranted(granted)
    }

    private fun subscribeToLocationUpdates() {
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
    }

    private fun removeSubscriptionToLocationUpdates() {
        locationManager?.removeUpdates(locationListener)
    }

    fun requestLocation() {
        if (!locatonRequested) {
            locatonRequested = true
            locationListener = LocationListener {
                presenter.locationChanged(it)
                removeSubscriptionToLocationUpdates()
                locatonRequested = false
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