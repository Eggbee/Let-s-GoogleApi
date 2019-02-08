package com.example.ty395.google_map.Start

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.airbnb.lottie.LottieAnimationView
import com.example.ty395.google_map.Main.StartActivity
import com.example.ty395.google_map.R

import org.w3c.dom.Text

class StartFragment : android.support.v4.app.Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val constraintLayout = inflater.inflate(R.layout.fragment_start, container, false) as ConstraintLayout
        val lottieAnimationView = constraintLayout.findViewById<LottieAnimationView>(R.id.lottie_start)
        lottieAnimationView.setAnimation("star.json")
        lottieAnimationView.playAnimation()
        val textView = constraintLayout.findViewById<TextView>(R.id.text)
        textView.setOnClickListener {
            val intent = Intent(context, StartActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }
        return constraintLayout
    }
}
