package de.adamwest.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import de.adamwest.database.Event;

import java.io.File;

/**
 * Created by philip on 20/01/15.
 */
public final class ImageHelper {
    private ImageHelper() {};

    public static Bitmap resizeBitMap(Context context, String imagePath) {

        File imgFile = new File(imagePath);
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
