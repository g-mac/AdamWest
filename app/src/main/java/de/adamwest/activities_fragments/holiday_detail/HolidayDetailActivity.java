package de.adamwest.activities_fragments.holiday_detail;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import de.adamwest.DatabaseManager;
import de.adamwest.R;
import de.adamwest.activities_fragments.holiday_detail.adapters.DetailsSlideViewPageAdapter;
import de.adamwest.activities_fragments.holiday_detail.adapters.MainRouteListAdapter;
import de.adamwest.activities_fragments.holiday_detail.fragments.map.MapFragment;
import de.adamwest.database.Event;
import de.adamwest.database.Holiday;
import de.adamwest.database.Route;
import de.adamwest.database.RouteLocation;
import de.adamwest.helper.CameraManager;
import de.adamwest.helper.Constants;
import de.adamwest.helper.HelpingMethods;

import java.util.List;

//import android.app.Fragment;

/**
 * Created by Philip on 18.10.2014.
 */
public class HolidayDetailActivity extends FragmentActivity implements LocationListener {

    private long holidayId;
    public ViewPager viewPager;
    private List<Route> routes;
    public long selectedRouteId;
    public long trackedRouteId;
    private CameraManager cameraManager;

    private LocationManager locationManager;
    private Location currentLoc;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL = 15000;
    private static final float MINIMUM_DISTANCE_IN_METER = 10;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL = 1000;

    private List<Event> clusterEventList;

    //------ Activity/Lifecycle Methods --------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        holidayId = getIntent().getLongExtra(Constants.KEY_HOLIDAY_ID, -1);
        selectedRouteId = -1;
        trackedRouteId = -1;
        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_detail);

        cameraManager = CameraManager.getCameraManager( this);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new DetailsSlideViewPageAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(2); //todo: check performance?!
        viewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });
        createTabBar();

        TextView holidayNameInTitleBar = (TextView) findViewById(R.id.action_bar_custom_title);
        String holidayName = DatabaseManager.getHolidayFromId(this, holidayId).getName();
        holidayNameInTitleBar.setText(holidayName);

        ListView listView = (ListView) findViewById(R.id.main_route_list);
        routes = DatabaseManager.getHolidayFromId(this, holidayId).getRouteList();
        listView.setAdapter(new MainRouteListAdapter(routes, this));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),selectedRoute.getName(),Toast.LENGTH_SHORT).show();
                Route selectedRoute = (Route) adapter.getItemAtPosition(position);
                selectedRouteId = selectedRoute.getId();
                viewPager.getAdapter().notifyDataSetChanged();
                updateActionBar();
                toggleRouteListDropdown();
            }
        });

        // ----------

        trackedRouteId = DatabaseManager.getHolidayFromId(this, holidayId).getCurrentRouteId();

        if (trackedRouteId >= 0)
            startTracking();


        // ----------


        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }


    //------ ActionBar Methods -----------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createTabBar() {
        final ActionBar actionBar = getActionBar();
        actionBar.setIcon(R.drawable.ic_overview_bright);

        // Create and show custom view in Action Bar
        routes = DatabaseManager.getHolidayFromId(this, holidayId).getRouteList();
        actionBar.setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.actionbar_custom_title, null);
        actionBar.setCustomView(view);
//        Spinner spinner = (Spinner) findViewById(R.id.actionbar_spinner);
//        spinner.setAdapter(new RouteListAdapter(routes, this));

//        // Temporary todo: delete or replace
//        actionBar.setTitle("Paris City Tour");
//        actionBar.setSubtitle("16th District");

        // Home Button settings
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };

        // Add 3 tabs, specifying the tab's text and TabListener
        ActionBar.Tab detailTab = actionBar.newTab().setText(getString(R.string.tab_detail))
                .setTabListener(tabListener);
//        ActionBar.Tab routesTab = actionBar.newTab().setText(getString(R.string.tab_routes))
//                .setTabListener(tabListener);
        ActionBar.Tab eventsTab = actionBar.newTab().setText(getString(R.string.tab_events))
                .setTabListener(tabListener);
        ActionBar.Tab mapTab = actionBar.newTab().setText(getString(R.string.tab_map))
                .setTabListener(tabListener);
        actionBar.addTab(detailTab);
        actionBar.addTab(eventsTab);
        actionBar.addTab(mapTab);
//        actionBar.addTab(routesTab);

    }

    private void toggleRouteListDropdown() {

        final ActionBar actionBar = getActionBar();

        View routeListView = findViewById(R.id.main_route_list_view);
        //todo: line might not be needed
        View viewPager = findViewById(R.id.pager);

//        actionBar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS
        if (routeListView.getVisibility() != View.VISIBLE) {
            getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            routeListView.setVisibility(View.VISIBLE);
            //todo: line might not be needed
            viewPager.setVisibility(View.GONE);

        } else {
            getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            routeListView.setVisibility(View.GONE);
            //todo: line might not be needed
            viewPager.setVisibility(View.VISIBLE);
        }
    }

    private void updateActionBar() {
        TextView routeNameInTitleBar = (TextView) findViewById(R.id.action_bar_custom_subtitle);
        if (selectedRouteId == -1) {
            routeNameInTitleBar.setText("All Routes");
        } else {
            routeNameInTitleBar.setText(DatabaseManager.getRouteFromId(this, selectedRouteId).getName());
        }
    }

    //------ OnClick Methods -------------------------------------------------------------------------------------------

    public void onHolidayListItemClick(View view) {
        selectedRouteId = -1;
        viewPager.getAdapter().notifyDataSetChanged();
        updateActionBar();
        toggleRouteListDropdown();
    }

    public void onTitleBarClick(View view) {
//        Toast.makeText(this, "select holiday/route", Toast.LENGTH_SHORT).show();
        toggleRouteListDropdown();
    }

    public void onAddPhotoClick(View view) {
        HelpingMethods.toast(this, "onAddPhotoClick clicked.");
        if (currentLoc != null)
            cameraManager.startCameraForPicture(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()));
        else
            HelpingMethods.log("currentLoc == null");
    }

    public void onAddVideoClick(View view) {
        HelpingMethods.toast(this, "onAddVideoClick clicked.");
    }

    public void onAddTextClick(View view) {
        HelpingMethods.toast(this, "onAddTextClick clicked.");
    }

    public void onStartTrackingClick(View view) {
//        HelpingMethods.toast(this, "onStartTracking-Click clicked.");
        startTracking();
//        onTestButtonClick(view);
    }

    public void onStopTrackingClick(View view) {
//        HelpingMethods.toast(this, "onStopTrackingClick clicked.");
        stopTracking();
    }

    public void onAddRouteClick(View view) {
//        HelpingMethods.toast(this, "onAddRouteClick clicked.");
        promptNewRoute();

//        Fragment newRouteFragment = new NewRouteFragment();
////        getFragmentManager().beginTransaction().add(R.id.main_layout, newRouteFragment).commit();
//        getFragmentManager().beginTransaction().add(android.R.id.content, newRouteFragment).commit();
    }

    public void onToggleMapClick(View view) {
        MapFragment mapFragment = getMapFragment();
        if (mapFragment != null)
            mapFragment.toggleMap();
    }

    public void onTestButtonClick(View view) {

//        final ActionBar actionBar = getActionBar();
//        if (actionBar.isShowing())
//            actionBar.hide();
//        else
//            actionBar.show();
//        Toast.makeText(this, "showing/hiding Action Bar", Toast.LENGTH_SHORT).show();
    }

    //------ Tracking/Location Methods ---------------------------------------------------------------------------------

    public void startTracking() {

        if (selectedRouteId == -1) {
            HelpingMethods.toast(this, "select or create route first");
            return;
        }

        trackedRouteId = selectedRouteId;
        DatabaseManager.getHolidayFromId(this, holidayId).setCurrentRouteId(trackedRouteId);
        HelpingMethods.toast(this, "tracking route: '" + DatabaseManager.getRouteFromId(this, trackedRouteId).getName() + "'");
        findViewById(R.id.holiday_no_tracking_menu).setVisibility(View.INVISIBLE);
        findViewById(R.id.holiday_tracking_menu).setVisibility(View.VISIBLE);

        //start actual tracking
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, new Float(Constants.MINIMUM_DISTANCE_BETWEEN_LOCATIONS), this);

        //inform mapfragment to redraw ?????
//        getMapFragment().setUpMap();
        MapFragment mapFragment = getMapFragment();
        if (mapFragment != null)
            mapFragment.setUpMap();
    }

    public void stopTracking() {

        if (trackedRouteId == -1)
            return;

        HelpingMethods.toast(this, "stopped tracking for route: '" + DatabaseManager.getRouteFromId(this, trackedRouteId).getName() + "'");
        DatabaseManager.removeActiveRouteForHoliday(this, holidayId);
        trackedRouteId = -1;

        findViewById(R.id.holiday_no_tracking_menu).setVisibility(View.VISIBLE);
        findViewById(R.id.holiday_tracking_menu).setVisibility(View.INVISIBLE);

        //actually stop tracking
        locationManager.removeUpdates(this);

        //inform mapfragment to redraw ?????
//        getMapFragment().setUpMap();
        MapFragment mapFragment = getMapFragment();
        if (mapFragment != null)
            mapFragment.setUpMap();
    }

    public Location getCurrentLocation() {
        Location currentLocation = currentLoc;
        if (currentLoc == null)
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        return currentLoc;
        return currentLocation;
    }

    @Override
    public void onLocationChanged(Location location) {

        HelpingMethods.log("LOCATION: " + location);
        currentLoc = location;

        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

        Holiday currentHoliday = DatabaseManager.getHolidayFromId(this, holidayId);

        Route trackedRoute = currentHoliday.getRoute();

        if (trackedRoute != null) {

            if (trackedRoute.getRouteLocationList().size() == 0) {
                DatabaseManager.addLocationToRoute(getApplicationContext(), trackedRoute.getId(), loc);
//                    map.addMarker(new MarkerOptions().position(loc).title("Start!"));
            } else {
                RouteLocation lastLocation = trackedRoute.getRouteLocationList().get(trackedRoute.getRouteLocationList().size() - 1);
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

                    DatabaseManager.addLocationToRoute(getApplicationContext(), trackedRoute.getId(), loc);

                    //todo: implement a "follow" button, so that tracking only follows current location if user chooses so.

                    getMapFragment().onLocationAdded(loc); //only if fragment is visible?

//                    (MapFragment) mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map)
//                    drawRouteOnMap(currentHoliday.getRoute(), activeColor);
//                    moveMapTo(loc);
                }
            }

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //------ Other Methods ---------------------------------------------------------------------------------------------

    public void promptNewRoute() {
//        LayoutInflater li = LayoutInflater.from(this);
//        View promptsView = li.inflate(R.layout.prompt_create_new_route, null);

//        Dialog dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.prompt_create_new_route);

        final TextView name = (TextView) dialog.findViewById(R.id.prompt_new_route_name);
        final TextView description = (TextView) dialog.findViewById(R.id.prompt_new_route_description);

        dialog.findViewById(R.id.prompt_new_route_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.findViewById(R.id.prompt_new_route_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRoute(name.getText() + "", description.getText() + "");
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public long addNewRoute(String name, String description) {
        HelpingMethods.toast(this, "Creating new Route: " + name);
        long newRouteId = -1;
        newRouteId = DatabaseManager.createNewRoute(this, holidayId, name, description);

        if (newRouteId != -1) {
            selectedRouteId = newRouteId;
            viewPager.getAdapter().notifyDataSetChanged();
            updateActionBar();
        }

        return newRouteId;
    }

    public long getHolidayId() {
        return holidayId;
    }

    //------ Map -------------------------------------------------------------------------------------------------------

    private MapFragment getMapFragment() {

        MapFragment mapFragment = null;

        try {
            Fragment fragment = ((DetailsSlideViewPageAdapter) viewPager.getAdapter()).getFragment(2);
            mapFragment = (MapFragment) fragment;
//            HelpingMethods.log("succesfully found MapFragment.");
        } catch (Exception e) {
            HelpingMethods.log("Error getting MapFragment: " + e.getMessage());
            e.printStackTrace();
        }

        return mapFragment;

//        //todo: this solution might be a little dirty.
//        MapFragment mapFragment = null;
////        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.pager);
//        try {
//            Fragment fragment = getSupportFragmentManager().getFragments().get(2);
//            mapFragment = (MapFragment) fragment;
//            HelpingMethods.log("succesfully found MapFragment.");
//        } catch (Exception e) {
//            HelpingMethods.log("Error getting MapFragment: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return mapFragment;
    }

    public List<Event> getClusterEventList() {
        return clusterEventList;
    }

    public void setClusterEventList(List<Event> clusterEventList) {
        this.clusterEventList = clusterEventList;
    }

    //

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        cameraManager.onActivityResult(requestCode, resultCode, data);
    }
}
