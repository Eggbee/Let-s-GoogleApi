package com.example.ty395.google_map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class InfoActivity extends AppCompatActivity {
    ImageView ic_place;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ic_place=findViewById(R.id.ic_place);
        TextView text_address=findViewById(R.id.text_address);
        TextView text_name=findViewById(R.id.text_name);
        Intent intent=getIntent();
        String type=intent.getStringExtra("type");
        String name=intent.getStringExtra("name");
        String address=intent.getStringExtra("address");
        text_address.setText(address);
        text_name.setText(name);
        switch (type){
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
    }
    public void glideModule(String type){
        FirebaseStorage fs = FirebaseStorage.getInstance();
        StorageReference imagesRef = fs.getReference().child("image/"+type+".png");
        Glide.with(this)
                .load(imagesRef)
                .into(ic_place);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
