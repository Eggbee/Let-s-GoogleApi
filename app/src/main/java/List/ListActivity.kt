package com.example.ty395.google_map.List

import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.ty395.google_map.Info.InfoData
import com.example.ty395.google_map.R

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

import java.util.ArrayList

class ListActivity : AppCompatActivity() {
    internal var firebaseDatabase = FirebaseDatabase.getInstance()
    internal var databaseReference = firebaseDatabase.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val infoData1 = ArrayList<InfoData>()
        val adapter = ListAdapter(this@ListActivity, R.layout.item_list, infoData1)
        val key= Settings.Secure.getString(this@ListActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        databaseReference.child(key).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val infoData = dataSnapshot.getValue(InfoData::class.java)
                if (infoData != null) {
                    infoData1.add(infoData)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
