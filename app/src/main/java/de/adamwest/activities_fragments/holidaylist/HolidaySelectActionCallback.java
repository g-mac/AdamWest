package de.adamwest.activities_fragments.holidaylist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import de.adamwest.R;
import de.adamwest.helper.HelpingMethods;

/**
 * Created by philip on 10/10/14.
 */
public class HolidaySelectActionCallback implements ActionMode.Callback {

    private long holidayId;
    HolidayListActivity activity;
    public HolidaySelectActionCallback(long holidayId, Activity activity) {
        this.holidayId = holidayId;
        this.activity = (HolidayListActivity)activity;
    }
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.holiday_cab_menu, menu);
        return true;
    }

    // Called each time the action mode is shown. Always called after onCreateActionMode, but
    // may be called multiple times if the mode is invalidated.
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false; // Return false if nothing is done
    }

    // Called when the user selects a contextual menu item
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        HelpingMethods.log("ActionItemClicked");

        switch (item.getItemId()) {
            case R.id.action_delete_holiday:
                buildAlertDialog();
                return false;
            default:
                return false;
        }
    }

    // Called when the user exits the action mode
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        activity.setActionMode(null);
        HelpingMethods.log("actionModeDestroyed");
    }

    public void buildAlertDialog() {
        new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.confirm_delete))
                .setMessage(activity.getString(R.string.confirm_holiday_delete_text))
                .setPositiveButton(activity.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                       activity.deleteHoliday(holidayId);
                        activity.getActionMode().finish(); // Action picked, so close the CAB
                    }
                })
                .setNegativeButton(activity.getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.getActionMode().finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
