package de.adamwest.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by philip on 13/01/15.
 */
public class EventClusterItem implements ClusterItem {

    private final LatLng mPosition;

    public EventClusterItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }


    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
