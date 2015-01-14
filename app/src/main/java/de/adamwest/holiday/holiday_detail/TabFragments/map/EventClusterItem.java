package de.adamwest.holiday.holiday_detail.TabFragments.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import de.adamwest.database.Event;

/**
 * Created by philip on 13/01/15.
 */
public class EventClusterItem implements ClusterItem {

    private final LatLng mPosition;
    private final Event event;

    public EventClusterItem(double lat, double lng, Event event) {
        mPosition = new LatLng(lat, lng);
        this.event = event;
    }


    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public Event getEvent() {
        return event;
    }
}
