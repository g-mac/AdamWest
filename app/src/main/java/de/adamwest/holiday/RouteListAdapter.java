package de.adamwest.holiday;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.adamwest.R;
import de.adamwest.database.Route;
import de.adamwest.helper.Constants;

import java.util.List;

/**
 * Created by philip on 21/09/14.
 */
public class RouteListAdapter extends BaseAdapter {


/**
 * Created by philip on 17/09/14.
 */
    private List<Route> routes;
    private Context context;
    private long currentHolidayId;

    public RouteListAdapter(List<Route> routes, Context context, long currentHolidayId) {
        this.routes = routes;
        this.context = context;
        this.currentHolidayId = currentHolidayId;
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

        if(0 == routes.size() || position == routes.size()) {
            View view = inflater.inflate(R.layout.list_item_new_holiday, parent, false);
            ((TextView)view.findViewById(R.id.text_view_new_holiday_item)).setText(context.getString(R.string.create_new_route));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment createNewRouteFragment = new CreateNewRouteFragment();
                    Bundle args = new Bundle();
                    args.putLong(Constants.KEY_ROUTE_ID, currentHolidayId);
                    createNewRouteFragment.setArguments(args);
                    ((MapActivity)context).getFragmentManager().beginTransaction().add(R.id.activity_map_layout, createNewRouteFragment).commit();
                }
            });
            return view;
        }
        else {
            View view = inflater.inflate(R.layout.list_item_holiday, parent, false);
            Route route = routes.get(position);
            ((TextView)view.findViewById(R.id.text_view_holiday_name)).setText(route.getName());
            return view;
        }

    }
}
