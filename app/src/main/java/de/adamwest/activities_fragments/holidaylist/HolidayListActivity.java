package de.adamwest.activities_fragments.holidaylist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import de.adamwest.DatabaseManager;
import de.adamwest.R;
import de.adamwest.activities_fragments.holiday_detail.HolidayDetailActivity;
import de.adamwest.activities_fragments.holiday_edit.MapActivity;
import de.adamwest.database.Holiday;
import de.adamwest.helper.Constants;
import de.adamwest.helper.HelpingMethods;

import java.util.List;


public class HolidayListActivity extends Activity {

    private ListView holidayListView;
    private View activeHolidayView;

    private ActionMode actionMode;

    private List<Holiday> inactiveHolidayList;
    private long activeHolidayId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_list);

        //todo: keep actionbar?
        hideActionBar();

        holidayListView = (ListView) findViewById(R.id.listview_route_list);
        activeHolidayView = findViewById(R.id.holliday_list_active_holiday);

        activeHolidayId = DatabaseManager.getActiveHolidayId(this);

        inactiveHolidayList = DatabaseManager.getAllHoliday(getApplicationContext());
        inactiveHolidayList.remove(DatabaseManager.getHolidayFromId(this, activeHolidayId));


        //todo: activity lifecycle stuff for all the below
        setUpListView();

        if (activeHolidayId == -1)
            activeHolidayView.setVisibility(View.GONE);

        if (inactiveHolidayList.size() == 0)
            holidayListView.setVisibility(View.GONE);

        if (inactiveHolidayList.size() == 0 && activeHolidayId == -1)
            findViewById(R.id.holiday_list_placeholder).setVisibility(View.VISIBLE);

//        HelpingMethods.log("inactiveHolidayList.size(): "+inactiveHolidayList.size());

    }

//------ Activity/Lifecycle Methods ------------------------------------------------------------------------------------

    @Override
    protected void onStart() {
        super.onStart();
        HelpingMethods.log("onStart");
        updateList();
    }

//------ OnClick Methods -----------------------------------------------------------------------------------------------

    public void onNewHolidayClick(View view) {
        Fragment createNewHolidayFragment = new CreateNewHolidayFragment();
        getFragmentManager().beginTransaction().add(R.id.main_layout, createNewHolidayFragment).commit();
//        getFragmentManager().beginTransaction().add(R.id.start_activity_layout, createNewHolidayFragment).commit();
    }

    public void onActiveHolidayClick(View view) {
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
//        Intent intent = new Intent(getApplicationContext(), HolidayDetailActivity.class);
        intent.putExtra(Constants.KEY_HOLIDAY_ID, activeHolidayId);
        startActivity(intent);
    }


//------ ActionBar Methods ---------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public ActionMode getActionMode() {
        return actionMode;
    }

    public void setActionMode(ActionMode actionMode) {
        this.actionMode = actionMode;
    }

    public void hideActionBar() {
        getActionBar().hide();
    }

//------ Data Methods --------------------------------------------------------------------------------------------------

    public void deleteHoliday(long holidayId) {
        DatabaseManager.deleteHoliday(this, holidayId);
        inactiveHolidayList = DatabaseManager.getAllHoliday(getApplicationContext());
        updateList();
    }

//------ Other Methods -------------------------------------------------------------------------------------------------

    private void setUpListView() {

        setUpActiveHolidayView();

        holidayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == inactiveHolidayList.size()) {
                    Fragment createNewHolidayFragment = new CreateNewHolidayFragment();
                    getFragmentManager().beginTransaction().add(R.id.main_layout, createNewHolidayFragment).commit();
                } else {
                    long holidayId = ((Holiday) holidayListView.getAdapter().getItem(position)).getId();
                    if (-1 == holidayId) {
                        //creation of new route failed
                        //TODO error msg
                    } else {

                        Intent intent;
                        if (DatabaseManager.getActiveHolidayId(HolidayListActivity.this) != holidayId) {
                            intent = new Intent(getApplicationContext(), HolidayDetailActivity.class);
                        } else {
                            intent = new Intent(getApplicationContext(), MapActivity.class);
                        }
                        intent.putExtra(Constants.KEY_HOLIDAY_ID, holidayId);
                        startActivity(intent);
                    }
                }
            }
        });

        holidayListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override //todo: remove override?
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                HelpingMethods.log("Size of list: " + inactiveHolidayList.size());

//                if(actionMode != null) return false;
                if (id == -1) {
                    return false;
                }

                actionMode = startActionMode(new HolidaySelectActionCallback(id, HolidayListActivity.this));
                arg1.setActivated(true);
                return true;
            }
        });
    }

    private void setUpActiveHolidayView() {

        Holiday holiday = DatabaseManager.getHolidayFromId(this, activeHolidayId);
        View view = findViewById(R.id.holliday_list_active_holiday);

        ((TextView) view.findViewById(R.id.text_view_holiday_name)).setText(holiday.getName());
        ((TextView) view.findViewById(R.id.text_view_date)).setText(HelpingMethods.convertDateToFormattedString(holiday.getCreatedAt()));
        String size = String.valueOf(holiday.getRouteList().size());
        ((TextView) view.findViewById(R.id.text_view_route_counter)).setText(size);
        ((TextView) view.findViewById(R.id.text_view_holiday_description)).setText(holiday.getDescription());
    }

//------ UI Methods ----------------------------------------------------------------------------------------------------

    public void updateList() {
        holidayListView.setAdapter(new HolidayListAdapter(inactiveHolidayList, this));
        HolidayListAdapter adapter = (HolidayListAdapter) holidayListView.getAdapter();
        adapter.notifyDataSetChanged();
    }


}
