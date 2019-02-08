package com.example.ty395.google_map.Start

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.airbnb.lottie.LottieAnimationView
import com.example.ty395.google_map.R

class PlaceFragment : android.support.v4.app.Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val constraintLayout = inflater.inflate(R.layout.fragment_place, container, false) as ConstraintLayout
        val lottieAnimationView = constraintLayout.findViewById<LottieAnimationView>(R.id.lottie_place)
        lottieAnimationView.setAnimation("place.json")
        lottieAnimationView.playAnimation()
        return constraintLayout
    }
}
