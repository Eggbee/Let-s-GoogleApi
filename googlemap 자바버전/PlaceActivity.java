package com.example.ty395.google_map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class PlaceActivity extends AppCompatActivity implements OnMapReadyCallback, PlacesListener,View.OnClickListener {

    List<Marker> previous_marker = null;
    LocationManager locationManager;
    GoogleMap map_place;
    Geocoder geocoder;
    List<Address> list = null;
    String address;
    LatLng latLng_place;
    ImageView ic_search;
    EditText edit_place;
    Button bt_food;
    Button bt_subway;
    Button bt_hospital;
    Button bt_cafe;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_place);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_place);
        mapFragment.getMapAsync(this);

        ic_search = findViewById(R.id.ic_search);
        edit_place = findViewById(R.id.edit_place);
        bt_food=findViewById(R.id.button);
        bt_hospital=findViewById(R.id.button2);
        bt_subway=findViewById(R.id.button3);
        bt_cafe=findViewById(R.id.button4);
        bt_cafe.setOnClickListener(this);
        bt_subway.setOnClickListener(this);
        bt_hospital.setOnClickListener(this);
        bt_food.setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        map_place = googleMap;
        geocoder = new Geocoder(this);
        ic_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        latLng_place = new LatLng(latitude, longitude);
                        map_place.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_place, 16));

                        MarkerOptions markerOptions=new MarkerOptions()
                                .position(latLng_place)
                                .title("현재위치");
                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.ic_person);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 200, 200, false);
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                        map_place.addMarker(markerOptions);
                    }
                }
            }
        });

    }


    // place api
    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(final List<Place> places) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MarkerOptions markerOptions_now=new MarkerOptions()
                        .position(latLng_place)
                        .title("현재위치");
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.ic_person);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 200, 200, false);
                markerOptions_now.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                map_place.addMarker(markerOptions_now);

                for (noman.googleplaces.Place place : places) {

                    LatLng latLng
                            = new LatLng(place.getLatitude()
                            , place.getLongitude());
                    gpsToaddress(latLng);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());
                    markerOptions.snippet(address);
                    map_place.addMarker(markerOptions);
                }
            }
        });
    }

    @Override
    public void onPlacesFinished() {

    }

    public void showPlaceInformation(LatLng location, String placeType) {
        map_place.clear();
        if (previous_marker != null)
            previous_marker.clear();
        else{
            new NRPlaces.Builder()
                    .listener(PlaceActivity.this)
                    .key("AIzaSyCw-RJ26x9II-zMFql-TRBiclAix7aocnU")
                    .latlng(location.latitude, location.longitude)//현재 위치
                    .radius(500) //500 미터 내에서 검색
                    .type(placeType)
                    .build()
                    .execute();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public String gpsToaddress(LatLng location){
        geocoder=new Geocoder(this);
        try {
            list = geocoder.getFromLocation(location.latitude, location.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list != null) {
            if (list.size() == 0) {
                Log.d("debug", "해당하지 않는 위치");
            } else {
                address = list.get(0).getAddressLine(0);
            }
        }
        return address;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                if (edit_place.getText().length()<=0){Toast.makeText(getApplicationContext(),"장소를 입력해주세요",Toast.LENGTH_SHORT).show();}
                else{showPlaceInformation(latLng_place,PlaceType.RESTAURANT);}
                break;
            case R.id.button2:
                if (edit_place.getText().length()<=0){Toast.makeText(getApplicationContext(),"장소를 입력해주세요",Toast.LENGTH_SHORT).show();}
                else{showPlaceInformation(latLng_place,PlaceType.HOSPITAL);}
                break;
            case R.id.button3:
                if (edit_place.getText().length()<=0){Toast.makeText(getApplicationContext(),"장소를 입력해주세요",Toast.LENGTH_SHORT).show();}
                else{showPlaceInformation(latLng_place,PlaceType.SUBWAY_STATION);}
                break;
            case R.id.button4:
                if (edit_place.getText().length()<=0){Toast.makeText(getApplicationContext(),"장소를 입력해주세요",Toast.LENGTH_SHORT).show();}
                else{showPlaceInformation(latLng_place,PlaceType.CAFE);}
                break;
        }
    }
}
