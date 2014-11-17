package de.adamwest.holiday.holiday_detail.TabFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLngBounds;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.database.Holiday;
import de.adamwest.database.Route;
import de.adamwest.helper.CameraManager;
import de.adamwest.helper.HelpingMethods;
import de.adamwest.holiday.holiday_detail.HolidayDetailActivity;

/**
 * Created by Philip on 18.10.2014.
 */
public class MapFragment extends Fragment {


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

    private void setUpMap() {
//        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        //Zoom map to correct holidaybounds OR routebounds
        if (routeId != -1) {
            Route route = DatabaseManager.getRouteFromId(getActivity(), routeId);
            if (route != null)
                zoomMapToBounds(HelpingMethods.getRouteBoundaries(route));
        } else {
            Holiday holiday = DatabaseManager.getHolidayFromId(getActivity(), holidayId);
            if(holiday!=null)
                zoomMapToBounds(HelpingMethods.getHolidayBoundaries(holiday));
        }
    }

    public void zoomMapToBounds(LatLngBounds bounds) {
//        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
    }


    //----------------------- Other Methods -----------------------------------

}
