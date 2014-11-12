package de.adamwest.holiday.holiday_detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import de.adamwest.helper.Constants;
import de.adamwest.holiday.holiday_detail.TabFragments.DetailFragment;
import de.adamwest.holiday.holiday_detail.TabFragments.EventGridFragment;
import de.adamwest.holiday.holiday_detail.TabFragments.MapFragment;
import de.adamwest.holiday.holiday_detail.TabFragments.RouteListFragment;

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
