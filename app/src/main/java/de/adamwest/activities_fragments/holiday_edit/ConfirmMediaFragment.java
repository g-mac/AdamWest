package de.adamwest.activities_fragments.holiday_edit;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;
import de.adamwest.R;
import de.adamwest.helper.CameraManager;
import de.adamwest.helper.Constants;

/**
 * Created by philip on 26/09/14.
 */
public class ConfirmMediaFragment extends MediaFragmentParent {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_confirm_media, container, false);
        initFragment(view);
        final String type = getArguments().getString(Constants.KEY_CAMERA_TYPE);
        final String path = CameraManager.currentFile.getAbsolutePath();

        view.findViewById(R.id.button_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = eventNameEditText.getText().toString();
                String description = ((EditText) view.findViewById(R.id.edit_text_media_description)).getText().toString();
                storeMediaElementInDatabase(type, path, description, holidayId, eventName);
            }
        });


        ImageView imagePreview = (ImageView) view.findViewById(R.id.imageview_preview);
        if (getArguments().getString(Constants.KEY_CAMERA_TYPE).equals(Constants.TYPE_IMAGE)) {
            imagePreview.setImageURI(Uri.parse(CameraManager.currentFile.getAbsolutePath()));
        } else {
            imagePreview.setVisibility(View.GONE);
            VideoView videoView = (VideoView) view.findViewById(R.id.videoview);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(CameraManager.currentFile.getAbsolutePath()));
            videoView.start();
        }


        return view;
    }


}
