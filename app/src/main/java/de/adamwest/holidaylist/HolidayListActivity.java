package de.adamwest.holidaylist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import de.adamwest.R;
import de.adamwest.database.DatabaseManager;
import de.adamwest.database.Holiday;
import de.adamwest.helper.Constants;
import de.adamwest.helper.HelpingMethods;
import de.adamwest.holiday.holiday_edit.MapActivity;
import de.adamwest.holiday.holiday_detail.HolidayDetailActivity;

import java.util.List;


public class HolidayListActivity extends Activity {

    private ListView holidayListView;
    private ActionMode actionMode;
    private List<Holiday> holidayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_holiday_list);
        holidayListView = (ListView)findViewById(R.id.listview_route_list);

        holidayList = DatabaseManager.getAllHoliday(getApplicationContext());
        holidayListView.setAdapter(new HolidayListAdapter(holidayList, this));
        holidayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == holidayList.size()) {
                    Fragment createNewHolidayFragment = new CreateNewHolidayFragment();
                    getFragmentManager().beginTransaction().add(R.id.main_layout, createNewHolidayFragment).commit();
                }
                else {
                    long holidayId = ((Holiday)holidayListView.getAdapter().getItem(position)).getId();
                    if(-1 == holidayId) {
                        //creation of new route failed
                        //TODO error msg
                    }
                    else {

                        Intent intent;
                        if(DatabaseManager.getActiveHolidayId(HolidayListActivity.this) != holidayId) {
                            intent = new Intent(getApplicationContext(), HolidayDetailActivity.class);
                        }
                        else {
                            intent = new Intent(getApplicationContext(), MapActivity.class);
                        }
                        intent.putExtra(Constants.KEY_HOLIDAY_ID, holidayId);
                        startActivity(intent);
                    }
                }
            }
        });

        holidayListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                HelpingMethods.log("Size of list: " + holidayList.size());

//                if(actionMode != null) return false;
                if(id == -1) {
                    return false;
                }

                actionMode = startActionMode(new HolidaySelectActionCallback(id, HolidayListActivity.this));
                arg1.setActivated(true);
                return true;
            }
        });
    }


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

    @Override
    protected void onStart() {
        super.onStart();
        HelpingMethods.log("on Resume");
        updateList();
    }

    public ActionMode getActionMode()  {
        return actionMode;
    }

    public void setActionMode(ActionMode actionMode) {
        this.actionMode = actionMode;
    }

    public void deleteHoliday(long holidayId) {
        DatabaseManager.deleteHoliday(this, holidayId);
        holidayList = DatabaseManager.getAllHoliday(getApplicationContext());
        updateList();



    }



    public void updateList() {
        holidayListView.setAdapter(new HolidayListAdapter(holidayList, this));
        HolidayListAdapter adapter = (HolidayListAdapter)holidayListView.getAdapter();
        adapter.notifyDataSetChanged();
    }


}
