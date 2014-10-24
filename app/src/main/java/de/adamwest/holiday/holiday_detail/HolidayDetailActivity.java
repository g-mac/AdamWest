package de.adamwest.holiday.holiday_detail;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import de.adamwest.R;
import de.adamwest.helper.Constants;

/**
 * Created by Philip on 18.10.2014.
 */
public class HolidayDetailActivity extends FragmentActivity {

    private long holidayId;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        holidayId = getIntent().getLongExtra(Constants.KEY_HOLIDAY_ID, -1);
        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_detail);
        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(new DetailsSlideViewPageAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });
        createTabBar();
    }

    private void createTabBar() {
        final ActionBar actionBar = getActionBar();

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };

        // Add 3 tabs, specifying the tab's text and TabListener
        ActionBar.Tab detailTab = actionBar.newTab().setText(getString(R.string.tab_detail))
                .setTabListener(tabListener);
        ActionBar.Tab routesTab = actionBar.newTab().setText(getString(R.string.tab_routes))
                .setTabListener(tabListener);
        ActionBar.Tab mapTab = actionBar.newTab().setText(getString(R.string.tab_map))
                .setTabListener(tabListener);
        actionBar.addTab(detailTab);
        actionBar.addTab(routesTab);
        actionBar.addTab(mapTab);

    }

    public long getHolidayId() {
        return holidayId;
    }

}
