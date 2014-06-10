package com.sigmobile.ucf_news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jjvg on 5/27/14.
 */
public class UcfMapFragment extends SupportMapFragment {

    private static final LatLng UCF_LAT_LNG = new LatLng(28.602025, -81.200820);
    private GoogleMap mGoogleMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mGoogleMap = getMap();

        //16 (other good zoom)
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UCF_LAT_LNG, 15));

        // Zoom in, animating the camera. 15
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15.5f), 2000, null);


        return v;
    }
}
