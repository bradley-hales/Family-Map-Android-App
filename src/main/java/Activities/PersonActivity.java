package Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bradl.familymapserver.Model;
import com.example.bradl.familymapserver.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Models.Event;
import Models.Person;
import adapter.ExpandableListViewAdapter;

public class PersonActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private ExpandableListViewAdapter expandableListViewAdapter;
    private List<String> listDataGroup;
    private HashMap<String, List<String>> listDataChild;
    private ArrayList<Person> listDataChildPeople;
    private ArrayList<Event> listDataChildEvents;
    Model model = Model.getInstance();
    Person mCurPerson;
    String PERSON_INTENT_KEY = "person";
    String EVENT_INTENT_KEY = "event";
    private String INTENT_KEY_LOGOUT = "logout";
    private TextView mFirstNamePerson;
    private TextView mLastNamePerson;
    private TextView mGenderPerson;
    private View mPersonInfoRow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mFirstNamePerson = (TextView) findViewById(R.id.first_name_person);
        mLastNamePerson = (TextView) findViewById(R.id.last_name_person);
        mGenderPerson = (TextView) findViewById(R.id.gender_person);



        // get the selected person
        Intent intent = getIntent();
        String curPersonID = intent.getStringExtra(PERSON_INTENT_KEY);
        mCurPerson = model.getPerson(curPersonID);
        String gender;

        if(mCurPerson.getGender().equals("f")){
            gender = "Female";
        }
        else{
            gender = "Male";
        }

        mFirstNamePerson.setText(mCurPerson.getFirstName());
        mLastNamePerson.setText(mCurPerson.getLastName());
        mGenderPerson.setText(gender);



        // initializing the views
        initViews();

        // initializing the listeners
        initListeners();

        // initializing the objects
        initObjects();

        // preparing list data
        initListData();
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


    /**
     * method to initialize the views
     */
    private void initViews() {

        expandableListView = findViewById(R.id.life_events_list);

    }

    /**
     * method to initialize the listeners
     */
    private void initListeners() {

        // ExpandableListView on child click listener
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        listDataGroup.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataGroup.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        // ExpandableListView Group expanded listener
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataGroup.get(groupPosition) + " " + getString(R.string.text_collapsed),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ExpandableListView Group collapsed listener
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataGroup.get(groupPosition) + " " + getString(R.string.text_collapsed),
                        Toast.LENGTH_SHORT).show();

            }
        });

    }

    /**
     * method to initialize the objects
     */
    private void initObjects() {

        // initializing the list of groups
        listDataGroup = new ArrayList<>();

        // initializing the list of child
        listDataChild = new HashMap<>();

        // initialize the list of array of people
        listDataChildPeople = new ArrayList<>();

        // initialize the list of events
        listDataChildEvents = new ArrayList<>();

        // initializing the adapter object
        expandableListViewAdapter = new ExpandableListViewAdapter(this, listDataGroup, listDataChild,
                mCurPerson.getPersonID(), listDataChildPeople, listDataChildEvents);

        // setting list adapter
        expandableListView.setAdapter(expandableListViewAdapter);


    }

    /*
     * Preparing the list data
     *
     * Dummy Items
     */
    private void initListData() {


        // Adding group data
        listDataGroup.add(getString(R.string.text_Life_Events));
        listDataGroup.add(getString(R.string.text_Family));

        ArrayList<Event> personEvents = model.getPersonEvents(mCurPerson.getPersonID());
        Person spouse = model.getPerson(mCurPerson.getSpouse());
        Person child = model.getChild(mCurPerson.getPersonID());

        // TODO: added in
        Person mom = model.getPerson(mCurPerson.getMother());
        Person dad = model.getPerson(mCurPerson.getFather());
        //TODO: till here

        // array of strings
        String[] array;

        // list of events
        List<String> eventsList = new ArrayList<>();
        for (Event e : personEvents) {
            String evDetails = model.getEventDetails(e);
            eventsList.add(evDetails);
            listDataChildEvents.add(e);
        }

        // list of family
        List<String> familyList = new ArrayList<>();
        if (spouse != null) {
            // add on s for spouse to know which type of person it is
            String spName = 's' + model.getPersonName(spouse);
            familyList.add(spName);
            listDataChildPeople.add(spouse);
        }
        if (child != null) {
            // add on c for child to know which type of person it is
            String cName = 'c' + model.getPersonName(child);
            familyList.add(cName);
            listDataChildPeople.add(child);
        }

        // TODO: added in
        if (mom != null) {
            // add on c for child to know which type of person it is
            String cName = 'M' + model.getPersonName(mom);
            familyList.add(cName);
            listDataChildPeople.add(mom);
        }
        if (dad != null) {
            // add on c for child to know which type of person it is
            String cName = 'D' + model.getPersonName(dad);
            familyList.add(cName);
            listDataChildPeople.add(dad);
        }
        // TODO: till here



        // Adding child data
        listDataChild.put(listDataGroup.get(0), eventsList);
        listDataChild.put(listDataGroup.get(1), familyList);


        // notify the adapter
        expandableListViewAdapter.notifyDataSetChanged();
    }
}
