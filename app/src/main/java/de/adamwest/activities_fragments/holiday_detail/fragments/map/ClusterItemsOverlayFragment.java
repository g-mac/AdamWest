package de.adamwest.activities_fragments.holiday_detail.fragments.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import de.adamwest.R;
import de.adamwest.helper.HelpingMethods;
import de.adamwest.activities_fragments.holiday_detail.adapters.EventGridAdapter;
import de.adamwest.activities_fragments.holiday_detail.HolidayDetailActivity;

/**
 * Created by philip on 20/01/15.
 */
public class ClusterItemsOverlayFragment extends Fragment {
    private GridView gridview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cluster_items_overlay, container, false);
        gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(new EventGridAdapter(getActivity(),((HolidayDetailActivity)getActivity()).getClusterEventList()));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpingMethods.log("clicked");
                getActivity().getSupportFragmentManager().beginTransaction().remove(ClusterItemsOverlayFragment.this).commit();
            }
        });
        return view;
    }
}
