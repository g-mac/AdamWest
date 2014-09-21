package de.adamwest.holidaylist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.database.Holiday;
import de.adamwest.database.Route;
import de.adamwest.database.RouteLocation;
import de.adamwest.helper.Constants;
import de.adamwest.holiday.MapActivity;

import java.util.List;


public class HolidayListActivity extends Activity {

    private ListView holidayListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_route_list);
        holidayListView = (ListView)findViewById(R.id.listview_route_list);

        final List<Holiday> holidayList = DatabaseManager.getAllHoliday(getApplicationContext());
        holidayListView.setAdapter(new HolidayListAdapter(holidayList, this));
        holidayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == holidayList.size()) {
                    Log.i("abc", "clicked: " + position);
                    Fragment createNewRouteFragment = new CreateNewHolidayFragment();
                    getFragmentManager().beginTransaction().add(R.id.main_layout, createNewRouteFragment).commit();
                }
                else {
                    long holidayId = ((Holiday)holidayListView.getAdapter().getItem(position)).getId();
                    if(-1 == holidayId) {
                        //creation of new route failed
                        //TODO error msg
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                        intent.putExtra(Constants.KEY_HOLIDAY_ID, holidayId);
                        startActivity(intent);
                    }
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dbTest() {
        List<Route> routes = DatabaseManager.getAllRoutes(getApplicationContext());
        for(Route route : routes) {
            List<RouteLocation> routeLocations = route.getRouteLocationList();
            for(RouteLocation routeLocation : routeLocations) {
                Log.i("Loc", "Locations Id: " + routeLocation.getId().toString());
                Log.i("Loc", "Date Id: " + routeLocation.getCreatedAt().toString());
                Log.i("Loc", "Lat: " + routeLocation.getLatitude().toString());
                Log.i("Loc", "Lon: " + routeLocation.getLongitude().toString());
                Log.i("Loc", "#############################################");


            }
        }
    }
}
