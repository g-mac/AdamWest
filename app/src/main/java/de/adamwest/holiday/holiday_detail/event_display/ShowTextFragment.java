package de.adamwest.holiday.holiday_detail.event_display;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.adamwest.R;
import de.adamwest.database.Event;
import org.w3c.dom.Text;

/**
 * Created by philip on 02/02/15.
 */
public class ShowTextFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Event event = ((ShowEventActivity)getActivity()).getEvent();
        View view = inflater.inflate(R.layout.fragment_show_text, container, false);
        ((TextView)view.findViewById(R.id.text_view_text_description)).setText(event.getDescription());
        return view;
    }
}
