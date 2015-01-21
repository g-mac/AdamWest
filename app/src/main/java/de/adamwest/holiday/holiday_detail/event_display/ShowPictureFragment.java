package de.adamwest.holiday.holiday_detail.event_display;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.database.Event;
import de.adamwest.helper.Constants;

import java.io.File;

/**
 * Created by philip on 21/01/15.
 */
public class ShowPictureFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_picture, container, false);
        Event event = ((ShowEventActivity)getActivity()).getEvent();
        ((TextView)view.findViewById(R.id.event_description)).setText(event.getDescription());

        File imgFile = new File(event.getPath());
        Bitmap bitmapImage;
        if(imgFile.exists()) {
            bitmapImage = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ((ImageView)view.findViewById(R.id.event_picture)).setImageBitmap(bitmapImage);
        }
        return view;
    }
}
