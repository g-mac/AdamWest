package de.adamwest.holiday.holiday_detail.TabFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.database.Event;
import de.adamwest.database.Holiday;
import de.adamwest.database.Route;
import de.adamwest.holiday.holiday_detail.EventGridAdapter;
import de.adamwest.holiday.holiday_detail.HolidayDetailActivity;
import de.adamwest.holidaylist.HolidayListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philip on 09.11.2014.
 */
public class EventGridFragment extends Fragment {

    GridView gridview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_grid, container, false);

        gridview = (GridView) view.findViewById(R.id.gridview);
        HolidayDetailActivity holidayDetailActivity = ((HolidayDetailActivity)getActivity());
        //TODO List needs to be auto gen
        Holiday holiday = DatabaseManager.getHolidayFromId(getActivity(), ((HolidayDetailActivity)getActivity()).getHolidayId());
        List<Event> eventList = new ArrayList<Event>();
        if(holidayDetailActivity.routeId != -1) {
            for(Route route : holiday.getRouteList()) {
                if(route.getId() == holidayDetailActivity.routeId) {
                    eventList = route.getEventList();
                    break;
                }
            }
        }
        else {
            for(Route route : holiday.getRouteList()) {
                eventList.addAll(route.getEventList());
            }
        }

        if (eventList != null && !eventList.isEmpty())
        gridview.setAdapter(new EventGridAdapter(getActivity(), eventList));
        return view;
    }
}
