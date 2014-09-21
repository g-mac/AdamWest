package de.adamwest.holiday;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.adamwest.database.DatabaseManager;
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

        View view = super.onCreateView(inflater, container, savedInstanceState);

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
                    getActivity().getFragmentManager().beginTransaction().remove(CreateNewRouteFragment.this).commit();
                }
            }
        });
        return view;
    }
}
