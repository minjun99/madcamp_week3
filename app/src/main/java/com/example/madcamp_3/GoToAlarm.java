package com.example.madcamp_3;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.madcamp_3.Fragment1.entirecollection;
import static com.example.madcamp_3.Fragment1.vehiclecollection;


public class GoToAlarm extends AppCompatActivity {

    String parsestring2;
    String parsestring;
    JSONObject jobj;
    ArrayList<Alarm> jarray= new ArrayList<>();
    ArrayList<String> sarray= new ArrayList<>();
    Context context ;
     RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_alarm);

        get("http://143.248.36.141:3000/alarm");

        recyclerView= findViewById(R.id.alarmlist);
        // context=getApplicationContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        get("http://143.248.36.30:3000/alarm");

    }

    public void get(String requestURL) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(requestURL)
                    .build();

            //비동기 처리 (enqueue 사용)
            client.newCall(request).enqueue(new Callback() {
                //비동기 처리를 위해 Callback 구현
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("error + Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    parsestring = response.body().string();
                    List<Vehicle> loaddata= vehiclecollection;
                    System.out.println("Response Body is " + parsestring);
                    try{
                        JSONArray jsonArray= new JSONArray(parsestring);
                        for(int i=0;i<jsonArray.length();i++){
                            try {
                                String vehiclenum = jsonArray.getJSONObject(i).getString("number");
                                String vehmessage = jsonArray.getJSONObject(i).getString("message");
                                Alarm alarm= new Alarm(vehiclenum,vehmessage);
                                for(int j=0;j<loaddata.size();j++){
                                    if(loaddata.get(j).number.equals(vehiclenum)){
                                        System.out.println(vehiclenum);
                                        jarray.add(alarm);
                                        //jarray.add(jsonArray.getJSONObject(i));
                                    }

                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }

                                AlarmAdapter alarmAdapter= new AlarmAdapter(context,jarray);
                                recyclerView.setAdapter(alarmAdapter);

                    }catch(Exception e){
                        e.printStackTrace();
                    }


                }
            });

        } catch (Exception e){
            System.err.println(e.toString());
        }
    }
}
