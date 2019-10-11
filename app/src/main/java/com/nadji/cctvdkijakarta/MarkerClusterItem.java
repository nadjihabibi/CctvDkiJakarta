package com.nadji.cctvdkijakarta;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerClusterItem implements ClusterItem {
    private LatLng latLng;
    private String link;


    public MarkerClusterItem(LatLng latLng, String link) {
        this.latLng = latLng;
        this.link = link;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return link;
    }

    @Override
    public String getSnippet() {
        return "";
    }
}
