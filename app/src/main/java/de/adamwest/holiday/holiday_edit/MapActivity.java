package de.adamwest.holiday.holiday_edit;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;
import de.adamwest.R;
import de.adamwest.database.*;
import de.adamwest.helper.CameraManager;
import de.adamwest.helper.Constants;
import de.adamwest.helper.HelpingMethods;
import de.adamwest.holiday.event.EventFragment;
import de.adamwest.holiday.holiday_detail.TabFragments.EventGridFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapActivity extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener {

    private long currentHolidayId = -1;
    public int selectedRouteId = -1;
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    static final int activeColor = Color.argb(125, 0, 255, 0); // -transparent green
    static final int inactiveColor = Color.argb(155, 255, 255, 255); // -transparent gray
    private final int EVENT_MARKER_ID = 666;
    private GoogleMap map;
    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;
    private Holiday currentHoliday;
    private Map<Marker, Long> eventMarkerMap;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Location currentLoc;
    private CameraManager cameraManager;

    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL = 15000;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL = 1000;


    //----------------------- Activity (LifeCycle) Methods --------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        cameraManager = CameraManager.getCameraManager( this);
        mLocationClient = new LocationClient(this, this, this);
        mLocationClient.connect(); //ToDo: move to onStart?!

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        currentHolidayId = getIntent().getLongExtra(Constants.KEY_HOLIDAY_ID, -1);
        if (currentHolidayId != -1) {
            currentHoliday = DatabaseManager.getHolidayFromId(getApplicationContext(), currentHolidayId);
        }

        initRouteDrawerList();

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true); // Enable MyLocation Layer of Google Map
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID); //set map type: satellite
        map.setOnMarkerClickListener(this);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
//        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
//        mLocationClient.disconnect();
        super.onStop();
    }

    //----------------------- OnClick Methods ---------------------------------

    public void onTestButtonClick(View view) {
        //perform test actions in this method
        //Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_map_layout, new EventGridFragment()).commit();

//        Route testRoute = currentHoliday.getRouteList().get(0);
//        if (testRoute == null)
//            return;
//        zoomMapToRoute(testRoute);
    }

    public void onToggleMapClick(View view) {
        if (map.getMapType() == GoogleMap.MAP_TYPE_HYBRID) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL); //set map type: normal
        } else {
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID); //set map type: satellite
        }
    }

    public void onFinishClick(View view) {
        Toast.makeText(getApplicationContext(), "finish", Toast.LENGTH_LONG).show();
        removeActiveRoute();
    }

    //----------------------- Main Methods ------------------------------------

    private void removeActiveRoute() {
        //todo: select route (so it will be zoomed to in setupmap())
        selectedRouteId = currentHoliday.getRouteList().indexOf(currentHoliday.getCurrentRoute());
        DatabaseManager.removeActiveRouteForHoliday(getApplicationContext(), currentHolidayId);
        updateRouteList();
        setUpMap();
    }

    public long getCurrentHolidayId() {
        return currentHolidayId;
    }

    private void initRouteDrawerList() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle("Closed");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("open");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        Holiday currentHoliday = DatabaseManager.getHolidayFromId(this, currentHolidayId);
        if (currentHoliday != null) {
            mDrawerList.setAdapter(new RouteListAdapter(currentHoliday.getRouteList(), this, currentHolidayId));
        }

    }

    public void updateRouteList() {
        RouteListAdapter adapter = (RouteListAdapter) mDrawerList.getAdapter();
        adapter.notifyDataSetChanged();
    }

    //----------------------- Map Methods -------------------------------------

    public void setUpMap() {
        map.clear();
        //go to current position
        Location currentLoc = mLocationClient.getLastLocation();
        moveMapTo(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()));

        if (currentHoliday == null) {
            //todo: error handling: activity started without existing holiday
        } else {

            List<Route> routeList = currentHoliday.getRouteList();

            if (routeList == null || routeList.size() == 0) {
                // todo: do nothing / or prompt to start new route
            } else {

                Route route = currentHoliday.getCurrentRoute();

                if (route != null) {
                    drawInactiveRoutes();
                    drawActiveRoute();
                    //proceed with range check / tracking
                } else {
                    drawInactiveRoutes();

//                    if(currentHoliday.getSelectedMap()!=null)
//                    zoomMapToRoute(selectedRoute);
//                    else
                    if (selectedRouteId != -1) {
                        Route selectedRoute = currentHoliday.getRouteList().get(selectedRouteId);
                        if (currentHoliday.getRouteList().get(selectedRouteId) != null) {
                            zoomMapToRoute(selectedRoute);
                        }
                    } else
                        zoomMapToCurrentHoliday(); //show entire holiday

                    //draw the routes: (all in different colors)
                    //1) draw all unselected routes (super transparent)
                    //2) draw selected route (zoomed to, and less transparent or not at all transp.)
                }
            }


        }
    }

    private void drawActiveRoute() {
        if (currentHoliday.getCurrentRoute() != null)
            drawRouteOnMap(currentHoliday.getCurrentRoute(), activeColor);
    }

    private void drawInactiveRoutes() {
        List<Route> routeList = currentHoliday.getRouteList();
        for (Route route : routeList) {
            if (route.getId() != currentHoliday.getCurrentRouteId())
                drawRouteOnMap(route, inactiveColor);
        }
    }

    private void drawRouteOnMap(Route route, int lineColor) {

//        List<RouteLocation> routeLocationList = currentHoliday.getCurrentRoute().getRouteLocationList();
        List<RouteLocation> routeLocationList = route.getRouteLocationList();

        PolylineOptions options = new PolylineOptions()
                .width(22)
//                .color(Color.GREEN);
//                .color(Color.argb(135, 0, 255, 0));
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
                if (route.getId() != currentHoliday.getCurrentRouteId())
                    map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Finish!")
                            .snippet(route.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_red)));
                ;
            }
        }


        //TODO implement some button to show/hide the events for each map
        if (currentHoliday.getCurrentRoute() != null) {
            eventMarkerMap = new HashMap<Marker, Long>();
            for (Event event : currentHoliday.getCurrentRoute().getEventList()) {
                LatLng pos = new LatLng(event.getRouteLocation().getLatitude(), event.getRouteLocation().getLongitude());
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(pos)
                        .title("Event")
                        .snippet(event.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_icon)));
                ;
                eventMarkerMap.put(marker, event.getId());
            }
        }
        map.addPolyline(options);

    }

    public void zoomMapToRoute(Route route) {
        LatLngBounds bounds = getRouteBoundaries(route);
//        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
    }

    public void zoomMapToCurrentHoliday() {
        LatLngBounds bounds = getCurrentHolidayBoundaries();
//        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
    }

    private void moveMapTo(LatLng latLng) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    }

    private LatLngBounds getRouteBoundaries(Route route) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        List<RouteLocation> locations = route.getRouteLocationList();
        for (RouteLocation location : locations) {
            builder.include(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        return builder.build();
    }

    private LatLngBounds getCurrentHolidayBoundaries() {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        List<Route> routes = currentHoliday.getRouteList();

        for (Route route : routes) {
            List<RouteLocation> locations = route.getRouteLocationList();

            for (RouteLocation location : locations) {
                builder.include(new LatLng(location.getLatitude(), location.getLongitude()));
            }

        }
        return builder.build();
    }

    public void createEvent() {

        //TODO: if user has not moved much from previous point, do not let him create new event, but edit previous instead
//        if(distanceToPreviousEvent < 20){
//            editEvent();
//        }


    }

    public void editEvent() {
        //TODO: called when user clicks on existing event (point on route)
    }

    //-------------------- Toolbar Methods -----------------------

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_add_attachment) {
            return true;
        } else if (id == R.id.menu_picture) {
            if (currentHoliday.getCurrentRoute() == null) {
                //TODO Raise error
                return false;
            }
            if (currentLoc != null) {
                cameraManager.startCameraForPicture(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()));
            }
        } else if (id == R.id.menu_video) {
            if (currentHoliday.getCurrentRoute() == null) {
                //TODO Raise error
                return false;
            }
            if (currentLoc != null) {
                cameraManager.startCameraForVideo(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()));
            }
        } else if (id == R.id.menu_description) {
            if (currentHoliday.getCurrentRoute() == null) {
                //TODO Raise error
                return false;
            }
            if (currentLoc != null) {
                Fragment descriptionFragment = new MediaDescriptionFragment();
                Bundle args = new Bundle();
                args.putLong(Constants.KEY_ROUTE_ID, currentHoliday.getCurrentRoute().getId());
                args.putLong(Constants.KEY_HOLIDAY_ID, currentHoliday.getId());
                args.putDouble(Constants.KEY_LAT, currentLoc.getLatitude());
                args.putDouble(Constants.KEY_LONG, currentLoc.getLongitude());
                descriptionFragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().add(R.id.activity_map_layout, descriptionFragment).commit();            }
        }
        return super.onOptionsItemSelected(item);
    }

    //-------------------- (GooglePlay) Location Client -----------------------

    @Override
    public void onLocationChanged(Location location) {

        currentLoc = location;
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

        if (currentHoliday != null) {
            Route route = currentHoliday.getCurrentRoute();
            if (route != null) {

                if (route.getRouteLocationList().size() == 0) {
                    DatabaseManager.addLocationToRoute(getApplicationContext(), route.getId(), loc);
//                    map.addMarker(new MarkerOptions().position(loc).title("Start!"));
                } else {
                    RouteLocation lastLocation = route.getRouteLocationList().get(route.getRouteLocationList().size() - 1);
                    Location lastLoc = new Location("dummyprovider");
                    lastLoc.setLatitude(lastLocation.getLatitude());
                    lastLoc.setLongitude(lastLocation.getLongitude());
                    double distance = location.distanceTo(lastLoc);

                    HelpingMethods.log("Distance: " + distance);
                    if (distance > Constants.MINIMUM_DISTANCE_BETWEEN_LOCATIONS) {

                        String msg = "Adding new location to route: " +
                                Double.toString(location.getLatitude()) + "," +
                                Double.toString(location.getLongitude());
                        HelpingMethods.log(msg);

                        DatabaseManager.addLocationToRoute(getApplicationContext(), route.getId(), loc);
                        drawRouteOnMap(currentHoliday.getCurrentRoute(), activeColor);
                        moveMapTo(loc);
                    }
                }

            }
        }

        //DatabaseManager.addLocationToRoute(getApplicationContext(), currentRouteId, loc);

//        Log.d(LOG_TAG, msg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        cameraManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (eventMarkerMap != null && eventMarkerMap.containsKey(marker)) {
            long eventId = eventMarkerMap.get(marker);
            Log.i("prose", "clicked on event");
            android.support.v4.app.Fragment eventFragment = new EventFragment();
            Bundle args = new Bundle();
            args.putLong(Constants.KEY_EVENT_ID, eventId);
            eventFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.activity_map_layout, eventFragment, Constants.TAG_EVENT_FRAGMENT).addToBackStack("").commit();
        }
        return false;
    }

    //-------------------- (GooglePlay) Services Client -----------------------

    @Override
    public void onConnected(Bundle bundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        // If already requested, start periodic updates
        mLocationClient.requestLocationUpdates(mLocationRequest, this);

        setUpMap();
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}


//    private void moveMapToCurrentLoc(){
//        //go to current position
//        LatLng latLngLast = getCurrentLatLng();
//        // Show the last Location of currentRoute in Google Map
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngLast, 10));
//        // Zoom in the Google Map
//        map.animateCamera(CameraUpdateFactory.zoomTo(17), 2500, null);
//    }
//
//    private LatLng getCurrentLatLng() {
//        // Enable MyLocation Layer of Google Map
//        map.setMyLocationEnabled(true);
//
//        // Get LocationManager object from System Service LOCATION_SERVICE
//        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        // Create a criteria object to retrieve provider
//        Criteria criteria = new Criteria();
//
//        // Get the name of the best provider
//        String provider = locationManager.getBestProvider(criteria, true);
//
//        // Get Current Location
//        Location myLocation = locationManager.getLastKnownLocation(provider);
//
//        //set map type
//        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//
//        // Get latitude of the current location
//        double latitude = myLocation.getLatitude();
//
//        // Get longitude of the current location
//        double longitude = myLocation.getLongitude();
//
//        // Create a LatLng object for the current location
//        LatLng latLng = new LatLng(latitude, longitude);
//
//        return latLng;
//    }


//            if (currentHoliday.getCurrentRoute() != null) {
//
//                List<RouteLocation> routeLocationList = currentHoliday.getCurrentRoute().getRouteLocationList();
//
//                if (routeLocationList == null || routeLocationList.size() < 1) { // IF route is empty
//                    return;
//                } else {
//                    //draw current route and go to last position on route
//
//                    HelpingMethods.log("routeLocationList.size() : " + routeLocationList.size());
//
//                    Double latitudeFirst = routeLocationList.get(0).getLatitude();
//                    Double longitudeFirst = routeLocationList.get(0).getLongitude();
////                    LatLng latLngFirst = new LatLng(latitudeFirst, longitudeFirst);
//                    // add marker for starting point
//                    map.addMarker(new MarkerOptions()
//                            .position(new LatLng(latitudeFirst, longitudeFirst))
//                            .snippet("This is the starting point of your route...")
//                            .title("Start"));
////                            .icon(BitmapDescriptorFactory
////                                .fromResource(R.drawable.ic_launcher)));
//
//                    Double latitudeLast = routeLocationList.get(routeLocationList.size() - 1).getLatitude();
//                    Double longitudeLast = routeLocationList.get(routeLocationList.size() - 1).getLongitude();
//                    LatLng latLngLast = new LatLng(latitudeLast, longitudeLast);
//
//                    drawRouteOnMap();
//                    moveMapTo(latLngLast);
//                }
//
//            } else {
//                //go to current position
//                Location currentLoc = mLocationClient.getLastLocation();
//                moveMapTo(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()));
//            }


//    private double[] getRouteBoundaries(Route route) {
//        double[] boundaries = null;
//
//        int mostEastLong = 0;
//        int mostWestLong = 1;
//        int mostNorthLat = 2;
//        int mostSouthLat = 3;
//
//        List<RouteLocation> locations = route.getRouteLocationList();
//        for (RouteLocation location : locations) {
//            double locationLat = location.getLatitude();
//            double locationLong = location.getLongitude();
//
//            if (boundaries == null) {
//                boundaries = new double[4];
//                boundaries[mostEastLong] = locationLong;
//                boundaries[mostWestLong] = locationLong;
//                boundaries[mostNorthLat] = locationLat;
//                boundaries[mostSouthLat] = locationLat;
//            } else {
//                if (locationLong > boundaries[mostEastLong])
//                    boundaries[mostEastLong] = locationLong;
//                else if (locationLong < boundaries[mostWestLong])
//                    boundaries[mostWestLong] = locationLong;
//
//                if (locationLat > boundaries[mostNorthLat])
//                    boundaries[mostNorthLat] = locationLat;
//                else if (locationLat < boundaries[mostSouthLat])
//                    boundaries[mostSouthLat] = locationLat;
//            }
//
//        }
//
//        return boundaries;
//    }

//PSEUDO

// initialize / set up map: show last point on route!
// currentHoliday.getRoute().getlastPoint()
// setUpMap(lastPoint) -> zooms to this point and print route for existing points

// OnLocationChange(): move camera to current position and show indicator for current poisition:
// if distance(lastPoint-currentPos)>10m: route.addPoint(currentPos)
// drawNewTrackOnMap(currentPos)
