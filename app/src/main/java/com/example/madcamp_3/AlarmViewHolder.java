package com.example.madcamp_3;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class AlarmViewHolder extends RecyclerView.ViewHolder  {
    public TextView ivNum, ivMessage;

    public AlarmViewHolder(View itemView){
        super(itemView);
        ivNum=itemView.findViewById(R.id.vehnume);
        ivMessage=itemView.findViewById(R.id.message);
    }
}
