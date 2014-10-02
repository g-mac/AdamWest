package de.adamwest.holiday;

import android.app.Fragment;
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

        final long holidayId = getArguments().getLong(Constants.KEY_HOLIDAY_ID);
        view.findViewById(R.id.button_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = ((EditText)view.findViewById(R.id.edit_text_description)).getText().toString();
                storeMediaElementInDatabase(Constants.TYPE_TEXT, null, description, holidayId);

            }
        });

        view.findViewById(R.id.button_cancel).setOnClickListener(cancelButtonOnClickListener(Constants.TYPE_TEXT));

        return view;
    }
}
