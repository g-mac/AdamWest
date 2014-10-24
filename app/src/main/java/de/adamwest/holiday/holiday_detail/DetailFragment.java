package de.adamwest.holiday.holiday_detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.database.Holiday;
import de.adamwest.helper.HolidayStatsCalculator;
import de.adamwest.helper.HelpingMethods;

/**
 * Created by Philip on 18.10.2014.
 */
public class DetailFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_holiday_detail, container, false);
        Holiday holiday = DatabaseManager.getHolidayFromId(getActivity(), ((HolidayDetailActivity) getActivity()).getHolidayId());

        ((TextView)view.findViewById(R.id.text_view_holiday_name)).setText(holiday.getName());
        ((TextView)view.findViewById(R.id.text_view_date)).setText(HelpingMethods.convertDateToFormattedString(holiday.getCreatedAt()));

        ((TextView)view.findViewById(R.id.text_view_description)).setText(holiday.getDescription());
        int amountOfRoutes = holiday.getRouteList() == null? 0 : holiday.getRouteList().size();
        ((TextView)view.findViewById(R.id.text_view_route_counter)).setText(String.valueOf(amountOfRoutes));
        String distance = HolidayStatsCalculator.getAbsoluteDistanceForHoliday(holiday) + getString(R.string.distance_scale_unit);
        ((TextView)view.findViewById(R.id.text_view_distance_counter)).setText(distance);
        ((TextView)view.findViewById(R.id.text_view_picture_counter)).setText(String.valueOf(HolidayStatsCalculator.getAmountOfImagesForHoliday(holiday)));
        ((TextView)view.findViewById(R.id.text_view_video_counter)).setText(String.valueOf(HolidayStatsCalculator.getAmountOfVideosForHoliday(holiday)));
        ((TextView)view.findViewById(R.id.text_view_text_counter)).setText(String.valueOf(HolidayStatsCalculator.getAmountOfTextsForHoliday(holiday)));

        HelpingMethods.log("absolute Distance: " + HolidayStatsCalculator.getAbsoluteDistanceForHoliday(holiday));
        return view;
    }
}
