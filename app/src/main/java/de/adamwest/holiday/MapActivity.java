package de.adamwest.holiday;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.database.Holiday;
import de.adamwest.database.Route;
import de.adamwest.database.RouteLocation;
import de.adamwest.helper.Constants;
import de.adamwest.helper.HelpingMethods;

import java.util.List;


public class MapActivity extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    private long currentHolidayId = -1;
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    static final int activeColor = Color.argb(125, 0, 255, 0); // -transparent green
    static final int inactiveColor = Color.argb(155, 255, 255, 255); // -transparent gray
    private GoogleMap map;
    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;
    private Holiday currentHoliday;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

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

    //----------------------- Main Methods ------------------------------------

    private void setUpMap() {
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
                    //draw the routes: (all in different colors)
                    //1) draw all unselected routes (super transparent)
                    //2) draw selected route (zoomed to, and less transparent or not at all transp.)
                }
//                private void drawRoutes(int selectedId){
//
//                }
            }


        }
    }

    private void drawActiveRoute() {
        drawRouteOnMap(currentHoliday.getCurrentRoute(), activeColor);
    }

    private void drawInactiveRoutes() {
        List<Route> routeList = currentHoliday.getRouteList();
        for (Route route : routeList) {
            if (route.getId() != currentHoliday.getCurrentRouteId())
                drawRouteOnMap(route, inactiveColor);
        }
    }

    //    private void drawRouteOnMap(List<RouteLocation> routeLocationList) {
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
                map.addMarker(new MarkerOptions().position(latLng).title("Start!"));
            } //todo: finish marker
        }

        map.addPolyline(options);

    }

    private void moveMapTo(LatLng latLng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        map.animateCamera(CameraUpdateFactory.zoomTo(17), 2500, null);
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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
        }
        return super.onOptionsItemSelected(item);
    }

    //-------------------- (GooglePlay) Location Client -----------------------

    @Override
    public void onLocationChanged(Location location) {

        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

        if (currentHoliday != null) {
            Route route = currentHoliday.getCurrentRoute();
            if (route != null) {

                if (route.getRouteLocationList().size() == 0) {
                    DatabaseManager.addLocationToRoute(getApplicationContext(), route.getId(), loc);
                    map.addMarker(new MarkerOptions().position(loc).title("Start!"));
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
                    }
                }

            }
        }

        //DatabaseManager.addLocationToRoute(getApplicationContext(), currentRouteId, loc);

//        Log.d(LOG_TAG, msg);
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


//PSEUDO

// initialize / set up map: show last point on route!
// currentHoliday.getRoute().getlastPoint()
// setUpMap(lastPoint) -> zooms to this point and print route for existing points

// OnLocationChange(): move camera to current position and show indicator for current poisition:
// if distance(lastPoint-currentPos)>10m: route.addPoint(currentPos)
// drawNewTrackOnMap(currentPos)
