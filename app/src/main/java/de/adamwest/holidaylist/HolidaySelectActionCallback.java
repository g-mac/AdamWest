package de.adamwest.holidaylist;

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

    ActionMode actionMode;
    private long holidayId;
    public HolidaySelectActionCallback(ActionMode actionMode, long holidayId) {
        this.actionMode = actionMode;
        this.holidayId = holidayId;
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
        switch (item.getItemId()) {
            case R.id.action_delete_holiday:
                //shareCurrentItem();
                mode.finish(); // Action picked, so close the CAB
                return true;
            default:
                return false;
        }
    }

    // Called when the user exits the action mode
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        HelpingMethods.log("actionModeDestroyed");
    }
}
