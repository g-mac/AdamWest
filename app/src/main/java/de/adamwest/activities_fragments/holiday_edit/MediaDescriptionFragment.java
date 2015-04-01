package de.adamwest.activities_fragments.holiday_edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import de.adamwest.R;
import de.adamwest.helper.Constants;

/**
 * Created by philip on 01/10/14.
 */
public class MediaDescriptionFragment extends MediaFragmentParent {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_media_text, container, false);
        initFragment(view);

        view.findViewById(R.id.button_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = ((EditText)view.findViewById(R.id.edit_text_description)).getText().toString();
                String eventName = eventNameEditText.getText().toString();
                storeMediaElementInDatabase(Constants.TYPE_TEXT, null, description, holidayId, eventName);

            }
        });


        return view;
    }
}
