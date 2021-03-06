package com.example.ty395.google_map.Main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.ty395.google_map.*
import com.example.ty395.google_map.List.ListActivity
import com.example.ty395.google_map.NowLocation.MainActivity
import com.example.ty395.google_map.Place.PlaceActivity
import com.example.ty395.google_map.Route.RouteActivity
import com.example.ty395.google_map.Search.SearchActivity

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val ic_search = findViewById<ImageView>(R.id.ic_search)
        val ic_gps = findViewById<ImageView>(R.id.ic_gps)
        val ic_route = findViewById<ImageView>(R.id.ic_route)
        val ic_place=findViewById<ImageView>(R.id.ic_place);
        val ic_list=findViewById<ImageView>(R.id.ic_list);
        ic_gps.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        ic_search.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        ic_route.setOnClickListener {
            val intent = Intent(this, RouteActivity::class.java)
            startActivity(intent)
        }
        ic_place.setOnClickListener {
            val intent_start = Intent (this, PlaceActivity::class.java)
        startActivity(intent_start)}
        ic_list.setOnClickListener { val intent = Intent(this, ListActivity::class.java)
            startActivity(intent) }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish();
    }

}
