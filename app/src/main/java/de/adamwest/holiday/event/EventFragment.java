package de.adamwest.holiday.event;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.database.Event;
import de.adamwest.database.MultimediaElement;
import de.adamwest.helper.CameraManager;
import de.adamwest.helper.Constants;

/**
 * Created by philip on 27/09/14.
 */
public class EventFragment extends Fragment {

    private Event currentEvent;
    private ViewPager viewPager;
    private final int CACHE_LIMIT = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        viewPager = (ViewPager)view.findViewById(R.id.view_pager_event);

        long eventId = getArguments().getLong(Constants.KEY_EVENT_ID, -1);
        if(eventId != -1) {
           currentEvent = DatabaseManager.getEventFromId(getActivity(), eventId);
        }
        viewPager.setOffscreenPageLimit(CACHE_LIMIT);
        viewPager.setAdapter(new EventSlideViewPageAdapter(getFragmentManager(), currentEvent.getMultimediaElementList(), eventId));


//        LinearLayout mediaElementsLayout = (LinearLayout)view.findViewById(R.id.layout_media_elements);
//        if(currentEvent.getMultimediaElementList() != null && currentEvent.getMultimediaElementList().size() > 0) {
//            for(final MultimediaElement multimediaElement : currentEvent.getMultimediaElementList()) {
//
//                ImageView imageView = new ImageView(getActivity());
//                imageView.setMaxHeight(100);
//                imageView.setMaxWidth(150);
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_VIEW);
//                        intent.setDataAndType(Uri.parse(multimediaElement.getPath()), "image/*");
//                        startActivity(intent);
//
//                    }
//                });
//
//                if(multimediaElement.getType().equals(Constants.TYPE_IMAGE)) {
//                    imageView.setImageURI(Uri.parse(multimediaElement.getPath()));
//                    mediaElementsLayout.addView(imageView);
//                }
//                else if(multimediaElement.getType().equals(Constants.TYPE_VIDEO)) {
//                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(multimediaElement.getPath(),
//                            MediaStore.Images.Thumbnails.MINI_KIND);
//                    imageView.setImageBitmap(thumb);
//                    mediaElementsLayout.addView(imageView);
//                }
//                else if(multimediaElement.getType().equals(Constants.TYPE_TEXT)) {
//                    TextView textView = new TextView(getActivity());
//                    textView.setText(multimediaElement.getDescription());
//                    mediaElementsLayout.addView(textView);
//                }
//
//            }
//        }

//
        return view;
    }
}
