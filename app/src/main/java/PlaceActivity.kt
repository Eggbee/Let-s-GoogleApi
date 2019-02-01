package com.example.ty395.google_map

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import java.io.IOException
import java.util.HashSet

import noman.googleplaces.NRPlaces
import noman.googleplaces.Place
import noman.googleplaces.PlaceType
import noman.googleplaces.PlacesException
import noman.googleplaces.PlacesListener

class PlaceActivity : AppCompatActivity(), OnMapReadyCallback, PlacesListener, View.OnClickListener {

    var previous_marker: MutableList<Marker>? = null
    lateinit var locationManager: LocationManager
    lateinit var map_place: GoogleMap
    lateinit var geocoder: Geocoder
    var list: List<Address>? = null
    lateinit var address: String
    lateinit var latLng_place: LatLng
    lateinit var ic_search: ImageView
    lateinit var edit_place: EditText
    lateinit var bt_food: Button
    lateinit var bt_subway: Button
    lateinit var bt_hospital: Button
    lateinit var bt_cafe: Button
    lateinit var type: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_place)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_place) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        ic_search = findViewById(R.id.ic_search)
        edit_place = findViewById(R.id.edit_place)
        bt_food = findViewById(R.id.button)
        bt_hospital = findViewById(R.id.button2)
        bt_subway = findViewById(R.id.button3)
        bt_cafe = findViewById(R.id.button4)
        bt_cafe.setOnClickListener(this)
        bt_subway.setOnClickListener(this)
        bt_hospital.setOnClickListener(this)
        bt_food.setOnClickListener(this)

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
        map_place = googleMap
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
                    latLng_place = LatLng(latitude, longitude)
                    map_place.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_place, 16f))

                    val markerOptions = MarkerOptions()
                            .position(latLng_place)
                            .title("현재위치")
                    val bitmapdraw = resources.getDrawable(R.drawable.ic_person) as BitmapDrawable
                    val b = bitmapdraw.bitmap
                    val smallMarker = Bitmap.createScaledBitmap(b, 200, 200, false)
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

                    map_place.addMarker(markerOptions)
                }
            }
        }
        map_place.setOnInfoWindowClickListener { marker ->
            val name = marker.title
            val address = marker.snippet
            val intent = Intent(this@PlaceActivity, InfoActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("address", address)
            intent.putExtra("type", type)
            startActivity(intent)
        }

    }


    // place api
    override fun onPlacesFailure(e: PlacesException) {

    }

    override fun onPlacesStart() {

    }

    override fun onPlacesSuccess(places: List<Place>) {

        runOnUiThread {
            val markerOptions_now = MarkerOptions()
                    .position(latLng_place)
                    .title("현재위치")
            val bitmapdraw = resources.getDrawable(R.drawable.ic_person) as BitmapDrawable
            val b = bitmapdraw.bitmap
            val smallMarker = Bitmap.createScaledBitmap(b, 200, 200, false)
            markerOptions_now.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

            map_place.addMarker(markerOptions_now)

            for (place in places) {

                val latLng = LatLng(place.latitude, place.longitude)
                gpsToaddress(latLng)
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.title(place.name)
                markerOptions.snippet(address)
                val bitmapdraw = resources.getDrawable(R.drawable.ic_pin) as BitmapDrawable
                val b = bitmapdraw.bitmap
                val smallMarker = Bitmap.createScaledBitmap(b, 200, 200, false)
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                map_place.addMarker(markerOptions)
            }
        }
    }

    override fun onPlacesFinished() {

    }

    fun showPlaceInformation(location: LatLng, placeType: String) {
        map_place.clear()
        if (previous_marker != null)
            previous_marker!!.clear()
        else {
            NRPlaces.Builder()
                    .listener(this@PlaceActivity)
                    .key("AIzaSyCw-RJ26x9II-zMFql-TRBiclAix7aocnU")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(500) //500 미터 내에서 검색
                    .type(placeType)
                    .build()
                    .execute()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    fun gpsToaddress(location: LatLng): String {
        geocoder = Geocoder(this)
        try {
            list = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (list != null) {
            if (list!!.size == 0) {
                Log.d("debug", "해당하지 않는 위치")
            } else {
                address = list!![0].getAddressLine(0)
            }
        }
        return address
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button -> if (edit_place.text.length <= 0) {
                Toast.makeText(applicationContext, "장소를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                showPlaceInformation(latLng_place, PlaceType.RESTAURANT)
                type = "레스토랑"
            }
            R.id.button2 -> if (edit_place.text.length <= 0) {
                Toast.makeText(applicationContext, "장소를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                showPlaceInformation(latLng_place, PlaceType.HOSPITAL)
                type = "병원"
            }
            R.id.button3 -> if (edit_place.text.length <= 0) {
                Toast.makeText(applicationContext, "장소를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                showPlaceInformation(latLng_place, PlaceType.SUBWAY_STATION)
                type = "지하철역"
            }
            R.id.button4 -> if (edit_place.text.length <= 0) {
                Toast.makeText(applicationContext, "장소를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                showPlaceInformation(latLng_place, PlaceType.CAFE)
                type = "카페"
            }
        }
    }
}
