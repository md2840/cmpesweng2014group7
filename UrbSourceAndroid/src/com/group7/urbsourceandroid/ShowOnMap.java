package com.group7.urbsourceandroid;


import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.group7.urbsourceandroid.R.*;

/**
 * Created by Admir Nurkovic on 1/7/15.
 */
public class ShowOnMap extends FragmentActivity {

    GoogleMap mMap;
    public static FragmentManager fragmentManager;
    Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.showmap);

        setUpMapIfNeeded();
        String latling = getIntent().getExtras().getString("LatLing");
        String[] geo = latling.split(",");
        if (!geo[0].equals("") && !geo[1].equals("")){
            double latitude = Double.parseDouble(geo[0]);
            double longitude = Double.parseDouble(geo[1]);
            LatLng latLng = new LatLng(latitude, longitude);
            if (marker != null) {
                marker.remove();
            }
            marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Story")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.markerlogo))
            );

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        }
    }


    void setUpMapIfNeeded(){
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();




        }
    }





}
