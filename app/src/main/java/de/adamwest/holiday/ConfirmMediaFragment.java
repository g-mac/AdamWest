package de.adamwest.holiday;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.android.gms.maps.model.LatLng;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.helper.CameraManager;
import de.adamwest.helper.Constants;

/**
 * Created by philip on 26/09/14.
 */
public class ConfirmMediaFragment extends Fragment {

    private long holidayId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_media, container, false);
        holidayId = getArguments().getLong(Constants.KEY_HOLIDAY_ID);
        view.findViewById(R.id.button_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long routeId = DatabaseManager.getHolidayFromId(getActivity(), holidayId).getCurrentRoute().getId();
                String type = getArguments().getString(Constants.KEY_CAMERA_TYPE);
                LatLng loc = getArguments().getParcelable(Constants.KEY_LAT_LNG);
                DatabaseManager.createNewEventWithMultiMediaElement(getActivity(), routeId, type, CameraManager.currentFile.getAbsolutePath(), loc);
            }
        });

        view.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ImageView imagepreview = (ImageView)view.findViewById(R.id.imageview_preview);
        imagepreview.setImageURI(Uri.parse(CameraManager.currentFile.getAbsolutePath()));

        return view;
    }
}
