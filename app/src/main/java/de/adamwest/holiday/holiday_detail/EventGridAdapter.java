package de.adamwest.holiday.holiday_detail;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.adamwest.R;
import de.adamwest.database.Event;
import de.adamwest.database.MultimediaElement;
import de.adamwest.helper.Constants;
import de.adamwest.helper.HelpingMethods;
import org.w3c.dom.Text;

import java.io.File;
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
            MultimediaElement multimediaElement = ((Event)getItem(position)).getMultimediaElementList().get(0);
            if(multimediaElement.getType().equals(Constants.TYPE_IMAGE)) {
                Bitmap image = resizeBitMap(view, multimediaElement);
                if(image != null) {
                    ((ImageView)view.findViewById(R.id.image_view_grid_item)).setImageBitmap(image);
                }
            }
            else if(multimediaElement.getType().equals(Constants.TYPE_TEXT)) {
                ((ImageView)view.findViewById(R.id.image_view_grid_item)).setImageDrawable(context.getResources().getDrawable(R.drawable.text_icon));
            }
            else if(multimediaElement.getType().equals(Constants.TYPE_VIDEO)) {
                view.findViewById(R.id.image_view_video_play).setVisibility(View.VISIBLE);
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(multimediaElement.getPath(),
                        MediaStore.Images.Thumbnails.MINI_KIND);
                if(thumb!= null) {
                    ((ImageView)view.findViewById(R.id.image_view_grid_item)).setImageBitmap(thumb);
                }
            }
            ((TextView)view.findViewById(R.id.text_view_grid_event_description)).setText(multimediaElement.getDescription());
            return view;
        }
        else {
            return convertView;
        }
    }

    private Bitmap resizeBitMap(View view, MultimediaElement multimediaElement) {

        File imgFile = new File(multimediaElement.getPath());
        Bitmap bitmapImage = null;
        if(imgFile.exists()){
            bitmapImage = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //Drawable d = new BitmapDrawable(getResources(), myBitmap);

        }

        if (bitmapImage != null) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int newWidth = (displaymetrics.widthPixels -8) / 2;
            double scaleFactor = bitmapImage.getWidth() / newWidth;
            int newHeight = new Double(bitmapImage.getHeight() / scaleFactor).intValue();

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmapImage, newWidth, newHeight, true);
            return resizedBitmap;
        }
        else {
            return null;
        }
    }


}
