package com.example.madcamp_3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Fragment1 extends Fragment {

    private static final String TAG = "Fragment";
    String str;
    URL url;
    Bitmap bitmap;
    ImageView profile_image;
    TextView name;
    TextView email;
    ImageButton button1;
    ImageButton button2;
    public static VehicleAdapter adapter;
    public static Context mContext;
    RecyclerView recyclerView;
    static String vehicles;
    public static String profilename;
    static List<Vehicle> entirecollection = new ArrayList<>();
    static  List<Vehicle> vehiclecollection= new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container,false);
        Context context = view.getContext();
        profile_image = (ImageView) view.findViewById(R.id.profile_image);
        name =  (TextView) view.findViewById(R.id.id_name);
        email = (TextView) view.findViewById(R.id.id_email);
        button1 = (ImageButton) view.findViewById(R.id.buttontoalarm);
        button2 = (ImageButton) view.findViewById(R.id.buttontoadd);
        recyclerView =(RecyclerView) view.findViewById(R.id.tab1recyclerview);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter= new VehicleAdapter(context,vehiclecollection);
        recyclerView.setAdapter(adapter);

        profile_image.setBackground(new ShapeDrawable(new OvalShape()));
        profile_image.setClipToOutline(true);

        final Bundle arguments = getArguments();
        Log.d(TAG, arguments.getString("url")+"44444444444");
        Picasso.get().load(arguments.getString("url")).into(profile_image);

        profilename=arguments.getString("name");
        name.setText( profilename);
        email.setText( arguments.getString("email"));
        get("http://143.248.36.141:3000/contactlist");
        //adapter.notifyDataSetChanged();

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GoToAlarm.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddVehicle.class);
                startActivity(intent);
            }
        });


        return view;
    }

    public static void get(String requestURL) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(requestURL)
                    .build();

            //비동기 처리 (enqueue 사용)
         //   synchronized (
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            client.newCall(request).enqueue(new Callback() {
                //비동기 처리를 위해 Callback 구현
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("Sibal");
                    System.out.println("error + Connect Server Error is " + e.toString());
                    countDownLatch.countDown();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    vehicles=response.body().string();
                    System.out.println("Response Body is " + vehicles);
                    try {
                        JSONArray Jarray = new JSONArray(vehicles);
                        List<Vehicle> vehiclelist = new ArrayList<>();
                        List<Vehicle> entire = new ArrayList<>();
                        if(Jarray!=null){
                            System.out.println(Jarray);
                            for(int i=0; i<Jarray.length();i++){
                                try{
                                    String vehiclename= Jarray.getJSONObject(i).getString("vehicle");
                                    System.out.println(vehiclename);
                                    String vehiclenum = Jarray.getJSONObject(i).getString("number");
                                    String vehicleid= Jarray.getJSONObject(i).getString("_id");
                                    String username=Jarray.getJSONObject(i).getString("name");
                                    System.out.println(vehiclenum);
                                    Vehicle vehicle=new Vehicle(vehicleid,vehiclename,vehiclenum);
                                    if(username.equals(profilename)) {
                                        vehiclelist.add(vehicle);
                                    }
                                    entire.add(vehicle);
                                    System.out.println(vehicle);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            entirecollection.clear();
                            entirecollection.addAll(entire);
                            vehiclecollection.clear();
                            vehiclecollection.addAll(vehiclelist);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    countDownLatch.countDown();}

            });
            countDownLatch.await();
            adapter.notifyDataSetChanged();
            System.out.println(adapter.getItemCount()+"count adapter");
        } catch (Exception e){
            System.err.println(e.toString());
        }

    }

}