package de.adamwest.holiday.holiday_detail.event_display;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import de.adamwest.R;
import de.adamwest.DatabaseManager;
import de.adamwest.database.Event;
import de.adamwest.helper.Constants;

/**
 * Created by philip on 21/01/15.
 */
public class ShowEventActivity extends FragmentActivity {
    private Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_show_event);
        event = DatabaseManager.getEventFromId(this, getIntent().getLongExtra(Constants.KEY_EVENT_ID, -1));
        Fragment fragment = null;
        if(event.getType().equals(Constants.TYPE_IMAGE) || event.getType().equals(Constants.TYPE_VIDEO)) {
            fragment = new ShowPictureFragment();
        }
        else if(event.getType().equals(Constants.TYPE_TEXT)) {
            fragment = new ShowTextFragment();
        }
        if(fragment != null) {
           getSupportFragmentManager().beginTransaction().add(R.id.show_event_layout, fragment).commit();
        }

    }

    public Event getEvent() {
        return event;
    }
}
