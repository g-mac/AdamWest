package de.adamwest.holiday.holiday_edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import de.adamwest.R;
import de.adamwest.DatabaseManager;
import de.adamwest.helper.Constants;
import de.adamwest.holidaylist.CreateNewHolidayFragment;

/**
 * Created by philip on 21/09/14.
 */
public class CreateNewRouteFragment  extends CreateNewHolidayFragment{

    private long currentHolidayId = -1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = super.onCreateView(inflater, container, savedInstanceState);

        view.findViewById(R.id.layout_active_route).setVisibility(View.VISIBLE);
        currentHolidayId = getArguments().getLong(Constants.KEY_ROUTE_ID, -1);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String routeName = holidayNameEditText.getText().toString();
                if(routeName == null || routeName.equals("")) {
                    //TODO send errormsg to user
                    return;
                }

                long routeId = DatabaseManager.createNewRoute(getActivity(), currentHolidayId, routeName, holidayDescriptionEditText.getText().toString());
                if(-1 == routeId) {
                    //creation of new route failed
                    //TODO error msg
                }
                else {
                    ((MapActivity)getActivity()).updateRouteList();
                    boolean active = ((CheckBox)view.findViewById(R.id.checkbox_route_active)).isChecked();
                    if(active) {
                        DatabaseManager.setActiveRouteForHoliday(getActivity(), currentHolidayId, routeId);
                    }
                    getActivity().getFragmentManager().beginTransaction().remove(CreateNewRouteFragment.this).commit();
                }
            }
        });
        return view;
    }
}
