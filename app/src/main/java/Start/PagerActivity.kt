package com.example.ty395.google_map.Start

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity

import com.example.ty395.google_map.R

class PagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)
        val viewPager = findViewById<ViewPager>(R.id.viewpager)
        viewPager.adapter = PagerAdapter(supportFragmentManager)
        viewPager.currentItem = 0
    }
}
