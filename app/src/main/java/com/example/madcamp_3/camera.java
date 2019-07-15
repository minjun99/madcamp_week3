package com.example.madcamp_3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.madcamp_3.Fragment1.entirecollection;
import static com.example.madcamp_3.Fragment1.vehiclecollection;

public class camera extends Fragment {
    private static final String TAG="PhotoFragment";
    MainActivity mainActivity;
    private static final int CAMERA_REQUEST_CODE=5;
    ImageView imageView;
    Context context;
    TextView registernum;
    TextView registermessage;
    Bitmap bitmap;
    View view;
    Context mContext;
    String viewdata;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        mContext=getContext();
        view = inflater.inflate(R.layout.fragment3,container,false);
        Log.d(TAG,"onCreateView:started.");
        ImageButton searchBtn = view.findViewById(R.id.camera);
        imageView = view.findViewById(R.id.cameraimage);
        registernum=view.findViewById(R.id.registernum);
        registermessage=view.findViewById(R.id.registermessage);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onClick:launch camera");
                Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);
            }
        });

        Button button = view.findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count=0;
                String number= registernum.getText().toString();
                String message = registermessage.getText().toString();
                List<Vehicle> listdata =entirecollection;
                if(message.equals("")||message.length()==0
                        ||number.equals("")||number.length()==0){
                    Toast.makeText(mContext.getApplicationContext(),"All fields are required.",Toast.LENGTH_SHORT).show();
                }
                else{
                    for (int i=0; i<listdata.size();i++){
                        if(listdata.get(i).number.equals(number)){
                            String[] data ={number, message};
                            //post("http://143.248.36.30:3000/alarm",data);
                            post("http://143.248.36.141:3000/alarm",data);
                            Toast.makeText(mContext.getApplicationContext(),"Message is sent", Toast.LENGTH_SHORT).show();
                            //getActivity().finish();
                            //((MainActivity)MainActivity.mContext).showProperFragment();
                        }
                        else{
                            count++;
                        }
                    }
                    if(count==listdata.size()){
                        Toast.makeText(mContext.getApplicationContext(),"User with number "+number+" does not exist!",Toast.LENGTH_SHORT).show();
                    }
                }
                registernum.setText("");
                registermessage.setText("");
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==CAMERA_REQUEST_CODE){
            bitmap= (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            System.out.println("taking photo");
            FirebaseApp.initializeApp(getContext());
            detect(view);
        }
    }

    public void detect(View v){
        if(bitmap==null){
            Toast.makeText(getActivity().getApplicationContext(),"bitmap null",Toast.LENGTH_LONG).show();
        }
        final FirebaseVisionImage firebaseVisionImage= FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer= FirebaseVision.getInstance().getCloudTextRecognizer();
        System.out.println("detect enter");
        firebaseVisionTextRecognizer.processImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        System.out.println("process_text enter");
                        process_text(firebaseVisionText);
                        System.out.println("success");
                    }
                });
    }

    private void process_text(FirebaseVisionText firebaseVisionText){
        List<FirebaseVisionText.TextBlock> blocks = firebaseVisionText.getTextBlocks();
        if(blocks.size()==0){
            Toast.makeText(getActivity().getApplicationContext(),"no text",Toast.LENGTH_SHORT).show();
        }
        else{
            for(FirebaseVisionText.TextBlock block: firebaseVisionText.getTextBlocks()){
                String text= block.getText();
                registernum.setText(text);
            }
        }
    }

    private void post(String requestURL, String[] data) {
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("number", data[0])
                    .add("message", data[1])
                    .build();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(requestURL)
                    .post(formBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                //비동기 처리를 위해 Callback 구현
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("Sib");
                    System.out.println("error + Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String vehiclebody = response.body().string();
                    System.out.println("Response Body is " + vehiclebody);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
