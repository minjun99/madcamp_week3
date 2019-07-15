package com.example.madcamp_3;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

//import com.login_signup_screendesign_demo.R;


public class Login_fragment extends Fragment {

    private static final String TAG = "Fragment";
    private LoginButton loginButton;
    //    private ImageButton loginwithfacebook;
    public static CallbackManager callbackManager;
    public static View view;
    private String name;
    private String imageurl;
    private String email;

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        //port = new PortToServer("http://143.248.36.38:3000", ((MainActivity)getActivity()).cookies);
        view = inflater.inflate(R.layout.loginpage, container, false);
        loginButton = view.findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String accessToken= loginResult.getAccessToken().getToken();
                //AccessToken accessToken = loginResult.getAccessToken();
                System.out.println(" callback success");
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                getData(object);
                                Log.d("response",response.toString());
                                System.out.println(response.getJSONObject());
                                try {
                                    name=response.getJSONObject().getString("name");
                                    Log.d(TAG,name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    email=response.getJSONObject().getString("email");
                                    Log.d(TAG,email);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    imageurl="http://graph.facebook.com/"+response.getJSONObject().getString("id")+"/picture?width=1000&height=1000";
                                    Log.d(TAG,name+imageurl);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(imageurl);
//                                ImageButton loggingin = (ImageButton) view.findViewById(R.id.loginwithfacebook);
//                                loggingin.setImageResource(R.drawable.loggingin);

                                Fragment fragment = new MainFragment();
                                Bundle arguments = new Bundle();
                                arguments.putString("name",name);
                                arguments.putString("email",email);
                                arguments.putString("url",imageurl);
                                MainActivity.fragment1.setArguments(arguments);
                                //fragment.setArguments(arguments);

                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.anim.down_enter, R.anim.up_out)
                                        .replace(R.id.frameContainer, fragment)
                                        .addToBackStack(null)
                                        .commit();

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,picture,email");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("fail");

            }
        });

        System.out.println("new");

        return view;
    }

    public void getData(JSONObject object) {
        try {
            URL profile_picture = new URL("https://graph.facebook.com/" + object.getString("id") + "/picture?width=250&height=250");
            //Picasso.with(this).load(profile_picture.toString()).into(img)
            System.out.println(profile_picture);
            System.out.println(object.getString("email"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}