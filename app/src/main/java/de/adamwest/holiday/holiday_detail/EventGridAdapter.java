package de.adamwest.holiday.holiday_detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import de.adamwest.R;
import de.adamwest.database.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philip on 09.11.2014.
 */
public class EventGridAdapter extends BaseAdapter {

    private List<Event> eventList;
    private Context context;

    public EventGridAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_event_grid, parent, false);
            return view;
        }
        else {
            return convertView;
        }
    }
}
