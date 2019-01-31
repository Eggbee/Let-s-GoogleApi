package com.example.ty395.google_map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

import java.io.IOException

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var map: GoogleMap
    lateinit var geocoder: Geocoder
    lateinit var gps: TextView
    var list: List<Address>? = null
    lateinit var now_gps: ImageView
    lateinit var locationManager: LocationManager

    internal var locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val now_latitude = location.latitude
            val now_longtitude = location.longitude
            val now = LatLng(now_latitude, now_longtitude)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(now, 16f))
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

        }

        override fun onProviderEnabled(s: String) {

        }

        override fun onProviderDisabled(s: String) {

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        gps = findViewById(R.id.gps)
        now_gps = findViewById(R.id.now_gps)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //GPS 설정화면으로 이동
            Toast.makeText(this, "gps를 켜주세요", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            startActivity(intent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        geocoder = Geocoder(this)
        map.setOnCameraChangeListener { cameraPosition ->
            val center = cameraPosition.target
            val latitude = center.latitude
            val longtitude = center.longitude
            try {
                list = geocoder.getFromLocation(latitude, longtitude, 1)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (list != null) {
                if (list!!.size == 0) {
                    Log.d("debug", "해당하지 않는 위치")
                } else {
                    val address = list!![0].getAddressLine(0)
                    gps.text = address
                }
            }
        }
        now_gps.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            } else {
                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val location2 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location2 != null) {
                    val now_latitude = location2.latitude
                    val now_longtitude = location2.longitude
                    val now = LatLng(now_latitude, now_longtitude)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(now, 16f))
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, locationListener)
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1f, locationListener)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
