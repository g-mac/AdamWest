package de.adamwest.routelist;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import de.adamwest.MapActivity;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.helper.Constants;

/**
 * Created by philip on 20/09/14.
 */
public class CreateNewRouteFragment extends Fragment {

    private EditText routeNameEditText;
    private EditText routeDescriptionEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_new_route, container, false);
        routeNameEditText = (EditText)view.findViewById(R.id.edit_text_route_name);
        routeDescriptionEditText = (EditText)view.findViewById(R.id.edit_text_route_description);

        view.findViewById(R.id.button_abort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().beginTransaction().remove(CreateNewRouteFragment.this).commit();
            }
        });

        view.findViewById(R.id.button_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String routeName = routeNameEditText.getText().toString();
                if(routeName == null || routeName.equals("")) {
                    //TODO send errormsg to user
                    return;
                }

                long routeId = DatabaseManager.createNewRoute(getActivity(), routeName, routeDescriptionEditText.getText().toString());
                if(-1 == routeId) {
                    //creation of new route failed
                    //TODO error msg
                }
                else {
                    Log.i("abc", "RouteId: " + routeId);
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    intent.putExtra(Constants.KEY_ROUTE_ID, routeId);
                    startActivity(intent);
                }
            }
        });
        return view;

    }
}