package de.adamwest.holiday.event;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.database.MultimediaElement;
import de.adamwest.helper.Constants;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by philip on 05/10/14.
 */
public class ImagePreviewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_preview, container, false);
        ImageView imageView = (ImageView)view.findViewById(R.id.image_view_multimedia_image);

        long elementId = getArguments().getLong(Constants.KEY_MULTIMEDIA_ELEMENT_ID);
        MultimediaElement element = DatabaseManager.getMultiMediaEventFromId(getActivity(), elementId);
        if(element != null) {
            if(element.getType().equals(Constants.TYPE_IMAGE)) {
                try {
                    File imgFile = new File(element.getPath());
                    Bitmap bitmapImage = null;
                    if(imgFile.exists()){
                        bitmapImage = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        //Drawable d = new BitmapDrawable(getResources(), myBitmap);

                    }

                    if (bitmapImage != null) {
                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                        int newWidth = displaymetrics.widthPixels;
                        double scaleFactor = bitmapImage.getWidth() / newWidth;
                        int newHeight = new Double(bitmapImage.getHeight() / scaleFactor).intValue();

                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmapImage, newWidth, newHeight, true);
                        //bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(Environment.getExternalStorageDirectory() + "/original.jpg"));

                        //resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(Environment.getExternalStorageDirectory() + "/resized.jpg"));
                        imageView.setImageBitmap(resizedBitmap);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //imageView.setImageURI(Uri.parse(element.getPath()));
            }
            else if(element.getType().equals(Constants.TYPE_VIDEO)) {
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(element.getPath(),
                            MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
                imageView.setImageBitmap(thumb);
            }

            ((TextView)view.findViewById(R.id.text_view_multimedia_element_description)).setText(element.getDescription());

        }
        return view;
    }
}
