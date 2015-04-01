package de.adamwest.activities_fragments.holiday_detail;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import de.adamwest.DatabaseManager;
import de.adamwest.R;
import de.adamwest.database.Event;
import de.adamwest.database.Route;
import de.adamwest.helper.Constants;
import de.adamwest.helper.HelpingMethods;
import de.adamwest.activities_fragments.holiday_detail.adapters.DetailsSlideViewPageAdapter;
import de.adamwest.activities_fragments.holiday_detail.adapters.MainRouteListAdapter;

import java.util.List;

/**
 * Created by Philip on 18.10.2014.
 */
public class HolidayDetailActivity extends FragmentActivity {

    private long holidayId;
    public ViewPager viewPager;
    private List<Route> routes;
    public long routeId;


    private List<Event> clusterEventList;

//------ Activity/Lifecycle Methods --------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        holidayId = getIntent().getLongExtra(Constants.KEY_HOLIDAY_ID, -1);
        routeId = -1;
        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_detail);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new DetailsSlideViewPageAdapter(getSupportFragmentManager()));
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
                routeId = selectedRoute.getId();
                viewPager.getAdapter().notifyDataSetChanged();
                updateActionBar();
                toggleRouteListDropdown();
            }
        });
    }


//------ ActionBar Methods -----------------------------------------------------------------------------------------

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

//------ OnClick Methods -------------------------------------------------------------------------------------------

    public void onHolidayListItemClick(View view) {
        routeId = -1;
        viewPager.getAdapter().notifyDataSetChanged();
        updateActionBar();
        toggleRouteListDropdown();
    }

    public void onTitleBarClick(View view) {
//        Toast.makeText(this, "select holiday/route", Toast.LENGTH_SHORT).show();
        toggleRouteListDropdown();
    }

    public void onAddPhoto(View view) {
        HelpingMethods.toast(this, "onAddPhoto clicked.");
    }

    public void onAddVideo(View view) {
        HelpingMethods.toast(this, "onAddVideo clicked.");
    }

    public void onAddText(View view) {
        HelpingMethods.toast(this, "onAddText clicked.");
    }

    public void onTestButtonClick(View view) {
        final ActionBar actionBar = getActionBar();
//        if (actionBar.isShowing())
//            actionBar.hide();
//        else
//            actionBar.show();
//        Toast.makeText(this, "showing/hiding Action Bar", Toast.LENGTH_SHORT).show();
    }

//------ Other Methods ---------------------------------------------------------------------------------------------

    public long getHolidayId() {
        return holidayId;
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
        if (routeId == -1) {
            routeNameInTitleBar.setText("All Routes");
        } else {
            routeNameInTitleBar.setText(DatabaseManager.getRouteFromId(this, routeId).getName());
        }
    }

    public List<Event> getClusterEventList() {
        return clusterEventList;
    }

    public void setClusterEventList(List<Event> clusterEventList) {
        this.clusterEventList = clusterEventList;
    }

}
