package com.example.ty395.google_map.Info;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ty395.google_map.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class InfoActivity extends AppCompatActivity {
    ImageView ic_place;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    String name;
    String address;
    String type;
    int num=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ic_place = findViewById(R.id.ic_place);
        TextView text_address = findViewById(R.id.text_address);
        TextView text_name = findViewById(R.id.text_name);
        Button bt_save = findViewById(R.id.bt_save);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        text_address.setText(address);
        text_name.setText(name);
        final String key = Settings.Secure.getString(InfoActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        switch (type) {
            case "레스토랑":
                glideModule("restaurant");
                break;
            case "지하철역":
                glideModule("subway");
                break;
            case "병원":
                glideModule("hospital");
                break;
            case "카페":
                glideModule("cafe");
                break;
        }
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num==0){
                    Toast.makeText(getApplication(), "눌림", Toast.LENGTH_SHORT).show();
                    InfoData infoData = new InfoData();
                    infoData.setAddress(address);
                    infoData.setName(name);
                    infoData.setType(type);
                    databaseReference.child(key).push().setValue(infoData);
                    num=1;
                }
                else{
                    Toast.makeText(getApplicationContext(),"이미 저장된 장소입니다",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void glideModule(String type) {
        FirebaseStorage fs = FirebaseStorage.getInstance();
        StorageReference imagesRef = fs.getReference().child("image/" + type + ".png");
        Glide.with(this)
                .load(imagesRef)
                .into(ic_place);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        num=0;
        finish();
    }
}
