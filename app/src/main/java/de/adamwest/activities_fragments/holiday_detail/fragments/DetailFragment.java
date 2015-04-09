package de.adamwest.activities_fragments.holiday_detail.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.adamwest.R;
import de.adamwest.DatabaseManager;
import de.adamwest.database.Holiday;
import de.adamwest.database.Route;
import de.adamwest.helper.HolidayStatsCalculator;
import de.adamwest.helper.HelpingMethods;
import de.adamwest.activities_fragments.holiday_detail.HolidayDetailActivity;

/**
 * Created by Philip on 18.10.2014.
 */
public class DetailFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        long routeId = ((HolidayDetailActivity) getActivity()).selectedRouteId;
        long holidayId = ((HolidayDetailActivity) getActivity()).getHolidayId();

//        Toast.makeText(getActivity().getApplicationContext(), "selectedRouteId: "+selectedRouteId,Toast.LENGTH_SHORT).show();

        View view;

        if (routeId != -1) {
            view = inflateRouteDetail(inflater, container, routeId);
        } else {
            view = inflateHolidayDetail(inflater, container, holidayId);
        }

        return view;
    }

    private View inflateHolidayDetail(LayoutInflater inflater, ViewGroup container, long holidayId) {
        View view = inflater.inflate(R.layout.fragment_holiday_detail, container, false);
        Holiday holiday = DatabaseManager.getHolidayFromId(getActivity(), holidayId);

        ((TextView) view.findViewById(R.id.text_view_holiday_name)).setText(holiday.getName());
        ((TextView) view.findViewById(R.id.text_view_date)).setText(HelpingMethods.convertDateToFormattedString(holiday.getCreatedAt()));

        ((TextView) view.findViewById(R.id.text_view_description)).setText(holiday.getDescription());
        int amountOfRoutes = holiday.getRouteList() == null ? 0 : holiday.getRouteList().size();
        ((TextView) view.findViewById(R.id.text_view_route_counter)).setText(String.valueOf(amountOfRoutes));
        String distance = HolidayStatsCalculator.getAbsoluteDistanceForHoliday(holiday) + getString(R.string.distance_scale_unit);
        ((TextView) view.findViewById(R.id.text_view_distance_counter)).setText(distance);
        ((TextView) view.findViewById(R.id.text_view_picture_counter)).setText(String.valueOf(HolidayStatsCalculator.getAmountOfImagesForHoliday(holiday)));
        ((TextView) view.findViewById(R.id.text_view_video_counter)).setText(String.valueOf(HolidayStatsCalculator.getAmountOfVideosForHoliday(holiday)));
        ((TextView) view.findViewById(R.id.text_view_text_counter)).setText(String.valueOf(HolidayStatsCalculator.getAmountOfTextsForHoliday(holiday)));

        HelpingMethods.log("absolute Distance: " + HolidayStatsCalculator.getAbsoluteDistanceForHoliday(holiday));
        return view;

    }

    private View inflateRouteDetail(LayoutInflater inflater, ViewGroup container, long routeId) {
        View view = inflater.inflate(R.layout.fragment_holiday_detail, container, false);
//        Holiday holiday = DatabaseManager.getHolidayFromId(getActivity(), holidayId);
        Route route = DatabaseManager.getRouteFromId(getActivity(), routeId);

        ((TextView) view.findViewById(R.id.text_view_holiday_name)).setText(route.getName());
        ((TextView) view.findViewById(R.id.text_view_date)).setText(HelpingMethods.convertDateToFormattedString(route.getCreatedAt()));

        ((TextView) view.findViewById(R.id.text_view_description)).setText(route.getDescription());

//        int amountOfRoutes = holiday.getRouteList() == null ? 0 : holiday.getRouteList().size();
//        ((TextView) view.findViewById(R.id.text_view_route_counter)).setText(String.valueOf(amountOfRoutes));
//
//        String distance = HolidayStatsCalculator.getAbsoluteDistanceForHoliday(holiday) + getString(R.string.distance_scale_unit);
//        ((TextView) view.findViewById(R.id.text_view_distance_counter)).setText(distance);
//        ((TextView) view.findViewById(R.id.text_view_picture_counter)).setText(String.valueOf(HolidayStatsCalculator.getAmountOfImagesForHoliday(holiday)));
//        ((TextView) view.findViewById(R.id.text_view_video_counter)).setText(String.valueOf(HolidayStatsCalculator.getAmountOfVideosForHoliday(holiday)));
//        ((TextView) view.findViewById(R.id.text_view_text_counter)).setText(String.valueOf(HolidayStatsCalculator.getAmountOfTextsForHoliday(holiday)));
//
//        HelpingMethods.log("absolute Distance: " + HolidayStatsCalculator.getAbsoluteDistanceForHoliday(holiday));

        return view;
    }
}
