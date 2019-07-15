package com.example.madcamp_3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Fragment2 extends Fragment implements OnMapReadyCallback {
    View view;
    Context mContext;
    private GoogleMap mMap;
    private MapView mapView;
    LocationManager locationManager;
    Spinner spinner;
    MarkerOptions markerOptions;
    ImageView trashcan;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment2, container, false);
        spinner = view.findViewById(R.id.spinner);
        trashcan= view.findViewById(R.id.trashcan);
        trashcan.setBackground(new ShapeDrawable(new OvalShape()));
        trashcan.setClipToOutline(true);
        trashcan.setAlpha((float)0.51);
        trashcan.setVisibility(View.INVISIBLE);
        //spinner.setVisibility(View.INVISIBLE);
        mapView=(MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        //SupportMapFragment mapFragment= (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap){

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            if(googleMap!=null){
                System.out.println("breakpoint");
                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
//                        spinner.setX((float) latLng.latitude);
//                        spinner.setY((float) latLng.longitude);
//                        spinner.setVisibility(View.VISIBLE);
                        if (Fragment1.vehiclecollection.size() == 0) {
                            Toast.makeText(mContext.getApplicationContext(), "No vehicle registered", Toast.LENGTH_SHORT).show();
                        } else {

                            Projection projection = googleMap.getProjection();
                            Point screenPosition =projection.toScreenLocation(latLng);
                            spinner.setVisibility(View.VISIBLE);
                            spinner.setX(screenPosition.x);
                            spinner.setY(screenPosition.y);
                            setSpinner(spinner,latLng,googleMap);
                            System.out.println("once or twice?");
                        }
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        System.out.println(marker.getSnippet()+"clicked");
                        Toast.makeText(mContext.getApplicationContext(),"This is vehicle "+marker.getSnippet(),Toast.LENGTH_SHORT).show();
                        spinner.setVisibility(View.INVISIBLE);
                        return true;
                    }
                });

                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {
                        trashcan.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        int w= trashcan.getWidth();
                        int h= trashcan.getHeight();
                        int[] coords= new int[2];
                        trashcan.getLocationOnScreen(coords);
                        Rect rect = new Rect(coords[0],coords[1],coords[0]+w,coords[1]+h);
                        Projection projection = googleMap.getProjection();
                        Point screenPosition =projection.toScreenLocation(marker.getPosition());
                        if(rect.contains((int)screenPosition.x,(int)screenPosition.y)){
                            marker.remove();
                        }
                        trashcan.setVisibility(View.INVISIBLE);
                    }
                });

                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        System.out.println("breakpoint2");
                        System.out.println(location);
                        System.out.println(location.getAltitude());
                        //googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("its me"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),16f));
                    }
                });
            }
        }
        System.out.println("not map");
    }

    private void setSpinner(final Spinner spinners,final LatLng latLng,final GoogleMap googleMap){
        int length= Fragment1.vehiclecollection.size();
        String[] arraySpinner= new String[length];
        for(int i=0; i<length; i++){
            arraySpinner[i]=Fragment1.vehiclecollection.get(i).getVehicle();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinners.setAdapter(adapter);
        markerOptions = new MarkerOptions().position(latLng);
        final Marker marker= googleMap.addMarker(markerOptions);
        marker.setDraggable(true);

        spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String text =adapterView.getItemAtPosition(i).toString();
                marker.setSnippet(text);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


}