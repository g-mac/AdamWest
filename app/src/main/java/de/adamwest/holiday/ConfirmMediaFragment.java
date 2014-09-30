package de.adamwest.holiday;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;
import com.google.android.gms.maps.model.LatLng;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.helper.CameraManager;
import de.adamwest.helper.Constants;

import java.security.Key;

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
               storeMediaElementInDatabase();
            }
        });

        view.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraManager.currentFile.delete();
                getActivity().getFragmentManager().beginTransaction().remove(ConfirmMediaFragment.this).commit();
            }
        });

        ImageView imagePreview = (ImageView)view.findViewById(R.id.imageview_preview);
        if(getArguments().getString(Constants.KEY_CAMERA_TYPE).equals(Constants.TYPE_IMAGE)) {
            imagePreview.setImageURI(Uri.parse(CameraManager.currentFile.getAbsolutePath()));
        }
        else {
            imagePreview.setVisibility(View.GONE);
            VideoView videoView = (VideoView) view.findViewById(R.id.videoview);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(CameraManager.currentFile.getAbsolutePath()));
            videoView.start();
        }


        return view;
    }

    private void storeMediaElementInDatabase() {
        long routeId = DatabaseManager.getHolidayFromId(getActivity(), holidayId).getCurrentRoute().getId();
        String type = getArguments().getString(Constants.KEY_CAMERA_TYPE);
        String path = CameraManager.currentFile.getAbsolutePath();

        long newCreationId = -1;
        if(getArguments().containsKey(Constants.KEY_EVENT_ID)) {
            long eventId = getArguments().getLong(Constants.KEY_EVENT_ID);
            newCreationId = DatabaseManager.createNewMultiMediaElement(getActivity(), type, path, eventId);
        }
        else {
            LatLng loc = new LatLng(getArguments().getDouble(Constants.KEY_LAT),
                    getArguments().getDouble(Constants.KEY_LONG));

            newCreationId = DatabaseManager.createNewEventWithMultiMediaElement(getActivity(), routeId, type, path, loc);
        }


        if(-1 != newCreationId) {
            getActivity().getFragmentManager().beginTransaction().remove(ConfirmMediaFragment.this).commit();
        }
        else{
            //TODO ERROR WHILE CREATING
        }
    }
}
