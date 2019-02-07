package com.example.ty395.google_map.Start;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.ty395.google_map.Main.StartActivity;
import com.example.ty395.google_map.R;

import org.w3c.dom.Text;

public class StartFragment extends android.support.v4.app.Fragment {
    public StartFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup constraintLayout= (ConstraintLayout) inflater.inflate(R.layout.fragment_start,container,false);
        LottieAnimationView lottieAnimationView=constraintLayout.findViewById(R.id.lottie_start);
        lottieAnimationView.setAnimation("star.json");
        lottieAnimationView.playAnimation();
        TextView textView=constraintLayout.findViewById(R.id.text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), StartActivity.class);
                startActivity(intent);
            }
        });
        return constraintLayout;
    }
}
