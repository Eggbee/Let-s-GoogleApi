package com.example.ty395.google_map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.util.LocaleData;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    EditText edit_start;
    EditText edit_end;
    Button bt_start;
    Button bt_end;
    Button bt_line;
    LocationManager locationManager;
    GoogleMap map_route;
    Geocoder geocoder;
    LatLng latLng_start;
    LatLng latLng_end;
    List<Address> list = null;
    Marker startMarker = null;
    Marker endMarker = null;
    Polyline polyline = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        edit_start = findViewById(R.id.edit_start);
        edit_end = findViewById(R.id.edit_end);
        bt_line = findViewById(R.id.bt_line);
        bt_start = findViewById(R.id.bt_start);
        bt_end = findViewById(R.id.bt_end);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        SupportMapFragment map_route_fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_route);
        map_route_fragment.getMapAsync(this);


        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //GPS 설정화면으로 이동
            Toast.makeText(this, "gps를 켜주세요", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map_route = googleMap;
        geocoder = new Geocoder(this);
        bt_start.setOnClickListener(this);
        bt_end.setOnClickListener(this);
        bt_line.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_start:
                String start_location = edit_start.getText().toString();
                try {
                    list = geocoder.getFromLocationName(start_location, 10);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (list != null) {
                    if (list.size() == 0) {
                        Toast.makeText(getApplicationContext(), "해당하는 주소지는 없습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        Address address = list.get(0);
                        double latitude = address.getLatitude();
                        double longitude = address.getLongitude();
                        latLng_start = new LatLng(latitude, longitude);

                        if (startMarker != null) {
                            startMarker.remove();
                            startMarker = null;
                        }
                        if (startMarker == null) {
                            startMarker = map_route.addMarker(new MarkerOptions().position(latLng_start).title(start_location));
                            map_route.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_start, 16));
                        }

                    }
                }
                break;

            case R.id.bt_end:
                String end_location = edit_end.getText().toString();
                try {
                    list = geocoder.getFromLocationName(end_location, 10);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (list != null) {
                    if (list.size() == 0) {
                        Toast.makeText(getApplicationContext(), "해당하는 주소지는 없습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        Address end_address = list.get(0);
                        double end_latitude = end_address.getLatitude();
                        double end_longtitude = end_address.getLongitude();
                        latLng_end = new LatLng(end_latitude, end_longtitude);
                        if (endMarker != null) {
                            endMarker.remove();
                            endMarker = null;
                        }
                        if (endMarker == null) {
                            endMarker = map_route.addMarker(new MarkerOptions().position(latLng_end).title(end_location));
                            map_route.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_end, 16));
                        }
                    }
                }
                break;

            case R.id.bt_line:
                if (polyline != null) {
                    polyline.remove();
                    polyline = null;
                }
                if (polyline == null) {
                    polyline = map_route.addPolyline(new PolylineOptions().add(latLng_start).add(latLng_end).width(5).color(R.color.colorMain));
                }

        }
    }
}
