package de.adamwest.holiday.holiday_detail.TabFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.GridView;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.database.Event;
import de.adamwest.database.Holiday;
import de.adamwest.database.Route;
import de.adamwest.helper.HelpingMethods;
import de.adamwest.holiday.holiday_detail.EventGridAdapter;
import de.adamwest.holiday.holiday_detail.HolidayDetailActivity;
import de.adamwest.holiday.holiday_detail.RouteListAdapter;
import de.adamwest.holidaylist.HolidayListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philip on 09.11.2014.
 */
public class EventGridFragment extends Fragment {

    GridView gridview;
    Holiday holiday;
    List<Event> eventList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_grid, container, false);
        HelpingMethods.log("init event grid");
        gridview = (GridView) view.findViewById(R.id.gridview);
        //TODO List needs to be auto gen
        holiday = DatabaseManager.getHolidayFromId(getActivity(), ((HolidayDetailActivity) getActivity()).getHolidayId());
        initGridView();
        registerForContextMenu(gridview);

        return view;
    }

    private void initGridView() {
        HolidayDetailActivity holidayDetailActivity = ((HolidayDetailActivity) getActivity());
        eventList = new ArrayList<Event>();
        if (holidayDetailActivity.routeId != -1) {
            for (Route route : holiday.getRouteList()) {
                if (route.getId() == holidayDetailActivity.routeId) {
                    eventList = route.getEventList();
                    break;
                }
            }
        } else {
            for (Route route : holiday.getRouteList()) {
                eventList.addAll(route.getEventList());
            }
        }

        if (eventList != null && !eventList.isEmpty())
            gridview.setAdapter(new EventGridAdapter(getActivity(), eventList));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_event_floating_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Event event = (Event) gridview.getAdapter().getItem(info.position);
        switch (item.getItemId()) {
            case R.id.menu_delete_event:
                confirmDeletion(event.getId());
                return true;
            case R.id.menu_edit_event:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void confirmDeletion(final long eventId) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.confirm_event_delete_text))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseManager.deleteEvent(getActivity(), eventId);
                        initGridView();

                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void updateWithSpecificEvents(List<Event> eventList) {
        //gridview.setAdapter(new EventGridAdapter(getActivity(), eventList));
    }

}
