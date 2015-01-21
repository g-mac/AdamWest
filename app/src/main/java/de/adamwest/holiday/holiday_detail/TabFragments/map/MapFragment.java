package de.adamwest.holiday.holiday_detail.TabFragments.map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import de.adamwest.R;
import de.adamwest.database.*;
import de.adamwest.helper.Constants;
import de.adamwest.helper.HelpingMethods;
import de.adamwest.helper.ImageHelper;
import de.adamwest.holiday.holiday_detail.HolidayDetailActivity;
import de.adamwest.holiday.holiday_detail.event_display.ShowEventActivity;
import de.adamwest.holiday.holiday_detail.event_display.ShowPictureFragment;
import de.adamwest.holiday.holiday_edit.MapActivity;
import de.adamwest.holidaylist.HolidayListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Philip on 18.10.2014.
 */
public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLoadedCallback {

    static final int inactiveColor = Color.argb(155, 110, 110, 110); // -transparent gray
    static final int activeColor = Color.argb(125, 0, 255, 0); // -transparent green
    private final int MAXIMUM_ZOOM_FOR_CLUSTER = 19;
    private MapView mapView;
    private GoogleMap map;
    private Bundle mBundle;

    private Map<Marker, Long> eventMarkerMap;
    private ClusterManager<EventClusterItem> eventClusterManager;
    private EventClusterItem clickedClusterItem;

    private long routeId;
    private long holidayId;

    //----------------------- Activity (LifeCycle) Methods --------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_map, container, false);

        routeId = ((HolidayDetailActivity) getActivity()).routeId;
        holidayId = ((HolidayDetailActivity) getActivity()).getHolidayId();

        MapsInitializer.initialize(getActivity());

        mapView = (MapView) inflatedView.findViewById(R.id.map_view);
        mapView.onCreate(mBundle);

//        map = ((MapView) inflatedView.findViewById(R.id.map_view)).getMap();
        map = mapView.getMap();

        map.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);
        map.setOnMapLoadedCallback(this);

        return inflatedView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    //----------------------- Map Methods -------------------------------------

    public void setUpMap() {
        map.clear();
        eventMarkerMap = new HashMap<Marker, Long>();
//        eventMarkerMap.clear();

        Holiday holiday = DatabaseManager.getHolidayFromId(getActivity(), holidayId);

        boolean holidayHasRoutes = (holiday != null && holiday.getRouteList()!=null && holiday.getRouteList().size()>0);

        if (holidayHasRoutes) {
            addRoutesToMap(holiday);
        }

        //Zoom map to correct holidaybounds OR routebounds
        if (routeId != -1) {
            Route route = DatabaseManager.getRouteFromId(getActivity(), routeId);
            if (route != null)
                zoomMapToBounds(HelpingMethods.getRouteBoundaries(route));
        } else {
//            Holiday holiday = DatabaseManager.getHolidayFromId(getActivity(), holidayId);
            if (holidayHasRoutes)
                zoomMapToBounds(HelpingMethods.getHolidayBoundaries(holiday));
        }

//        //go to current position
//        Location currentLoc = mLocationClient.getLastLocation();
//        moveMapTo(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()));
    }

    public void addRoutesToMap(Holiday holiday) {

        // a route is currently selected
        if (routeId != -1) {

//            Route selectedRoute = DatabaseManager.getRouteFromId(getActivity(), routeId);
//            addActiveRouteToMap(selectedRoute);

            List<Route> routeList = holiday.getRouteList();
            for (Route route : routeList) {
                if (route.getId() == routeId)
                    addActiveRouteToMap(route);
                else
                    addInactiveRouteToMap(route);
            }
        }

        // no route is selected
        else {
            List<Route> routeList = holiday.getRouteList();
            for (Route route : routeList) {
                addActiveRouteToMap(route);
            }
        }
    }

    private void addInactiveRouteToMap(Route route) {
        drawTrackOnMap(route, inactiveColor);
    }

    private void addActiveRouteToMap(Route route) {
        drawTrackOnMap(route, activeColor);
        addEventMarkersToMap(route);
    }

    private void drawTrackOnMap(Route route, int lineColor) {

        List<RouteLocation> routeLocationList = route.getRouteLocationList();

        PolylineOptions options = new PolylineOptions()
                .width(22)
                .color(lineColor);

        for (int i = 0; i < routeLocationList.size(); i++) {
            Double latitude = routeLocationList.get(i).getLatitude();
            Double longitude = routeLocationList.get(i).getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            options.add(latLng);

            //todo: start/finish marker
//            if (i == 0) {
//                map.addMarker(new MarkerOptions()
//                        .position(latLng)
//                        .title("Start!")
//                        .snippet(route.getName())
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_start_finish)));
////                map.addCircle(HelpingMethods.getCircleOptions().center(latLng));
//            }
//            else if (i == routeLocationList.size() - 1) {
//                map.addMarker(new MarkerOptions()
//                        .position(latLng)
//                        .title("Finish!")
//                        .snippet(route.getName())
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_start_finish)));
//            }
        }

        map.addPolyline(options);
    }

    private void addEventMarkersToMap(Route route) {
        for (Event event : route.getEventList()) {
            LatLng pos = new LatLng(event.getRouteLocation().getLatitude(), event.getRouteLocation().getLongitude());
            BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.map_marker_picture);
//            Marker marker = map.addMarker(new MarkerOptions()
//                    .position(pos)
//                    .title("Event")
////                    .title(event.getName())
//                    .snippet(event.getName())
//                    .icon(markerIcon));
           // eventMarkerMap.put(marker, event.getId());
        EventClusterItem eventClusterItem = new EventClusterItem(event.getRouteLocation().getLatitude(), event.getRouteLocation().getLongitude(), event);
        eventClusterManager.addItem(eventClusterItem );
        }
    }

    public void zoomMapToBounds(LatLngBounds bounds) {
//        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
    }

    //----------------------- Implemented Methods -----------------------------

    //todo: might not be needed since replaced by onInfoWindowClick()
    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getActivity(), "onMarkerClick", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
//        Toast.makeText(getActivity(), "onInfoWindowClick", Toast.LENGTH_SHORT).show();
        if (eventMarkerMap != null && eventMarkerMap.containsKey(marker)) {
            long eventId = eventMarkerMap.get(marker);
            Log.i("prose", "clicked on event");
            Toast.makeText(getActivity(), "Marker Click succesful, eventID: "+eventId, Toast.LENGTH_SHORT).show();
//            android.support.v4.app.Fragment eventFragment = new EventFragment();
//            Bundle args = new Bundle();
//            args.putLong(Constants.KEY_EVENT_ID, eventId);
//            eventFragment.setArguments(args);
//            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_map_layout, eventFragment, Constants.TAG_EVENT_FRAGMENT).addToBackStack("").commit();
        }
    }

    @Override
    public void onMapLoaded() {
        eventClusterManager = new ClusterManager<EventClusterItem>(getActivity(), map);
        eventClusterManager.setAlgorithm(new EventClusterAlgorithm<EventClusterItem>());
        eventClusterManager.setRenderer(new EventClusterRenderer(getActivity(), map, eventClusterManager));
        eventClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<EventClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<EventClusterItem> eventClusterItemCluster) {
                if (MAXIMUM_ZOOM_FOR_CLUSTER > map.getCameraPosition().zoom) {
                    CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(eventClusterItemCluster.getPosition(), MAXIMUM_ZOOM_FOR_CLUSTER);
                    map.animateCamera(zoom);
                } else {
                    ArrayList<Event> events = new ArrayList<Event>();
                    for (EventClusterItem clusterItem : eventClusterItemCluster.getItems()) {
                        events.add(clusterItem.getEvent());
                    }
                    ((HolidayDetailActivity) getActivity()).setClusterEventList(events);
                    Fragment clusterItemsOverlayFragment = new ClusterItemsOverlayFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.map_view, clusterItemsOverlayFragment).addToBackStack("").commit();

                }
                return true;
            }
        });
        eventClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<EventClusterItem>() {
            @Override
            public boolean onClusterItemClick(EventClusterItem eventClusterItem) {
                HelpingMethods.log("Single Item Clicked");
                clickedClusterItem = eventClusterItem;
                return false;
            }
        });
        map.setOnCameraChangeListener(eventClusterManager);
        map.setOnMarkerClickListener(eventClusterManager);
        map.setInfoWindowAdapter(eventClusterManager.getMarkerManager());
        eventClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new ClusterItemInfoAdapter());
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ShowEventActivity.class);
                intent.putExtra(Constants.KEY_EVENT_ID, clickedClusterItem.getEvent().getId());
                startActivity(intent);
            }
        });
        setUpMap();
    }

    //----------------------- Other Methods -----------------------------------


    public class ClusterItemInfoAdapter implements GoogleMap.InfoWindowAdapter {


        public ClusterItemInfoAdapter() {

        }
        @Override
        public View getInfoWindow(Marker marker) {
            //TODO check different kind of types
            View infoView = getActivity().getLayoutInflater().inflate(R.layout.event_marker_info_popup, null);
            //((ImageView)infoView.findViewById(R.id.image_view_event_image)).setImageBitmap(ImageHelper.resizeBitMap(getActivity(), clickedClusterItem.getEvent().getPath()));
            //((TextView)infoView.findViewById(R.id.text_view_event_description)).setText(clickedClusterItem.getEvent().getDescription());
            HelpingMethods.log("Info window get!!! type: " + clickedClusterItem.getEvent().getType());
            return infoView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }


}
