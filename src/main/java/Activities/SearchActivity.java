package Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.bradl.familymapserver.Model;
import com.example.bradl.familymapserver.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Models.Event;
import Models.Person;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private SearchActivity.SearchAdapter mAdapter;
    private Model model = Model.getInstance();
    private String INTENT_KEY_LOGOUT = "logout";
    private TextView mTopText;
    private TextView mBottomText;
    private ImageView mSearchIcon;
    private View mSearchRow;
    private RecyclerView mSearchRecyclerView;
    private SearchView mSearchBar;
    String PERSON_INTENT_KEY = "person";
    String EVENT_INTENT_KEY = "event";
    private String typeID;
    private ArrayList<String> resultList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSearchBar = (SearchView) findViewById(R.id.search_input);
        mSearchBar.setOnQueryTextListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        ArrayList<String> results = Filter(query.toLowerCase());
        resultList = results;
        model.setSearchText(query);
        if (mAdapter == null){
            mAdapter = new SearchAdapter(results);
            mSearchRecyclerView.setAdapter(mAdapter);
        }
        else{
            mAdapter.notifyDataSetChanged();
            mAdapter = new SearchAdapter(results);
            mSearchRecyclerView.setAdapter(mAdapter);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // TODO: need to implement this
//        mSearchBar.setQuery(newText.toLowerCase(), false);
        return false;
    }

    public ArrayList<String> Filter(String searchText){
        HashMap<String, Event> events = model.getFilteredEvents();
        HashMap<String, Person> people = model.getFilteredPeople();

        ArrayList<String> matches = new ArrayList<>();

        if (searchText.length() == 0){
            // add everything
            for (Map.Entry<String, Person> pair: people.entrySet()){
                String id = pair.getKey();
                // add p for person
                String newID = "p" + id;

                matches.add(newID);
            }
            for (Map.Entry<String, Event> pair: events.entrySet()){
                String id = pair.getKey();
                // add e for event
                String newID = "e" + id;

                matches.add(newID);
            }
        }
        else{
            for (Map.Entry<String, Person> pair: people.entrySet()){
                String id = pair.getKey();
                Person p = pair.getValue();
                String first = p.getFirstName().toLowerCase();
                String last = p.getLastName().toLowerCase();
                // add p for person
                String newID = "p" + id;

                if (first.contains(searchText) || last.contains(searchText)){
                    matches.add(newID);
                }

            }
            for (Map.Entry<String, Event> pair: events.entrySet()){
                String id = pair.getKey();
                Event e = pair.getValue();
                String cntry = e.getCountry().toLowerCase();
                String city = e.getCity().toLowerCase();
                String eType = e.getEventType().toLowerCase();
                String year = String.valueOf(e.getYear());
                // add e for event
                String newID = "e" + id;

                if (cntry.contains(searchText) || city.contains(searchText) ||
                        eType.contains(searchText) || year.contains(searchText)){
                    matches.add(newID);
                }
            }
        }
        // set search Results in Model
        model.setSearchResults(matches);

        return matches;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        updateUI();
        String searchTXT = model.getSearchText();
        ArrayList<String> results = model.getSearchResults();
        mSearchBar.setQuery(searchTXT, true);
        if (searchTXT != null){
            if (mAdapter == null){
                mAdapter = new SearchAdapter(results);
                mSearchRecyclerView.setAdapter(mAdapter);
            }
            else{
                mAdapter.notifyDataSetChanged();
            }
        }
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

    private class SearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private String mEventTypeString = null;
        private String mPersonString = null;


        public SearchHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_search, parent, false));
            itemView.setOnClickListener(this);

            mTopText = (TextView) itemView.findViewById(R.id.textViewChildSearchTop);
            mBottomText = (TextView) itemView.findViewById(R.id.textViewChildSearchBottom);
            mSearchIcon = (ImageView) itemView.findViewById(R.id.search_image);
            mSearchRow = (View) itemView.findViewById(R.id.search_item_row);

        }

        public void bind(String type){
            Drawable genderIcon;
            Drawable markerIcon;

            typeID = type;

            char identifier = type.charAt(0);
            String id = type.substring(1);
            // check the type of object
            if (identifier == 'p'){
                // it is a person object
                Person p = model.getPerson(id);
                String name = model.getPersonName(p);
                String gender = p.getGender();
                mTopText.setText(name);
                setImageGender(gender);
                // set tag to identify the type when clicked
                mSearchRow.setTag(type);
            }
            else {
                // it is an event object
                Event e = model.getEvent(id);
                Person p = model.getPerson(e.getPerson());
                String details = model.getEventDetails(e);
                String name = model.getPersonName(p);
                mTopText.setText(details);
                mBottomText.setText(name);
                markerIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).sizeDp(40);
                mSearchIcon.setImageDrawable(markerIcon);
                // set the tag to be able to identify the type
                mSearchRow.setTag(type);

            }

        }
        private void setImageGender(String gend){
            Drawable genderIcon;


            if (gend.equals("m")){
                genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).
                        colorRes(R.color.male_icon).sizeDp(40);
                mSearchIcon.setImageDrawable(genderIcon);
            }
            else if (gend.equals("f")){
                genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female).
                        colorRes(R.color.female_icon).sizeDp(40);
                mSearchIcon.setImageDrawable(genderIcon);
            }
        }


        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            String identifier = resultList.get(position);
            char type = identifier.charAt(0);
            String id = identifier.substring(1);


            if (type == 'p'){
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra(PERSON_INTENT_KEY, id);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra(EVENT_INTENT_KEY, id);
                startActivity(intent);
            }

        }
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchActivity.SearchHolder>{
        ArrayList<String> mEventPersonIds;

        public SearchAdapter(ArrayList<String> eventPersonIds){
            mEventPersonIds = eventPersonIds;
        }

        @NonNull
        @Override
        public SearchHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            return new SearchHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchHolder searchHolder, int i) {
            String type = mEventPersonIds.get(i);
            searchHolder.bind(type);
        }

        @Override
        public int getItemCount() {
            return mEventPersonIds.size();
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

    }
}
