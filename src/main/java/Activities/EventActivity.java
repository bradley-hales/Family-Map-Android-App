package Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import Fragments.MapFragment;
import com.example.bradl.familymapserver.R;

public class EventActivity extends AppCompatActivity {

    String EVENT_INTENT_KEY = "event";
    String INTENT_KEY_LOGOUT = "logout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // get the selected person
        Intent intent = getIntent();
        String curEventID = intent.getStringExtra(EVENT_INTENT_KEY);

        // send the event to the map fragment
        Bundle bundle = new Bundle();
        bundle.putString(EVENT_INTENT_KEY, curEventID);

        Fragment fragment = new MapFragment();
        fragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();

        manager.beginTransaction()
                .replace(R.id.event_fragment_container, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(INTENT_KEY_LOGOUT, false);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
