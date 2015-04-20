package de.adamwest.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import de.adamwest.R;
import de.adamwest.activities_fragments.holiday_detail.HolidayDetailActivity;
import de.adamwest.activities_fragments.holiday_edit.ConfirmMediaFragment;
import de.adamwest.activities_fragments.holiday_edit.MapActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by philip on 25/09/14.
 */
public class CameraManager {
    private Context context;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static File currentFile;
    private LatLng currentLoc;
    private long currentEventId;
    private Uri fileUri;
    private static CameraManager instance;

    public void setContext(Context context) {
        this.context = context;
    }

    private CameraManager(Context context) {

        this.context = context;
    }

    public static CameraManager getCameraManager(Context context){
        if(instance == null) {
            instance = new CameraManager(context);
        }
        else {
           instance.setContext(context);
        }
        return instance;
    }

    public void startCameraForPicture(LatLng latLng) {
        currentLoc = latLng;
        currentEventId = -1;
        startCameraForPicture(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void startCameraForPicture(int code) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        ((Activity)context).startActivityForResult(intent, code);
    }

    public void startCameraForPicture(long eventId) {
        currentEventId = eventId;
        currentLoc = null;
        startCameraForPicture(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void startCameraForVideo(int code) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        ((Activity)context).startActivityForResult(intent, code);
    }

    public void startCameraForVideo(long eventId) {
        currentEventId = eventId;
        currentLoc = null;
        startCameraForVideo(CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }

    public void startCameraForVideo(LatLng latLng) {
        currentLoc = latLng;
        currentEventId = -1;
        startCameraForVideo(CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }
        currentFile = mediaFile;
        return mediaFile;
    }

    private void openConfirmFragment(String type) {
        Fragment confirmFragment = new ConfirmMediaFragment();

//        MapActivity mapActivity = (MapActivity)context;
        HolidayDetailActivity holidayDetailActivity = (HolidayDetailActivity)context;
        Bundle args = new Bundle();
        args.putLong(Constants.KEY_HOLIDAY_ID, holidayDetailActivity.getHolidayId());
        args.putString(Constants.KEY_CAMERA_TYPE, type);
        if(currentLoc != null) {
            args.putDouble(Constants.KEY_LAT, currentLoc.latitude);
            args.putDouble(Constants.KEY_LONG, currentLoc.longitude);
        }
        //else if(currentEventId != -1) {
            //args.putLong(Constants.KEY_EVENT_ID, currentEventId);
        //}
        confirmFragment.setArguments(args);
        holidayDetailActivity.getSupportFragmentManager().beginTransaction().add(R.id.main_layout, confirmFragment).commitAllowingStateLoss();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(context, "Image saved to:\n" +
                        currentFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

                openConfirmFragment(Constants.TYPE_IMAGE);


            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(context, "Video saved to:\n" +
                        currentFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

                openConfirmFragment(Constants.TYPE_VIDEO);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }
}
