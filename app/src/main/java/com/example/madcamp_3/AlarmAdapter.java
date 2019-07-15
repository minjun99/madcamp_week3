package com.example.madcamp_3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder> {
    private  Context mContext;
    View baseView;
    List<Alarm> alarm;

    public AlarmAdapter(Context context, List<Alarm> alarm){
        this.mContext=context;
        this.alarm=alarm;
    }


    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        baseView= View.inflate(mContext, R.layout.alarm_item,null);
        AlarmViewHolder alarmViewHolder= new AlarmViewHolder(baseView);
        return alarmViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AlarmViewHolder holder, final int position) {
        Alarm alarmitem= alarm.get(position);
        holder.ivNum.setText(alarmitem.getnumber());
        holder.ivMessage.setText(alarmitem.getMessage());

    }

    @Override
    public int getItemCount() {
        return alarm.size();
    }




}
