package de.adamwest.routelist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import de.adamwest.MapActivity;
import de.adamwest.R;
import de.adamwest.database.Route;
import de.adamwest.database.TestDataCreator;

import java.util.ArrayList;
import java.util.List;


public class RouteListActivity extends Activity {

    private ListView routeListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_route_list);
        routeListView = (ListView)findViewById(R.id.listview_route_list);

        TestDataCreator.setupDatabase(this);
        List<Route> routes = TestDataCreator.getAllRoutes(this);
        //TestDataCreator.createNewRoutes(this);


        final ArrayList<Route> testArr = new ArrayList<Route>();
        testArr.add(new Route());
        testArr.add(new Route());
        testArr.add(new Route());
        testArr.add(new Route());

        routeListView.setAdapter(new RouteListAdapter(testArr, this));
        routeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("abc", "clicked: " + position);
                if(position==testArr.size()){
                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
//                    String message = "empty message";
//                    intent.putExtra("message", message);
                    startActivity(intent);
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
}
