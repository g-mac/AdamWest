package de.adamwest.activities_fragments.holiday_detail.fragments.map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
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
import de.adamwest.DatabaseManager;
import de.adamwest.R;
import de.adamwest.activities_fragments.holiday_detail.HolidayDetailActivity;
import de.adamwest.activities_fragments.holiday_detail.event_display.ShowEventActivity;
import de.adamwest.database.Event;
import de.adamwest.database.Holiday;
import de.adamwest.database.Route;
import de.adamwest.database.RouteLocation;
import de.adamwest.helper.Constants;
import de.adamwest.helper.HelpingMethods;
import de.adamwest.helper.ImageHelper;

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

    //    private long selectedRouteId;
//    private long trackedRouteId;
    private long holidayId;

    //----------------------- Activity (LifeCycle) Methods --------------------


    // Fragment is created in the following order (all right after each other)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        HelpingMethods.log("MapFragment onCreate()");
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HelpingMethods.log("MapFragment onCreateView()");

        View inflatedView = inflater.inflate(R.layout.fragment_map, container, false);

        holidayId = ((HolidayDetailActivity) getActivity()).getHolidayId();

        //this might change, need to put to approriate location in code, so it gets called when it changes
//        selectedRouteId = ((HolidayDetailActivity) getActivity()).selectedRouteId;
//        trackedRouteId = ((HolidayDetailActivity) getActivity()).trackedRouteId;

        MapsInitializer.initialize(getActivity());

        mapView = (MapView) inflatedView.findViewById(R.id.map_view);
        mapView.onCreate(mBundle);

//        map = ((MapView) inflatedView.findViewById(R.id.map_view)).getMap();
        map = mapView.getMap();
        map.setMyLocationEnabled(true); // Enable MyLocation Layer of Google Map
        map.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);
        map.setOnMapLoadedCallback(this);


        return inflatedView;
    }

    @Override
    public void onStart() {
        HelpingMethods.log("MapFragment onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        HelpingMethods.log("MapFragment onResume()");
        super.onResume();
        mapView.onResume();
    }

    // Fragment is destroyed in the following order (all right after each other)

    @Override
    public void onPause() {
        HelpingMethods.log("MapFragment onPause()");
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        HelpingMethods.log("MapFragment onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        HelpingMethods.log("MapFragment onDestroy()");
        mapView.onDestroy();
        super.onDestroy();
    }


    //----------------------- Map Methods -------------------------------------

    public void setUpMap() {
        //GENERAL SETUP

        map.clear();
//        eventMarkerMap = new HashMap<Marker, Long>(); // todo: check if needed
//        eventMarkerMap.clear(); //todo: checks if this works instead of the above?!

        Holiday holiday = DatabaseManager.getHolidayFromId(getActivity(), holidayId);
        boolean holidayHasLocationPoints = HelpingMethods.holidayHasLocationPoints(holiday);
        HelpingMethods.log("holidayHasLocationPoints: " + holidayHasLocationPoints);

        if (!holidayHasLocationPoints)
//        zoomMapToCurrentPostition();
            return;

        long selectedRouteId = ((HolidayDetailActivity) getActivity()).selectedRouteId;
        long trackedRouteId = ((HolidayDetailActivity) getActivity()).trackedRouteId;

        //DRAWING

        if (selectedRouteId != -1) { //if a route is selected draw only that route
            Route route = DatabaseManager.getRouteFromId(getActivity(), selectedRouteId);
            addRouteToMap(route, activeColor);
        } else { //if all routes /the whole holiday is selected, draw all routes
            List<Route> routeList = holiday.getRouteList();
            for (Route route : routeList) {
                int color = HelpingMethods.getRandomColor();
                addRouteToMap(route, color);
            }
        }

        //ZOOMING

        //Zoom map to correct holidaybounds OR routebounds OR currentloc todo: add button for the below functionality
//        if(trackedRouteId != -1){
//            moveMapTo(currentPostition);
//        }
        if (selectedRouteId != -1) { //if a route is selected zoom to that route
            Route route = DatabaseManager.getRouteFromId(getActivity(), selectedRouteId);
            if (route != null && HelpingMethods.routeHasLocationPoints(route))
                zoomMapToBounds(HelpingMethods.getRouteBoundaries(route));
//            else zoomMapToCurrentPostition();
        } else { //if all routes /the whole holiday is selected, draw all routes
//            Holiday holiday = DatabaseManager.getHolidayFromId(getActivity(), holidayId);
            if (holidayHasLocationPoints)
                zoomMapToBounds(HelpingMethods.getHolidayBoundaries(holiday));
//            else zoomMapToCurrentPostition();
        }
    }

    private void addRouteToMap(Route route, int color) {
        drawTrackOnMap(route, color);
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
            if (i == 0) {
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Start!")
                        .snippet(route.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_start_finish)));
//                map.addCircle(HelpingMethods.getCircleOptions().center(latLng));
            } else if (i == routeLocationList.size() - 1
                    && route.getId() != ((HolidayDetailActivity) getActivity()).trackedRouteId) {
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Finish!")
                        .snippet(route.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_start_finish)));
            }
        }

        map.addPolyline(options);
    }

    private void addEventMarkersToMap(Route route) {
        for (Event event : route.getEventList()) {
            LatLng pos = new LatLng(event.getRouteLocation().getLatitude(), event.getRouteLocation().getLongitude());
            EventClusterItem eventClusterItem = new EventClusterItem(event.getRouteLocation().getLatitude(), event.getRouteLocation().getLongitude(), event);
            eventClusterManager.addItem(eventClusterItem);
        }
    }

    public void zoomMapToBounds(LatLngBounds bounds) {
//        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
    }

    // not in use

    private void moveMapTo(LatLng latLng) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    }

    //    public void onToggleMapClick(View view) {
    public void toggleMap() {
        if (map.getMapType() == GoogleMap.MAP_TYPE_HYBRID) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL); //set map type: normal
        } else {
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID); //set map type: satellite
        }
    }

    //----------------------- Implemented Methods -----------------------------

    //todo: might not be needed since replaced by onInfoWindowClick()
    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getActivity(), "onMarkerClick", Toast.LENGTH_SHORT).show();
        return false;
    }

    // only loaded once visible. onCreate, onCreateView, onStart and onResume are called beforehand.
    @Override
    public void onMapLoaded() {
        HelpingMethods.log("MapFragment onMapLoaded()");
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
            //todo: compare with outside override
            @Override
            public void onInfoWindowClick(Marker marker) {
//                Intent intent = new Intent(getActivity().getApplicationContext(), ShowEventActivity.class);
//                intent.putExtra(Constants.KEY_EVENT_ID, clickedClusterItem.getEvent().getId());
//                startActivity(intent);
            }
        });
        setUpMap();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        //philips new code (crashes for start/finish markers)
//        Intent intent = new Intent(getActivity().getApplicationContext(), ShowEventActivity.class);
//        intent.putExtra(Constants.KEY_EVENT_ID, clickedClusterItem.getEvent().getId());
//        startActivity(intent);


        //old code
////        Toast.makeText(getActivity(), "onInfoWindowClick", Toast.LENGTH_SHORT).show();
//        if (eventMarkerMap != null && eventMarkerMap.containsKey(marker)) {
//            long eventId = eventMarkerMap.get(marker);
//            Log.i("prose", "clicked on event");
//            Toast.makeText(getActivity(), "Marker Click succesful, eventID: " + eventId, Toast.LENGTH_SHORT).show();
////            android.support.v4.app.Fragment eventFragment = new EventFragment();
////            Bundle args = new Bundle();
////            args.putLong(Constants.KEY_EVENT_ID, eventId);
////            eventFragment.setArguments(args);
////            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_map_layout, eventFragment, Constants.TAG_EVENT_FRAGMENT).addToBackStack("").commit();
//        }
    }

    //----------------------- Other Methods -----------------------------------

    public class ClusterItemInfoAdapter implements GoogleMap.InfoWindowAdapter {


        public ClusterItemInfoAdapter() {

        }

        @Override
        public View getInfoWindow(Marker marker) {
            View infoView = getActivity().getLayoutInflater().inflate(R.layout.event_marker_info_popup, null);
            ImageView thumbnailView = ((ImageView) infoView.findViewById(R.id.image_view_event_image));

            String eventType = clickedClusterItem.getEvent().getType();
            if (eventType.equals(Constants.TYPE_IMAGE)) {
                thumbnailView.setImageBitmap(ImageHelper.resizeBitMap(getActivity(), clickedClusterItem.getEvent().getPath()));
            } else if (eventType.equals(Constants.TYPE_VIDEO)) {
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(clickedClusterItem.getEvent().getPath(),
                        MediaStore.Images.Thumbnails.MINI_KIND);
                thumbnailView.setImageBitmap(thumb);
                infoView.findViewById(R.id.image_view_video_play).setVisibility(View.VISIBLE);

            } else if (eventType.equals(Constants.TYPE_TEXT)) {
                thumbnailView.setVisibility(View.GONE);
            }
            //TODO check different kind of types
            ((TextView) infoView.findViewById(R.id.text_view_event_description)).setText(clickedClusterItem.getEvent().getDescription());
            HelpingMethods.log("Info window get!!! type: " + clickedClusterItem.getEvent().getType());
            return infoView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }


}

// trash?

//    public void zoomMapToRoute(Route route) {
//        LatLngBounds bounds = HelpingMethods.getRouteBoundaries(route);
////        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
//        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
//    }
//
//    public void zoomMapToHoliday(Holiday holiday) {
//        LatLngBounds bounds = HelpingMethods.getHolidayBoundaries(holiday);
////        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
//        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
//    }


//    public void setUpMap2() {
//        long selectedRouteId = ((HolidayDetailActivity) getActivity()).selectedRouteId;
//
//        map.clear();
//        eventMarkerMap = new HashMap<Marker, Long>();
////        eventMarkerMap.clear();
//
//        Holiday holiday = DatabaseManager.getHolidayFromId(getActivity(), holidayId);
//
//        boolean holidayHasLocationPoints = HelpingMethods.holidayHasLocationPoints(holiday);
//        HelpingMethods.log("holidayHasLocationPoints: " + holidayHasLocationPoints);
//
//        if (holidayHasLocationPoints) {
//            addRoutesToMap(holiday);
//        }
//
//
//        //Zoom map to correct holidaybounds OR routebounds todo: add button for the below functionality
//        if (selectedRouteId != -1) {
//            Route route = DatabaseManager.getRouteFromId(getActivity(), selectedRouteId);
//            if (route != null && HelpingMethods.routeHasLocationPoints(route))
////            if (route != null)
//                zoomMapToBounds(HelpingMethods.getRouteBoundaries(route));
////            else zoomMapToCurrentPostition();
//        } else {
////            Holiday holiday = DatabaseManager.getHolidayFromId(getActivity(), holidayId);
//            if (holidayHasLocationPoints)
//                zoomMapToBounds(HelpingMethods.getHolidayBoundaries(holiday));
////            else zoomMapToCurrentPostition();
//        }
//
////        //go to current position
////        Location currentLoc = mLocationClient.getLastLocation();
////        moveMapTo(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()));
//    }

//    public void addRoutesToMap(Holiday holiday) {
//        long selectedRouteId = ((HolidayDetailActivity) getActivity()).selectedRouteId;
//
//        // a route is currently selected
//        if (selectedRouteId != -1) {
//
////            Route selectedRoute = DatabaseManager.getRouteFromId(getActivity(), selectedRouteId);
////            addActiveRouteToMap(selectedRoute);
//
//            List<Route> routeList = holiday.getRouteList();
//            for (Route route : routeList) {
//                if (route.getId() == selectedRouteId)
//                    addActiveRouteToMap(route);
//                else
//                    addInactiveRouteToMap(route);
//            }
//        }
//
//        // no route is selected
//        else {
//            List<Route> routeList = holiday.getRouteList();
//            for (Route route : routeList) {
//                addActiveRouteToMap(route);
//            }
//        }
//    }


//    private void addInactiveRouteToMap(Route route) {
//        drawTrackOnMap(route, inactiveColor);
//    }


//    private void drawActiveRoute(Holiday holiday) {
//
//        if (holiday.getRoute() != null)
//            drawRouteOnMap(holiday, holiday.getRoute(), activeColor);
//    }

//    private void drawInactiveRoutes(Holiday holiday) {
//        List<Route> routeList = holiday.getRouteList();
//        for (Route route : routeList) {
//            if (route.getId() != holiday.getCurrentRouteId())
//                drawRouteOnMap(holiday, route, inactiveColor);
//        }
//    }

//    private void drawRouteOnMap(Holiday holiday, Route route, int lineColor) {
//
////        List<RouteLocation> routeLocationList = currentHoliday.getCurrentRoute().getRouteLocationList();
//        List<RouteLocation> routeLocationList = route.getRouteLocationList();
//
//        PolylineOptions options = new PolylineOptions()
//                .width(22)
//                .color(lineColor);
//
//        for (int i = 0; i < routeLocationList.size(); i++) {
//            Double latitude = routeLocationList.get(i).getLatitude();
//            Double longitude = routeLocationList.get(i).getLongitude();
//            LatLng latLng = new LatLng(latitude, longitude);
//            options.add(latLng);
//            if (i == 0) {
//                map.addMarker(new MarkerOptions()
//                        .position(latLng)
//                        .title("Start!")
//                        .snippet(route.getName())
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_start_finish)));
//
//            } //todo: finish marker
//            else if (i == routeLocationList.size() - 1) {
//                if (route.getId() != holiday.getCurrentRouteId())
//                    map.addMarker(new MarkerOptions()
//                            .position(latLng)
//                            .title("Finish!")
//                            .snippet(route.getName())
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_start_finish)));
//            }
//        }
//
//
//        //TODO implement some button to show/hide the events for each map
////        if (holiday.getRoute() != null) {
//        if (route != null) {
//            eventMarkerMap = new HashMap<Marker, Long>();
////            for (Event event : holiday.getRoute().getEventList()) {
//            for (Event event : route.getEventList()) {
//                LatLng pos = new LatLng(event.getRouteLocation().getLatitude(), event.getRouteLocation().getLongitude());
//                Marker marker = map.addMarker(new MarkerOptions()
//                        .position(pos)
//                        .title("Event")
//                        .snippet(event.getName())
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_picture)));
//                eventMarkerMap.put(marker, event.getId());
//            }
//        }
//
//        map.addPolyline(options);
//
//    }
