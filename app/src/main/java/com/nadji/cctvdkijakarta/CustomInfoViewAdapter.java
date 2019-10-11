package com.nadji.cctvdkijakarta;

import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CustomInfoViewAdapter implements GoogleMap.InfoWindowAdapter {

    private final LayoutInflater mInflater;

    public CustomInfoViewAdapter(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        final View popup = mInflater.inflate(R.layout.info_marker_layout, null);

        double lat = marker.getPosition().latitude;
        double lng = marker.getPosition().longitude;

        Geocoder geocoder = new Geocoder(AppController.getContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {

//                            addresses = String.valueOf(geocoder.getFromLocation(Double.valueOf(item.getLat()), Double.valueOf(item.getLng()), 1));
//                            addresses = geocoder.getFromLocation(Double.valueOf(item.getLat()), Double.valueOf(item.getLng()), 1);
            addresses = geocoder.getFromLocation(Double.valueOf(lat), Double.valueOf(lng), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String alamat = addresses.get(0).getAddressLine(0);

        ((TextView) popup.findViewById(R.id.info_window)).setText(alamat);


//        ((TextView) popup.findViewById(R.id.info_window)).setText(String.valueOf(lat));
        return popup;
//        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
//        final View popup = mInflater.inflate(R.layout.info_marker_layout, null);
//        ((TextView) popup.findViewById(R.id.info_window)).setText(String.valueOf(marker.getPosition()));
//        return popup;
        return null;
    }
}

//public class CustomInfoViewAdapter implements GoogleMap.InfoWindowAdapter {
//    private final LayoutInflater mInflater;
//
//    public CustomInfoViewAdapter(LayoutInflater inflater) {
//        this.mInflater = inflater;
//    }
//
//    @Override
//    public View getInfoWindow(Marker marker) {
//        final View popup = mInflater.inflate(R.layout.info_window_layout, null);
//        ((TextView) popup.findViewById(R.id.title)).setText(marker.getSnippet());
//        return popup;
//        //return null;
//    }
//
//    @Override
//    public View getInfoContents(Marker marker) {
//        final View popup = mInflater.inflate(R.layout.info_window_layout, null);
//        ((TextView) popup.findViewById(R.id.title)).setText(marker.getSnippet());
//        return popup;
//    }
//}
