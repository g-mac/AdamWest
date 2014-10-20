package de.adamwest.holidaylist;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import de.adamwest.holiday.holiday_edit.MapActivity;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.helper.Constants;

/**
 * Created by philip on 20/09/14.
 */
public class CreateNewHolidayFragment extends Fragment {

    public EditText holidayNameEditText;
    public EditText holidayDescriptionEditText;

    public Button submitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_new_route, container, false);
        holidayNameEditText = (EditText)view.findViewById(R.id.edit_text_holiday_name);
        holidayDescriptionEditText = (EditText)view.findViewById(R.id.edit_text_holiday_description);

        view.findViewById(R.id.button_abort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().beginTransaction().remove(CreateNewHolidayFragment.this).commit();
            }
        });

        submitButton = (Button)view.findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String routeName = holidayNameEditText.getText().toString();
                if(routeName == null || routeName.equals("")) {
                    //TODO send errormsg to user
                    return;
                }

                long holidayId = DatabaseManager.createNewHoliday(getActivity(), routeName, holidayDescriptionEditText.getText().toString());
                if(-1 == holidayId) {
                    //creation of new route failed
                    //TODO error msg
                }
                else if(DatabaseManager.setHolidayAsActive(getActivity(), holidayId)) {
                    Log.i("abc", "RouteId: " + holidayId);
                    //TODO ask if route should be active maybe?

                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    intent.putExtra(Constants.KEY_HOLIDAY_ID, holidayId);
                    startActivity(intent);
                }
                else {
                    //TODO Something went wrong ;)
                }
            }
        });
        return view;

    }
}