package de.adamwest.holiday.holiday_edit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import com.google.android.gms.maps.model.LatLng;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.database.Event;
import de.adamwest.database.Route;
import de.adamwest.helper.CameraManager;
import de.adamwest.helper.Constants;

/**
 * Created by philip on 01/10/14.
 */
public class MediaFragmentParent extends Fragment {
    protected EditText eventNameEditText;
    protected long holidayId;

    protected View.OnClickListener cancelButtonOnClickListener(final String mediaType) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaType.equals(Constants.TYPE_TEXT)) {
                    CameraManager.currentFile.delete();
                }
                getActivity().getSupportFragmentManager().beginTransaction().remove(MediaFragmentParent.this).commit();
            }
        };
    }

    protected void storeMediaElementInDatabase(String type, String path, String description, long holidayId, String eventName) {
        long routeId = DatabaseManager.getHolidayFromId(getActivity(), holidayId).getCurrentRoute().getId();
        long newCreationId = -1;


        if(getArguments().containsKey(Constants.KEY_EVENT_ID)) {
            long eventId = getArguments().getLong(Constants.KEY_EVENT_ID);
            newCreationId = DatabaseManager.createNewMultiMediaElement(getActivity(), type, path, eventId, description);
        }
        else {
            LatLng loc = new LatLng(getArguments().getDouble(Constants.KEY_LAT),
                    getArguments().getDouble(Constants.KEY_LONG));

            newCreationId = DatabaseManager.createNewEventWithMultiMediaElement(getActivity(), routeId, type, path, loc, description, eventName);
        }

        if(-1 != newCreationId) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(MediaFragmentParent.this).commit();
        }
        else{
            //TODO ERROR WHILE CREATING
        }
    }

    protected void checkNearbyEvents(long holidayId) {
        Route route = DatabaseManager.getHolidayFromId(getActivity(), holidayId).getCurrentRoute();
        if(getArguments().containsKey(Constants.KEY_EVENT_ID) || route == null) return;
        Location currentLoc = new Location("dummyprovider");

        currentLoc.setLatitude(getArguments().getDouble(Constants.KEY_LAT));
        currentLoc.setLongitude(getArguments().getDouble(Constants.KEY_LONG));

        for(Event event : route.getEventList()) {
            Location eventLocation = new Location("eventprovider");
            eventLocation.setLatitude(event.getRouteLocation().getLatitude());
            eventLocation.setLongitude(event.getRouteLocation().getLongitude());

            if(currentLoc.distanceTo(eventLocation) < Constants.MAXIMAL_MERGING_DISTANCE_BETWEEN_EVENTS) {
                buildAlertDialog(event);
                break;
            }
        }
    }

    private void buildAlertDialog(final Event event) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.event_nearby_title))
                .setMessage(getEventNearbyString(event))
                .setPositiveButton(getString(R.string.old_event), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        getArguments().putLong(Constants.KEY_EVENT_ID, event.getId());
                        eventNameEditText.setVisibility(View.GONE);
                    }
                })
                .setNegativeButton(getString(R.string.new_event), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        eventNameEditText.setVisibility(View.VISIBLE);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private String getEventNearbyString(Event event) {
        String eventString;
        eventString = getString(R.string.event_nearby_text_1);
        eventString += " (" + ((event.getName().equals("")) ? getString(R.string.unnamed): event.getName()) + ")";
        eventString+= getString(R.string.event_nearby_text_2);

        return eventString;
    }

    protected void initFragment(View view) {
        eventNameEditText = (EditText)view.findViewById(R.id.edit_text_event_name);
        if(getArguments().containsKey(Constants.KEY_EVENT_ID)) {
            eventNameEditText.setVisibility(View.GONE);
        }
        else {
            holidayId = getArguments().getLong(Constants.KEY_HOLIDAY_ID);
        }
        checkNearbyEvents(holidayId);
        view.findViewById(R.id.button_cancel).setOnClickListener(cancelButtonOnClickListener(Constants.TYPE_TEXT));

    }
}
