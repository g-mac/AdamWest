package de.adamwest.activities_fragments.holiday_detail.event_display;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.adamwest.R;
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
        final Event event = ((ShowEventActivity)getActivity()).getEvent();
        ((TextView)view.findViewById(R.id.event_description)).setText(event.getDescription());


        if(event.getType().equals(Constants.TYPE_IMAGE)) {
            File imgFile = new File(event.getPath());
            Bitmap bitmapImage;
            if(imgFile.exists()) {
                bitmapImage = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ((ImageView)view.findViewById(R.id.event_picture)).setImageBitmap(bitmapImage);
            }
        }
        else {
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(event.getPath(),
                    MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
            ((ImageView)view.findViewById(R.id.event_picture)).setImageBitmap(thumb);
            view.findViewById(R.id.image_view_video_play).setVisibility(View.VISIBLE);
            view.findViewById(R.id.layout_image_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getPath()));
                    intent.setDataAndType(Uri.parse(event.getPath()), "video/*");
                    startActivity(intent);
                }
            });
        }
        return view;
    }
}
