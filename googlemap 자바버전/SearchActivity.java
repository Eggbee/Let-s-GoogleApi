package com.example.ty395.google_map;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.io.IOException;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    EditText edit_place;
    Geocoder geocoder;
    List<Address> list = null;
    ImageView ic_search;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        edit_place = findViewById(R.id.edit_place);
        ic_search = findViewById(R.id.ic_search);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        geocoder = new Geocoder(this);
        ic_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String place = edit_place.getText().toString();
                try {
                    list = geocoder.getFromLocationName(place, 10);
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
                        LatLng search_place = new LatLng(latitude, longitude);

                        try {
                            list = geocoder.getFromLocation(latitude, longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (list != null) {
                            if (list.size() == 0) {
                                Log.d("debug", "해당하지 않는 위치");
                            } else {
                                String place_name = list.get(0).getAddressLine(0);
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(search_place, 16));
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions
                                        .position(search_place)
                                        .title(place_name)
                                        .snippet("위도 : " + latitude + " 경도 : " + longitude);
                                map.addMarker(markerOptions);
                            }
                        }
                    }
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
