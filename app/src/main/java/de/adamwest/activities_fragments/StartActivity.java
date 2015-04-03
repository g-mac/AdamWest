package de.adamwest.activities_fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import de.adamwest.R;
import de.adamwest.activities_fragments.holidaylist.CreateNewHolidayFragment;
import de.adamwest.activities_fragments.holidaylist.HolidayListActivity;
import de.adamwest.helper.HelpingMethods;


//------ Activity/Lifecycle Methods --------------------------------------------------------------------------------

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_start);
        HelpingMethods.createTestData(this);
    }


//------ OnClick Methods -------------------------------------------------------------------------------------------

    public void onBrowseHolidaysClick(View view) {

        Intent intent = new Intent(getApplicationContext(), HolidayListActivity.class);
        startActivity(intent);
//        finish();

//        if (actionBar.isShowing())
//            actionBar.hide();
//        else
//            actionBar.show();
//        Toast.makeText(this, "showing/hiding Action Bar", Toast.LENGTH_SHORT).show();
    }

    public void onNewHolidayClick(View view) {
        Fragment createNewHolidayFragment = new CreateNewHolidayFragment();
        getFragmentManager().beginTransaction().add(R.id.start_activity_layout, createNewHolidayFragment).commit();
    }

    public void onContinueHolidayClick(View view) {
        Intent intent = new Intent(getApplicationContext(), HolidayListActivity.class);
        startActivity(intent);
    }


//------ Options Methods -------------------------------------------------------------------------------------------

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.start, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


}
