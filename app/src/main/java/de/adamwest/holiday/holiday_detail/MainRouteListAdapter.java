package de.adamwest.holiday.holiday_detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.adamwest.R;
import de.adamwest.database.Route;
import de.adamwest.helper.Constants;
import de.adamwest.helper.HelpingMethods;
import de.adamwest.helper.HolidayStatsCalculator;

import java.util.Date;
import java.util.List;

/**
 * Created by Philip on 25.10.2014.
 */
public class MainRouteListAdapter extends BaseAdapter {

    private List<Route> routes;
    private Context context;

    public MainRouteListAdapter(List<Route> routes, Context context) {
        this.routes = routes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return routes.size();
    }

    @Override
    public Object getItem(int position) {
        return routes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return routes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Route route = routes.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_route_main, parent, false);

        String startDate = HelpingMethods.convertDateToFormattedString(route.getCreatedAt()) + " - " + HelpingMethods.convertDateToFormatedTime(route.getCreatedAt());
        String finishedDate;
        if(route.getRouteLocationList().size() > 0) {
            Date lastDate = route.getRouteLocationList().get(route.getRouteLocationList().size() -1).getCreatedAt();
            finishedDate = HelpingMethods.convertDateToFormattedString(lastDate) + " - " + HelpingMethods.convertDateToFormatedTime(lastDate);

        }
        else {
            finishedDate = startDate;
        }
        ((TextView) view.findViewById(R.id.list_item_date)).setText(startDate);
//        ((TextView) view.findViewById(R.id.text_view_finished_date)).setText(finishedDate);

        ((TextView) view.findViewById(R.id.list_item_name)).setText(route.getName());
        ((TextView)view.findViewById(R.id.list_item_description)).setText(route.getDescription());
//        ((TextView)view.findViewById(R.id.text_view_distance_counter)).setText(HolidayStatsCalculator.getDistanceForRoute(route) + (context).getString(R.string.distance_scale_unit));

//        ((TextView)view.findViewById(R.id.list_item_no_pictures)).setText(HolidayStatsCalculator.getAmountOfEventsForRoute(route, Constants.TYPE_IMAGE));
//        ((TextView)view.findViewById(R.id.list_item_no_videos)).setText(HolidayStatsCalculator.getAmountOfEventsForRoute(route, Constants.TYPE_VIDEO));
//        ((TextView)view.findViewById(R.id.list_item_no_texts)).setText(HolidayStatsCalculator.getAmountOfEventsForRoute(route, Constants.TYPE_TEXT));



        return view;
    }
}
