package com.example.ty395.google_map.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ty395.google_map.Info.InfoData;
import com.example.ty395.google_map.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    ArrayList<InfoData> infoData;
    StorageReference imagesRef;
    Context context;
    int item_list;
    FirebaseStorage fs = FirebaseStorage.getInstance();
    public ListAdapter(Context context, int item_list, ArrayList<InfoData> infoData) {
        this.infoData = infoData;
        this.item_list=item_list;
        this.context=context;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list,viewGroup,false);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder viewHolder, int i) {
        InfoData infoData1=infoData.get(i);
        viewHolder.text_name.setText(infoData1.getName());
        viewHolder.text_address.setText(infoData1.getAddress());
        switch (infoData1.getType()){
            case "레스토랑":
                imagesRef = fs.getReference().child("image/restaurant.png");
                Glide.with(context)
                        .load(imagesRef)
                        .into(viewHolder.ic_icon);
                break;
            case "병원":
                imagesRef = fs.getReference().child("image/hospital.png");
                Glide.with(context)
                        .load(imagesRef)
                        .into(viewHolder.ic_icon);
                break;
            case "지하철역":
                imagesRef = fs.getReference().child("image/subway.png");
                Glide.with(context)
                        .load(imagesRef)
                        .into(viewHolder.ic_icon);
                break;
            case "카페":
                imagesRef = fs.getReference().child("image/cafe.png");
                Glide.with(context)
                        .load(imagesRef)
                        .into(viewHolder.ic_icon);
                break;
        }
        viewHolder.ic_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("7c176070fbbeb833").removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return infoData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;
        TextView text_address;
        ImageView ic_icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_address=itemView.findViewById(R.id.text_address);
            text_name=itemView.findViewById(R.id.text_name);
            ic_icon=itemView.findViewById(R.id.ic_icon);
        }
    }
}
