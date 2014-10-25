package de.adamwest.holiday.holiday_detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.database.Route;

import java.util.List;

/**
 * Created by Philip on 18.10.2014.
 */
public class RouteListFragment extends Fragment {

    private ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_route_list, container, false);
        listView = (ListView)view.findViewById(R.id.list_view_route_list);
        List<Route> routes = DatabaseManager.getHolidayFromId(getActivity(),((HolidayDetailActivity)getActivity()).getHolidayId()).getRouteList();
        listView.setAdapter(new RouteListAdapter(routes, getActivity()));

        return view;
    }
}
