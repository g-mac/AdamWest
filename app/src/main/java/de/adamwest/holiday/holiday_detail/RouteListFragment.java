package de.adamwest.holiday.holiday_detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.adamwest.R;

/**
 * Created by Philip on 18.10.2014.
 */
public class RouteListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_detail_route_list, container, false);
    }
}
