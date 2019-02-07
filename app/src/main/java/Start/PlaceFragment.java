package com.example.ty395.google_map.Start;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.example.ty395.google_map.R;

public class PlaceFragment extends android.support.v4.app.Fragment {
    public PlaceFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup constraintLayout= (ConstraintLayout) inflater.inflate(R.layout.fragment_place,container,false);
        LottieAnimationView lottieAnimationView=constraintLayout.findViewById(R.id.lottie_place);
        lottieAnimationView.setAnimation("place.json");
        lottieAnimationView.playAnimation();
        return constraintLayout;
    }
}
