package de.adamwest.activities_fragments.holiday_detail.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import de.adamwest.helper.Constants;
import de.adamwest.activities_fragments.holiday_detail.fragments.DetailFragment;
import de.adamwest.activities_fragments.holiday_detail.fragments.EventGridFragment;
import de.adamwest.activities_fragments.holiday_detail.fragments.map.MapFragment;

/**
 * Created by Philip on 18.10.2014.
 */
public class DetailsSlideViewPageAdapter extends FragmentStatePagerAdapter {

    public DetailsSlideViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DetailFragment();
//            case 1:
//                return new RouteListFragment();
            case 1:
                return new EventGridFragment();
            case 2:
                return new MapFragment();
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
}
