package com.example.sky

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class SkyMainActivity : AppCompatActivity() {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var wayLatitude = 0.0
    private var wayLongitude = 0.0
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var isContinue = true
    private var isGPS = false
    val isGpsString = "isGPS"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sky_main)
//        if(savedInstanceState!=null){
//            isGPS = savedInstanceState.getBoolean(isGpsString)
//        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(60 * 60 * 1000.toLong()).setFastestInterval(30 * 60 * 1000.toLong())
        GpsUtils(this).turnGPSOn(object : GpsUtils.OnGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) {
                // turn on GPS
                isGPS = isGPSEnable
            }
        })
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.e("hjsdghjsgd", "sdhgsdg")
                for (location in locationResult.locations) {
                    wayLatitude = location.latitude
                    wayLongitude = location.longitude
                    Log.i("SkyMainActivity", "$wayLatitude $wayLongitude")
                    val sharedPref = getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE
                    )
                    with(sharedPref.edit()) {
                        putFloat(
                            getString(R.string.longitude),
                            wayLongitude.toFloat()
                        )
                        commit()
                    }
                    with(sharedPref.edit()) {
                        putFloat(getString(R.string.latitude), wayLatitude.toFloat())
                        commit()
                    }
                    if (!isContinue) {
                        stopLocationUpdates()
                    }
                }
            }
        }
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)
        location()
    }

    private fun location() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                AppConstants.LOCATION_REQUEST
            )
        } else {
            if (isContinue) {
                startLocationUpdates()
            } else {
                mFusedLocationClient.lastLocation.addOnSuccessListener(
                    this
                ) { location: Location? ->

                    Log.e("hjsdghjsgd1", "sdhgsdg")
                    if (location != null) {
                        val sharedPref = getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE
                        )
                        with(sharedPref.edit()) {
                            putFloat(
                                getString(R.string.longitude),
                                wayLongitude.toFloat()
                            )
                            commit()
                        }
                        with(sharedPref.edit()) {
                            putFloat(getString(R.string.latitude), wayLatitude.toFloat())
                            commit()
                        }

                        Log.i("SkyMainActivity1", "$wayLatitude $wayLongitude")
                    } else {
                        startLocationUpdates()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    if (isContinue) {
                        startLocationUpdates()
                    } else {
                        lastLocationCaller()
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun lastLocationCaller() {
        mFusedLocationClient.lastLocation
            .addOnSuccessListener(
                this
            ) { location: Location? ->

                Log.e("startlastLocationCaller", "$location")
                if (location != null) {
                    wayLatitude = location.latitude
                    wayLongitude = location.longitude
                    val sharedPref = getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE
                    )
                    with(sharedPref.edit()) {
                        putFloat(
                            getString(R.string.longitude),
                            wayLongitude.toFloat()
                        )
                        commit()
                    }
                    with(sharedPref.edit()) {
                        putFloat(getString(R.string.latitude), wayLatitude.toFloat())
                        commit()
                    }

                    Log.i("SkyMainActivity2", "$wayLatitude $wayLongitude")
                } else {
                    startLocationUpdates()
                }
            }
    }

    private fun startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )

        Log.e("hjsdghjsgdstar", "sdhgsdg")
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.GPS_REQUEST) {
                isGPS = true // flag maintain before get location
                location()
                startLocationUpdates()
            }
        }
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        isContinue = true
        location()
        if (isGPS) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        isContinue = false
        stopLocationUpdates()
    }
}