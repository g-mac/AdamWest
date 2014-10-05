package de.adamwest.holiday.event;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import de.adamwest.database.MultimediaElement;
import de.adamwest.helper.Constants;

import java.util.List;

/**
 * Created by philip on 05/10/14.
 */
public class EventSlideViewPageAdapter extends FragmentStatePagerAdapter {

    private List<MultimediaElement> multimediaElements;
    private long eventId;
    public EventSlideViewPageAdapter(FragmentManager fm, List<MultimediaElement> multimediaElements, long eventId) {
        super(fm);
        this.multimediaElements = multimediaElements;
        this.eventId = eventId;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putLong(Constants.KEY_EVENT_ID, eventId);
        Fragment returnFragment;
        if(0 == position) {

            returnFragment = new AddAttachmentFragment();
        }
        else {
            MultimediaElement element = multimediaElements.get(position - 1);
            args.putLong(Constants.KEY_MULTIMEDIA_ELEMENT_ID, element.getId());

            if(element.getType().equals(Constants.TYPE_TEXT)) {
                returnFragment = new TextPreviewFragment();
            }
            else if(element.getType().equals(Constants.TYPE_IMAGE) || element.getType().equals(Constants.TYPE_VIDEO)) {
                returnFragment = new ImagePreviewFragment();
            }
            else {
                returnFragment = null;
            }
        }
        returnFragment.setArguments(args);
        return returnFragment;
    }

    @Override
    public int getCount() {
        return multimediaElements.size() + 1;
    }

    //TODO delete unused fragments
}


