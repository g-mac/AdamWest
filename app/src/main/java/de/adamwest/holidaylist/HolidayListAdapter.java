package de.adamwest.holidaylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.adamwest.R;
import de.adamwest.database.Holiday;

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
        return position;
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
            ((TextView)view.findViewById(R.id.text_view_holiday_name)).setText(holiday.getName());
            return view;
        }

    }
}
