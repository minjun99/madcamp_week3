package com.example.madcamp_3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddVehicle extends AppCompatActivity {
    TextView registernum;
    ImageView imageView;
    Button addBtn;
    TextView vehiclename;
    private static final int CAMERA_REQUEST_CODE = 5;
    Bitmap bitmap;
    View view;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        ImageButton button = findViewById(R.id.camera2);
        imageView = findViewById(R.id.cameraimage2);
        registernum=findViewById(R.id.registernum2);
        vehiclename=findViewById(R.id.vehiclename);
        addBtn=findViewById(R.id.search2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=vehiclename.getText().toString();
                String number= registernum.getText().toString();

                if(name.equals("")||name.length()==0
                        ||number.equals("")||number.length()==0){
                    Toast.makeText(mContext,"All fields are required.",Toast.LENGTH_SHORT).show();
                }
                else{
                    String[] data ={name,number};
                    post("http://143.248.36.141:3000/contactlist",data);
                    //post("http://143.248.36.30:3000/contactlist",data);

                    //((MainActivity)MainActivity.mContext).showProperFragment();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==CAMERA_REQUEST_CODE){
            bitmap= (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            System.out.println("taking photo");
            FirebaseApp.initializeApp(this);
            detect(view);
        }
    }

    public void detect(View v){
        if(bitmap==null){
            Toast.makeText(this.getApplicationContext(),"bitmap null",Toast.LENGTH_LONG).show();
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
            Toast.makeText(this.getApplicationContext(),"no text",Toast.LENGTH_SHORT).show();
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
            RequestBody formBody = new FormBody.Builder().add("name", Fragment1.profilename)
                    .add("vehicle", data[0])
                    .add("number", data[1])
                    .build();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(requestURL)
                    .post(formBody)
                    .build();
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
                    String vehiclebody = response.body().string();
                    System.out.println("Response Body is " + vehiclebody);
                    countDownLatch.countDown();
                }
            });
            countDownLatch.await();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
