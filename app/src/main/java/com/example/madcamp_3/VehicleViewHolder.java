package com.example.madcamp_3;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class VehicleViewHolder extends RecyclerView.ViewHolder {
    public TextView ivvehicle, ivnum;
    public RelativeLayout ivrow;

    public VehicleViewHolder(View itemView){
        super(itemView);
        ivvehicle=itemView.findViewById(R.id.vehiclename);
        ivnum=itemView.findViewById(R.id.vehiclenumber);

        ivrow=itemView.findViewById(R.id.vehicle);
    }
}
