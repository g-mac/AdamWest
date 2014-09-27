package de.adamwest.holiday;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.database.Event;
import de.adamwest.database.MultimediaElement;
import de.adamwest.helper.Constants;

/**
 * Created by philip on 27/09/14.
 */
public class EventFragment extends Fragment {

    private Event currentEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        long eventId = getArguments().getLong(Constants.KEY_EVENT_ID, -1);
        if(eventId != -1) {
           currentEvent = DatabaseManager.getEventFromId(getActivity(), eventId);
        }

        LinearLayout mediaElementsLayout = (LinearLayout)view.findViewById(R.id.layout_media_elements);
        if(currentEvent.getMultimediaElementList() != null && currentEvent.getMultimediaElementList().size() > 0) {
            for(final MultimediaElement multimediaElement : currentEvent.getMultimediaElementList()) {
                if(multimediaElement.getType().equals(Constants.TYPE_IMAGE)) {
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setImageURI(Uri.parse(multimediaElement.getPath()));
                    imageView.setMaxHeight(100);
                    imageView.setMaxWidth(150);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(multimediaElement.getPath()), "image/*");
                            startActivity(intent);

                        }
                    });
                    mediaElementsLayout.addView(imageView);
                }
            }
        }
        return view;
    }
}
