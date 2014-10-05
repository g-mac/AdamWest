package de.adamwest.holiday;

import android.support.v4.app.Fragment;
import android.view.View;
import com.google.android.gms.maps.model.LatLng;
import de.adamwest.database.DatabaseManager;
import de.adamwest.helper.CameraManager;
import de.adamwest.helper.Constants;

/**
 * Created by philip on 01/10/14.
 */
public class MediaFragmentParent extends Fragment {

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

    protected void storeMediaElementInDatabase(String type, String path, String description, long holidayId) {
        long routeId = DatabaseManager.getHolidayFromId(getActivity(), holidayId).getCurrentRoute().getId();
        long newCreationId = -1;
        if(getArguments().containsKey(Constants.KEY_EVENT_ID)) {
            long eventId = getArguments().getLong(Constants.KEY_EVENT_ID);
            newCreationId = DatabaseManager.createNewMultiMediaElement(getActivity(), type, path, eventId, description);
        }
        else {
            LatLng loc = new LatLng(getArguments().getDouble(Constants.KEY_LAT),
                    getArguments().getDouble(Constants.KEY_LONG));

            newCreationId = DatabaseManager.createNewEventWithMultiMediaElement(getActivity(), routeId, type, path, loc, description);
        }


        if(-1 != newCreationId) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(MediaFragmentParent.this).commit();
        }
        else{
            //TODO ERROR WHILE CREATING
        }
    }
}
