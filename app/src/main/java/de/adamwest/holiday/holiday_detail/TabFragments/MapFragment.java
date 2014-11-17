package de.adamwest.holiday.holiday_detail.TabFragments;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.*;
import de.adamwest.R;
import de.adamwest.database.*;
import de.adamwest.helper.CameraManager;
import de.adamwest.helper.HelpingMethods;
import de.adamwest.holiday.holiday_detail.HolidayDetailActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Philip on 18.10.2014.
 */
public class MapFragment extends Fragment {

    static final int inactiveColor = Color.argb(155, 255, 255, 255); // -transparent gray
    static final int activeColor = Color.argb(125, 0, 255, 0); // -transparent green

    private MapView mapView;
    private GoogleMap map;
    private Bundle mBundle;
    private CameraManager cameraManager;

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

        map = ((MapView) inflatedView.findViewById(R.id.map_view)).getMap();

        cameraManager = CameraManager.getCameraManager(getActivity());

        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                setUpMap();
            }
        });

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
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    //----------------------- Map Methods -------------------------------------

    public void setUpMap() {
        map.clear();

//        //go to current position
//        Location currentLoc = mLocationClient.getLastLocation();
//        moveMapTo(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()));

        Holiday holiday = DatabaseManager.getHolidayFromId(getActivity(), holidayId);

        if (holiday != null) {
            drawInactiveRoutes(holiday);
        }

        //Zoom map to correct holidaybounds OR routebounds
        if (routeId != -1) {
            Route route = DatabaseManager.getRouteFromId(getActivity(), routeId);
            if (route != null)
                zoomMapToBounds(HelpingMethods.getRouteBoundaries(route));
        } else {
//            Holiday holiday = DatabaseManager.getHolidayFromId(getActivity(), holidayId);
            if(holiday!=null)
                zoomMapToBounds(HelpingMethods.getHolidayBoundaries(holiday));
        }
    }

    private void drawInactiveRoutes(Holiday holiday) {
        List<Route> routeList = holiday.getRouteList();
        for (Route route : routeList) {
            if (route.getId() != holiday.getCurrentRouteId())
                drawRouteOnMap(route, activeColor);
        }
    }

    private void drawRouteOnMap(Route route, int lineColor) {

        List<RouteLocation> routeLocationList = route.getRouteLocationList();

        PolylineOptions options = new PolylineOptions()
                .width(22)
                .color(lineColor);

        for (int i = 0; i < routeLocationList.size(); i++) {
            Double latitude = routeLocationList.get(i).getLatitude();
            Double longitude = routeLocationList.get(i).getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            options.add(latLng);
            if (i == 0) {
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Start!")
                        .snippet(route.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_green)));

            } //todo: finish marker
            else if (i == routeLocationList.size() - 1) {
                    map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Finish!")
                            .snippet(route.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_red)));
            }
        }

        map.addPolyline(options);
    }

    public void zoomMapToBounds(LatLngBounds bounds) {
//        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
    }

    //----------------------- Other Methods -----------------------------------

}
