package de.adamwest.activities_fragments.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.adamwest.R;
import de.adamwest.DatabaseManager;
import de.adamwest.database.Event;
import de.adamwest.helper.Constants;

/**
 * Created by philip on 05/10/14.
 */
public class TextPreviewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_text_preview, container, false);

        long elementId = getArguments().getLong(Constants.KEY_MULTIMEDIA_ELEMENT_ID);
        Event event = DatabaseManager.getEventFromId(getActivity(), elementId);
        ((TextView)view.findViewById(R.id.text_view_event_text)).setText(event.getDescription());

        return view;
    }
}
