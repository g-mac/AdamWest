package de.adamwest.holiday;

import android.app.Fragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
public class ConfirmMediaFragment extends MediaFragmentParent {

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_confirm_media, container, false);
        final long holidayId = getArguments().getLong(Constants.KEY_HOLIDAY_ID);
        final String type = getArguments().getString(Constants.KEY_CAMERA_TYPE);
        final String path = CameraManager.currentFile.getAbsolutePath();
        final String description = ((EditText)view.findViewById(R.id.edit_text_media_description)).getText().toString();

        view.findViewById(R.id.button_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               storeMediaElementInDatabase(type, path, description, holidayId);
            }
        });

        view.findViewById(R.id.button_cancel).setOnClickListener(cancelButtonOnClickListener(getArguments().getString(Constants.KEY_CAMERA_TYPE)));

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


}
