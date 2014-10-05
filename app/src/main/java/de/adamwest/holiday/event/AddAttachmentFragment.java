package de.adamwest.holiday.event;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.adamwest.R;
import de.adamwest.helper.CameraManager;
import de.adamwest.helper.Constants;
import de.adamwest.holiday.MapActivity;
import de.adamwest.holiday.MediaDescriptionFragment;

/**
 * Created by philip on 05/10/14.
 */
public class AddAttachmentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_attachment, container, false);

        final long eventId = getArguments().getLong(Constants.KEY_EVENT_ID, -1);

        view.findViewById(R.id.imageview_add_attachment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setTitle("Example");
                String[] types = {getString(R.string.menu_picture), getString(R.string.menu_video), getString(R.string.menu_description)};

                b.setItems(types, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        Fragment eventFragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.TAG_EVENT_FRAGMENT);
                        switch(which){
                            case 0:
                                CameraManager.getCameraManager(getActivity()).startCameraForPicture(eventId);
                                eventFragment.getFragmentManager().beginTransaction().remove(eventFragment).commit();

                                break;
                            case 1:
                                CameraManager.getCameraManager(getActivity()).startCameraForVideo(eventId);
                                eventFragment.getFragmentManager().beginTransaction().remove(eventFragment).commit();
                                //onCategoryRequested();
                                break;
                            case 2:
                                Fragment  confirmFragment = new MediaDescriptionFragment();
                                MapActivity mapActivity = (MapActivity)getActivity();
                                Bundle args = new Bundle();
                                args.putLong(Constants.KEY_HOLIDAY_ID, mapActivity.getCurrentHolidayId());
                                args.putLong(Constants.KEY_EVENT_ID, eventId);
                                confirmFragment.setArguments(args);
                                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_map_layout, confirmFragment).commit();
                                getActivity().getSupportFragmentManager().beginTransaction().remove(AddAttachmentFragment.this).commit();
                        }
                    }

                });

                b.show();
            }
        });

        return view;
    }
}
