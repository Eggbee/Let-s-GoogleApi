package com.example.ty395.google_map.Start

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(i: Int): Fragment? {
        when (i) {
            0 -> return MapFragment()
            1 -> return RouteFragment()
            2 -> return PlaceFragment()
            3 -> return StartFragment()
            else -> return null
        }
    }

    override fun getCount(): Int {
        return 4
    }
}
