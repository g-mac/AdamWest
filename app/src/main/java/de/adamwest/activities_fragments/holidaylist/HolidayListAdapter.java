package de.adamwest.activities_fragments.holidaylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.adamwest.R;
import de.adamwest.DatabaseManager;
import de.adamwest.database.Holiday;
import de.adamwest.helper.HelpingMethods;

import java.util.List;

/**
 * Created by philip on 17/09/14.
 */
public class HolidayListAdapter extends BaseAdapter {
    private List<Holiday> holidays;
    private Context context;

    public HolidayListAdapter(List<Holiday> holidays, Context context) {
        this.holidays = holidays;
        this.context = context;
    }
    @Override
    public int getCount() {

        return holidays.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if(position <= holidays.size()) {
            return holidays.get(position);
        }
        else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        if(0 == holidays.size() || position == holidays.size()) {
            return -1;
        }
        else {
            return ((Holiday)getItem(position)).getId();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(0 == holidays.size() || position == holidays.size()) {
            View view = inflater.inflate(R.layout.list_item_new_holiday, parent, false);
            return view;
        }
        else {
            View view = inflater.inflate(R.layout.list_item_holiday, parent, false);
            Holiday holiday = holidays.get(position);
            if(DatabaseManager.getActiveHolidayId(context) ==  holiday.getId()) {
                //active Holiday
                view.setBackgroundColor(context.getResources().getColor(R.color.route_is_active_color));
            }
            ((TextView)view.findViewById(R.id.text_view_holiday_name)).setText(holiday.getName());
            ((TextView)view.findViewById(R.id.text_view_date)).setText(HelpingMethods.convertDateToFormattedString(holiday.getCreatedAt()));
            String size = String.valueOf(holiday.getRouteList().size());
            ((TextView)view.findViewById(R.id.text_view_route_counter)).setText(size);
            ((TextView)view.findViewById(R.id.text_view_holiday_description)).setText(holiday.getDescription());

            return view;
        }

    }


}
