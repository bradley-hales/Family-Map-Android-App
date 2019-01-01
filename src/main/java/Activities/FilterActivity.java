package Activities;

import android.content.Intent;
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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bradl.familymapserver.Model;
import com.example.bradl.familymapserver.R;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {
    private RecyclerView mFilterRecyclerView;
    private FilterAdapter mAdapter;
    private Model model = Model.getInstance();
    private String INTENT_KEY_LOGOUT = "logout";
    private String EVENT_INTENT_KEY = "event";

    private TextView mEventType;
    private TextView mFilterBy;
    private Switch mFilterSwitch;
    private String mTypeOfEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


        mFilterRecyclerView = (RecyclerView) findViewById(R.id.filter_recycler_view);
        mFilterRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        updateUI();
    }

    private void updateUI(){
//        Model model = Model.getInstance();
        ArrayList<String> eventTypes = model.getEventTypes();

        // add on the extra required filters
        if (!eventTypes.contains("Fathers Side")) {
            eventTypes.add("Fathers Side");
            eventTypes.add("Mothers Side");
            eventTypes.add("Male");
            eventTypes.add("Female");
        }


        if (mAdapter == null){
            mAdapter = new FilterAdapter(eventTypes);
            mFilterRecyclerView.setAdapter(mAdapter);
        }
        else{
            mAdapter.notifyDataSetChanged();
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

    private class FilterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private String mEventTypeString = null;


        public FilterHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_filter, parent, false));
            itemView.setOnClickListener(this);

            mEventType = (TextView) itemView.findViewById(R.id.textViewFilterEventType);
            mFilterBy = (TextView) itemView.findViewById(R.id.textViewFilterBy);
            mFilterSwitch = (Switch) itemView.findViewById(R.id.filterSwitch);
            mFilterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Toast.makeText(getApplicationContext(), mEventTypeString, Toast.LENGTH_LONG).show();
                    String eventType = mEventType.getText().toString();
                    String type = mEventTypeString.toLowerCase();
                    model.applyFilterToDictionaries(type, isChecked);
                }
            });

        }

        public void bind(String evType){
            mTypeOfEvent = evType;
            mEventTypeString = evType;

            String firstUpper = evType.substring(0,1).toUpperCase();
            String nameUpper = firstUpper + evType.substring(1);

            String type = nameUpper + " Events"; // was evType
            String filterBy = "filter by " + evType + " events";
            mEventType.setText(type);
            mFilterBy.setText(filterBy.toUpperCase());

            // set the checked state of switch
            boolean isChecked = model.checkIfFilterItemActive(evType.toLowerCase());
            mFilterSwitch.setChecked(isChecked);

        }

        @Override
        public void onClick(View v) {

        }
    }

    private class FilterAdapter extends RecyclerView.Adapter<FilterHolder>{
        ArrayList<String> mEventTypes;

        public FilterAdapter(ArrayList<String> eventType){
            mEventTypes = eventType;
        }

        @NonNull
        @Override
        public FilterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            return new FilterHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull FilterHolder filterHolder, int i) {
            String evType = mEventTypes.get(i);
            filterHolder.bind(evType);
        }

        @Override
        public int getItemCount() {
            return mEventTypes.size();
        }
    }
}
