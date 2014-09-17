package de.adamwest.routelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import de.adamwest.R;
import de.adamwest.database.Route;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by philip on 17/09/14.
 */
public class RouteListAdapter extends BaseAdapter {
    private ArrayList<Route> routes;
    private Context context;

    public RouteListAdapter(ArrayList<Route> routes, Context context) {
        this.routes = routes;
        this.context = context;
    }
    @Override
    public int getCount() {
        return routes.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if(position <= routes.size()) {
            return routes.get(position);
        }
        else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(position == routes.size()) {
            //TODO NEW ROUTE
            return inflater.inflate(R.layout.list_item_new_route, parent, false);
        }
        else {
            return inflater.inflate(R.layout.list_item_route, parent, false);
        }

    }
}
