package de.adamwest.activities_fragments.holiday_detail.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import de.adamwest.activities_fragments.holiday_detail.fragments.DetailFragment;
import de.adamwest.activities_fragments.holiday_detail.fragments.EventGridFragment;
import de.adamwest.activities_fragments.holiday_detail.fragments.map.MapFragment;
import de.adamwest.helper.Constants;

import java.util.HashMap;

/**
 * Created by Philip on 18.10.2014.
 */
public class DetailsSlideViewPageAdapter extends FragmentStatePagerAdapter {

    private HashMap<Integer, Fragment> fragmentMap = new HashMap<Integer, Fragment>();

    public DetailsSlideViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DetailFragment detailFragment = new DetailFragment();
                fragmentMap.put(position, detailFragment);
                return detailFragment;
            case 1:
                EventGridFragment eventGridFragment = new EventGridFragment();
                fragmentMap.put(position, eventGridFragment);
                return eventGridFragment;
            case 2:
                MapFragment mapFragment = new MapFragment();
                fragmentMap.put(position, mapFragment);
                return mapFragment;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return Constants.NUMBER_OF_DETAIL_TABS;
    }

    //refer to: http://stackoverflow.com/questions/7263291/viewpager-pageradapter-not-updating-the-view
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public Fragment getFragment(int position) {
        return fragmentMap.get(position);
    }
}
