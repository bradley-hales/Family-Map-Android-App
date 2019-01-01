package Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import Async_Tasks.GetEventsAsyncTask;
import Async_Tasks.GetPeopleAsyncTask;
import com.example.bradl.familymapserver.Model;
import com.example.bradl.familymapserver.R;

import Request.EventRequest;
import Request.PersonRequest;

public class SettingsActivity extends AppCompatActivity implements GetPeopleAsyncTask.SettingsActivity, GetEventsAsyncTask.SettingsActivity {
    private String INTENT_KEY_LOGOUT = "logout";
    private String EVENT_INTENT_KEY = "event";
    boolean shouldLogOut = false;
    private Switch LifeStorySwitch;
    private Switch FamilyTreeSwitch;
    private Switch SpouseSwitch;
    private Spinner LifeStorySpinner;
    private Spinner FamilyTreeSpinner;
    private Spinner SpouseSpinner;
    private Spinner MapTypeSpinner;
    private View Logout;
    private View Sync;
    private Model model = Model.getInstance();
    Model.Settings settings = model.getSettings();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        LifeStorySwitch = (Switch) findViewById(R.id.SettingsLifeStorySwitch);
        boolean lifeSwitched = settings.getSwitchState("life story");
        LifeStorySwitch.setChecked(lifeSwitched);
        LifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String lineType = "life story";
                settings.changeLinesDrawn(lineType, isChecked);
            }
        });
        FamilyTreeSwitch = (Switch) findViewById(R.id.SettingsFamilyTreeSwitch);
        boolean familySwitched = settings.getSwitchState("father side");
        FamilyTreeSwitch.setChecked(familySwitched);
        FamilyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String lineType1 = "father side";
                String lineType2 = "mother side";
                settings.changeLinesDrawn(lineType1, isChecked);
                settings.changeLinesDrawn(lineType2, isChecked);
            }
        });
        SpouseSwitch = (Switch) findViewById(R.id.SettingsSpouseLinesSwitch);
        boolean spouseSwitched = settings.getSwitchState("spouse");
        SpouseSwitch.setChecked(spouseSwitched);
        SpouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String lineType = "spouse";
                settings.changeLinesDrawn(lineType, isChecked);
            }
        });

        LifeStorySpinner = (Spinner) findViewById(R.id.LifeStoryColorSpinner);
        int lifeColorPosition = settings.getColorPositions("life story");
        LifeStorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);

                String color = (String) item;
                settings.changeEventGroupToLineColor("life story", color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        FamilyTreeSpinner = (Spinner) findViewById(R.id.FamilyTreeColorSpinner);
        int treeColorPosition = settings.getColorPositions("father side");
        FamilyTreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);

                String color = (String) item;
                settings.changeEventGroupToLineColor("father side", color);
                settings.changeEventGroupToLineColor("mother side", color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
        SpouseSpinner = (Spinner) findViewById(R.id.SpouseLinesColorSpinner);
        int spouseColorPosition = settings.getColorPositions("spouse");
        SpouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                String color = (String) item;
                settings.changeEventGroupToLineColor("spouse", color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        MapTypeSpinner = (Spinner) findViewById(R.id.MapTypeSpinner);
        int mapTypePosition = settings.getMapTypePosition();
        MapTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                String typeOfMap = (String) item;
                settings.changeMapType(typeOfMap);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        // TODO: need to get default value for maptype

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> lineAdapter = ArrayAdapter.createFromResource(this,
                R.array.line_color_choices, android.R.layout.simple_spinner_item);
        lineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> mapAdapter = ArrayAdapter.createFromResource(this,
                R.array.map_type_choices, android.R.layout.simple_spinner_item);
        mapAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LifeStorySpinner.setAdapter(lineAdapter);
        FamilyTreeSpinner.setAdapter(lineAdapter);
        SpouseSpinner.setAdapter(lineAdapter);
        FamilyTreeSpinner.setAdapter(lineAdapter);
        MapTypeSpinner.setAdapter(mapAdapter);

        LifeStorySpinner.setSelection(lifeColorPosition);
        FamilyTreeSpinner.setSelection(treeColorPosition);
        SpouseSpinner.setSelection(spouseColorPosition);
        MapTypeSpinner.setSelection(mapTypePosition);


        Sync = (View) findViewById(R.id.resyncData); // TODO: need to figure out resync
        Sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resyncData();
            }
        });
        Logout = (View) findViewById(R.id.logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldLogOut = true;
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // TODO: put logic in to logout
                intent.putExtra(INTENT_KEY_LOGOUT, shouldLogOut);
                startActivity(intent);
                finish();
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // TODO: put logic in to logout
            intent.putExtra(INTENT_KEY_LOGOUT, shouldLogOut);
            startActivity(intent);
            finish();

        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        updateUI();
    }

    public void resyncData(){
        // make person and event request
        String authToken = model.getAuthToken();
        String port = model.getPort();
        String host = model.getHost();
        PersonRequest pRequest = new PersonRequest(authToken);
        EventRequest eRequest = new EventRequest(authToken);

        //get people and events
        GetPeopleAsyncTask getPeople = new GetPeopleAsyncTask(SettingsActivity.this, port, host);
        GetEventsAsyncTask getEvents = new GetEventsAsyncTask(SettingsActivity.this, port, host);
        getPeople.execute(pRequest);
        getEvents.execute(eRequest);
    }

    @Override
    public void onGetPeopleCompleteActivity() {
        // do nothing till events are complete
    }

    @Override
    public void onGetEventsCompleteActivity() {
        model.fillDictionairies();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // TODO: put logic in to logout
        intent.putExtra(INTENT_KEY_LOGOUT, shouldLogOut);
        startActivity(intent);
        finish();
        Toast.makeText(SettingsActivity.this, "Resync of Data complete", Toast.LENGTH_LONG).show();
    }
}
