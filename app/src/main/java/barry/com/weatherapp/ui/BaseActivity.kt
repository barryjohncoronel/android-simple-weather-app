package barry.com.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.app_toolbar.*


abstract class BaseActivity : AppCompatActivity(), BaseView {
    protected val TAG = javaClass.simpleName

    companion object {
        private const val UPDATE_INTERVAL = (2 * 1000).toLong()
        private const val FASTEST_INTERVAL = 2000.toLong()
        const val PERMISSIONS_REQUEST_LOCATION = 100
        const val ENABLE_LOCATION_REQUEST = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    abstract fun setupViews()

    fun setupToolbarTextTitleWithBack(title: String) {
        textTitle.text = title

        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) { // back button in top left corner of screen
            onBackPressed()
        }

        return true
    }

    protected fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSIONS_REQUEST_LOCATION
        )
    }

    protected fun shouldShowRequestPermissionRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            requestLocationPermission()
            Log.d(TAG, "1 permission denied")
        } else {
            // No explanation needed, we can request the permission.
            requestLocationPermission()
            Log.d(TAG, "2 permission denied")
        }
    }

    @SuppressLint("MissingPermission")
    protected fun startLocationUpdates() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = UPDATE_INTERVAL
        mLocationRequest.fastestInterval = FASTEST_INTERVAL

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(
            mLocationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            onLocationChanged(p0.lastLocation)
        }
    }

    protected open fun onLocationChanged(location: Location) {
        Log.d(TAG, "onLocationChanged()_Updated Location: ${location.latitude}, ${location.longitude}")
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback)
    }

    /*@SuppressLint("MissingPermission")
    protected fun getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        val locationClient = getFusedLocationProviderClient(this)

        locationClient.lastLocation
            .addOnSuccessListener { location ->
                // GPS location can be null if GPS is switched off
                if (location != null) {
                    onLocationChanged(location)
                }
            }
            .addOnFailureListener { e ->
                Log.d("MapDemoActivity", "Error trying to get last GPS location")
                startLocationUpdates()
                e.printStackTrace()
            }
    }*/


    protected fun isGpsEnabled(): Boolean {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    protected fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                dialog.cancel()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, ENABLE_LOCATION_REQUEST)
            }
            .setNegativeButton("No") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }
}