package com.example.ty395.google_map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.icu.util.LocaleData
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

import java.io.IOException

class RouteActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    lateinit var edit_start: EditText
    lateinit var edit_end: EditText
    lateinit var bt_start: Button
    lateinit var bt_end: Button
    lateinit var bt_line: Button
    lateinit var locationManager: LocationManager
    lateinit var map_route: GoogleMap
    lateinit var geocoder: Geocoder
    lateinit var latLng_start: LatLng
    lateinit var latLng_end: LatLng
    var list: List<Address>? = null
    var startMarker: Marker? = null
    var endMarker: Marker? = null
    var polyline: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        edit_start = findViewById(R.id.edit_start)
        edit_end = findViewById(R.id.edit_end)
        bt_line = findViewById(R.id.bt_line)
        bt_start = findViewById(R.id.bt_start)
        bt_end = findViewById(R.id.bt_end)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val map_route_fragment = supportFragmentManager.findFragmentById(R.id.map_route) as SupportMapFragment?
        map_route_fragment!!.getMapAsync(this)


        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //GPS 설정화면으로 이동
            Toast.makeText(this, "gps를 켜주세요", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            startActivity(intent)
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map_route = googleMap
        geocoder = Geocoder(this)
        bt_start.setOnClickListener(this)
        bt_end.setOnClickListener(this)
        bt_line.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.bt_start -> {
                val start_location = edit_start.text.toString()
                try {
                    list = geocoder.getFromLocationName(start_location, 10)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                if (list != null) {
                    if (list!!.size == 0) {
                        Toast.makeText(applicationContext, "해당하는 주소지는 없습니다", Toast.LENGTH_SHORT).show()
                    } else {
                        val address = list!![0]
                        val latitude = address.latitude
                        val longitude = address.longitude
                        latLng_start = LatLng(latitude, longitude)

                        if (startMarker != null) {
                            startMarker!!.remove()
                            startMarker = null
                        }
                        if (startMarker == null) {
                            startMarker = map_route.addMarker(MarkerOptions().position(latLng_start).title(start_location))
                            map_route.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_start, 16f))
                        }

                    }
                }
            }

            R.id.bt_end -> {
                val end_location = edit_end.text.toString()
                try {
                    list = geocoder.getFromLocationName(end_location, 10)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                if (list != null) {
                    if (list!!.size == 0) {
                        Toast.makeText(applicationContext, "해당하는 주소지는 없습니다", Toast.LENGTH_SHORT).show()
                    } else {
                        val end_address = list!![0]
                        val end_latitude = end_address.latitude
                        val end_longtitude = end_address.longitude
                        latLng_end = LatLng(end_latitude, end_longtitude)
                        if (endMarker != null) {
                            endMarker!!.remove()
                            endMarker = null
                        }
                        if (endMarker == null) {
                            endMarker = map_route.addMarker(MarkerOptions().position(latLng_end).title(end_location))
                            map_route.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_end, 16f))
                        }
                    }
                }
            }

            R.id.bt_line -> {
                if (polyline != null) {
                    polyline!!.remove()
                    polyline = null
                }
                if (polyline == null) {
                    polyline = map_route.addPolyline(PolylineOptions().add(latLng_start).add(latLng_end).width(5f).color(R.color.colorMain))
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
