package com.example.madcamp_3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleViewHolder> {
    private Context mContext;
    private List<Vehicle> vehicles;
    private String vehiclebody;

    public VehicleAdapter(Context mContext, List<Vehicle> vehicles){
        this.mContext=mContext;
        this.vehicles=vehicles;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View baseView= View.inflate(mContext,R.layout.vehicle_item,null);
        VehicleViewHolder vehicleViewHolder=new VehicleViewHolder(baseView);
        return vehicleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final VehicleViewHolder holder, final int position){
        Vehicle vehicle= vehicles.get(position);
        holder.ivnum.setText(vehicle.getNumber());
        holder.ivvehicle.setText(vehicle.getVehicle());
        holder.ivrow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setTitle("차량 삭제 안내")
                        .setMessage("차량을 삭제하겠습니까?")
                        .setIcon(android.R.drawable.ic_menu_save)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Vehicle removed= vehicles.remove(position);
                                Toast.makeText(mContext.getApplicationContext(),"기기 삭제 완료", Toast.LENGTH_SHORT).show();
                                //get("http://143.248.36.30:3000/contactlist",removed);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount(){
        return vehicles.size();
    }
    }
