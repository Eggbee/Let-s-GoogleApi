package com.example.ty395.google_map.Search

import android.location.Address
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.ty395.google_map.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import java.io.IOException

class SearchActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var map: GoogleMap
    lateinit var edit_place: EditText
    lateinit var geocoder: Geocoder
    var list: List<Address>? = null
    lateinit var ic_search: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        edit_place = findViewById(R.id.edit_place)
        ic_search = findViewById(R.id.ic_search)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        geocoder = Geocoder(this)
        ic_search.setOnClickListener {
            val place = edit_place.text.toString()
            try {
                list = geocoder.getFromLocationName(place, 10)
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
                    val search_place = LatLng(latitude, longitude)

                    try {
                        list = geocoder.getFromLocation(latitude, longitude, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    if (list != null) {
                        if (list!!.size == 0) {
                            Log.d("debug", "해당하지 않는 위치")
                        } else {
                            val place_name = list!![0].getAddressLine(0)
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(search_place, 16f))
                            val markerOptions = MarkerOptions()
                            markerOptions
                                    .position(search_place)
                                    .title(place_name)
                                    .snippet("위도 : $latitude 경도 : $longitude")
                            map.addMarker(markerOptions)
                        }
                    }
                }
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
