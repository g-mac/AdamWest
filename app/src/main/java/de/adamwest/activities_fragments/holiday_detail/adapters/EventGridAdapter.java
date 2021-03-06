package de.adamwest.activities_fragments.holiday_detail.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.adamwest.R;
import de.adamwest.database.Event;
import de.adamwest.helper.Constants;
import de.adamwest.helper.ImageHelper;
import de.adamwest.activities_fragments.holiday_detail.event_display.ShowEventActivity;

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
            final Event event = ((Event)getItem(position));
            if(event.getType().equals(Constants.TYPE_IMAGE)) {
                Bitmap image = ImageHelper.resizeBitMap(context, event.getPath());
                if(image != null) {
                    ((ImageView)view.findViewById(R.id.image_view_grid_item)).setImageBitmap(image);
                }
            }
            else if(event.getType().equals(Constants.TYPE_TEXT)) {
                ((ImageView)view.findViewById(R.id.image_view_grid_item)).setImageDrawable(context.getResources().getDrawable(R.drawable.text_icon));
            }
            else if(event.getType().equals(Constants.TYPE_VIDEO)) {
                view.findViewById(R.id.image_view_video_play).setVisibility(View.VISIBLE);
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(event.getPath(),
                        MediaStore.Images.Thumbnails.MINI_KIND);
                if(thumb!= null) {
                    ((ImageView)view.findViewById(R.id.image_view_grid_item)).setImageBitmap(thumb);
                }
            }
            ((TextView)view.findViewById(R.id.text_view_grid_event_description)).setText(event.getDescription());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), ShowEventActivity.class);
                    intent.putExtra(Constants.KEY_EVENT_ID, event.getId());
                    context.startActivity(intent);
                }
            });
            return view;
        }
        else {
            return convertView;
        }
    }
}
