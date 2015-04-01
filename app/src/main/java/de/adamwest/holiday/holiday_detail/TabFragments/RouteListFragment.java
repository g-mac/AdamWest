package de.adamwest.holiday.holiday_detail.TabFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import de.adamwest.R;
import de.adamwest.DatabaseManager;
import de.adamwest.database.Route;
import de.adamwest.holiday.holiday_detail.HolidayDetailActivity;
import de.adamwest.holiday.holiday_detail.RouteListAdapter;

import java.util.List;

/**
 * Created by Philip on 18.10.2014.
 */
public class RouteListFragment extends Fragment {

    private ListView listView;
    private List<Route> routes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_route_list, container, false);
        listView = (ListView)view.findViewById(R.id.list_view_route_list);
        routes = DatabaseManager.getHolidayFromId(getActivity(),((HolidayDetailActivity)getActivity()).getHolidayId()).getRouteList();
        listView.setAdapter(new RouteListAdapter(routes, getActivity()));
        registerForContextMenu(listView);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_route_list_floating_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
       Route route = (Route)listView.getAdapter().getItem(info.position);
        switch (item.getItemId()) {
            case R.id.menu_delete_route:
                confirmDeletion(route.getId());
                return true;
            case R.id.menu_edit_route:
                return true;
            case R.id.menu_show_attachments:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void confirmDeletion(final long routeId) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.confirm_route_delete_text))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseManager.deleteRoute(getActivity(),routeId);
                        routes = DatabaseManager.getHolidayFromId(getActivity(),((HolidayDetailActivity)getActivity()).getHolidayId()).getRouteList();
                        listView.setAdapter(new RouteListAdapter(routes, getActivity()));

                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
