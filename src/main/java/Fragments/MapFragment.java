package Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bradl.familymapserver.Model;
import com.example.bradl.familymapserver.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Activities.FilterActivity;
import Activities.PersonActivity;
import Activities.SearchActivity;
import Activities.SettingsActivity;
import Models.Event;
import Models.Person;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    HashMap<Marker, Event> markersToEvents;
    HashMap<String, Event> selectedEvents;
    HashMap<String, Person> selectedPeople;
    List<Polyline> lines;
    Model model = Model.getInstance();
    Model.Settings settings = model.getSettings();
    Marker startEvent;
    String mselectedPersonID;
    String mSelectedEventID;
    Event mSelectedEvent;

    private TextView mNameClicked;
    private TextView mNameLocClicked;
    private TextView mLocationClicked;
    private ImageView mGenderImage;
    private View mEventInfoBox;
    private float mCameraZoom = 3.0f;

    private RecyclerView mFilterRecyclerView;
    private RecyclerView mSettingsRecyclerView;
    private RecyclerView mSearchRecyclerView;

    private MenuItem mFilterButton;
    private MenuItem mSearchButton;
    private MenuItem mSettingsButton;

    private String PERSON_INTENT_KEY = "person";
    private String EVENT_INTENT_KEY = "event";

    private boolean hasEventData = false;

    private static int POLYLINE_STROKE_WIDTH_PX = 16;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMap != null){
            updateMap();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.filter_item:
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent);
                return true;
            case R.id.settings_item:
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.search_item:
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                startActivity(searchIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check for event data
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(EVENT_INTENT_KEY)){
            hasEventData = true;
            mSelectedEventID = getArguments().getString(EVENT_INTENT_KEY);

            setHasOptionsMenu(false);
        }
        else {
            hasEventData = false;
            setHasOptionsMenu(true);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        selectedPeople = model.getFilteredPeople(); // was: getPeople(); // should be getFilteredPeople
        selectedEvents = model.getFilteredEvents(); // was: getEvents(); // should be getFilteredEvents

        mNameClicked = (TextView) v.findViewById(R.id.name_map_text);
        mLocationClicked = (TextView) v.findViewById(R.id.location_map_text);
//        mNameLocClicked = (TextView) v.findViewById(R.id.name_loc_map_text);
        mGenderImage = (ImageView) v.findViewById(R.id.gender_image);
        mEventInfoBox = (View) v.findViewById(R.id.event_info_box);
        mEventInfoBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event clickedEvent = (Event) v.getTag();
                if (clickedEvent != null){
                    mselectedPersonID = clickedEvent.getPerson();
                    Intent intent = new Intent(getActivity(), PersonActivity.class);
                    intent.putExtra(PERSON_INTENT_KEY, mselectedPersonID);
                    startActivity(intent);
                }

            }
        });

        mFilterRecyclerView = (RecyclerView) v.findViewById(R.id.filter_recycler_view);

        markersToEvents = new HashMap<>();
        lines = new ArrayList<>();

        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null){
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMapType();

        mMap.setOnMarkerClickListener(this);

        populateMap(hasEventData);

        if (hasEventData){
            centerMap(mSelectedEventID);
            Event e = model.getEvent(mSelectedEventID);
            drawLines(e);

        }
        else{
            setText("Click on a marker", "to see event details");
            setImageDefault();
        }

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Event ev = markersToEvents.get(marker);
        mselectedPersonID = ev.getPerson();
        mSelectedEventID = ev.getEventID();
        String location = ev.getEventType() + ": " + ev.getCity() + ", " + ev.getCountry() + " (" + ev.getYear() + ")";
        String name = model.getMarkerName(ev.getPerson());
        String gender = model.getGender(ev.getPerson());
        // set selected person
        model.setSelectedPerson(ev.getPerson());

        // set the event
        mEventInfoBox.setTag(ev);

        setText(name, location);
        setImageGender(gender);

//        removePolyLines();
//        drawLines(ev.getPerson()); // TODO: drawlines change
        drawLines(ev);

        return true;
    }

    private void setImageGender(String gend){
        Drawable genderIcon;


        if (gend.equals("m")){
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                    colorRes(R.color.male_icon).sizeDp(40);
            mGenderImage.setImageDrawable(genderIcon);
        }
        else if (gend.equals("f")){
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                    colorRes(R.color.female_icon).sizeDp(40);
            mGenderImage.setImageDrawable(genderIcon);
        }
    }

    private void setImageDefault(){
        Drawable genderIcon;

        genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android).
                colorRes(R.color.android_icon_color).sizeDp(40);
        mGenderImage.setImageDrawable(genderIcon);
    }

    private void setText(String topText, String bottomText){
        mNameClicked.setTextSize(18);
        mLocationClicked.setTextSize(18);
        mNameClicked.setText(topText);
        mLocationClicked.setText(bottomText);
    }

    public void centerMap(String evID){
        Event event = selectedEvents.get(evID);
        if (event != null){
            Person person = selectedPeople.get(event.getPerson());

            double lat = event.getLatitude();
            double lng = event.getLongitude();

            LatLng latlng = new LatLng(lat, lng);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, mCameraZoom)); // was just newLatLng()

            String location = model.getEventDetails(event);
            String gender = model.getGender(event.getPerson());
            String name = model.getPersonName(person);

            mEventInfoBox.setTag(event);

            setText(name, location);
            setImageGender(gender);
        }

    }

    public void updateMap(){
        mMap.clear();
        markersToEvents.clear();
        lines.clear();

        setMapType();

        // fill the map and store markers
        String userBirth = model.getUserBirth();
        Marker marker;
        HashMap<String, Float> colorVals = settings.getMarkerColors();  // was: getDefaultColors();
        for(Map.Entry<String, Event> pair: model.getFilteredEvents().entrySet()){  // was: getEvents().entrySet()){
            Event ev = pair.getValue();
            String key = pair.getKey();
            String evType = ev.getEventType().toLowerCase();
            LatLng loc = new LatLng(ev.getLatitude(), ev.getLongitude());

            marker = mMap.addMarker(new MarkerOptions()
                    .position(loc)
                    .icon(BitmapDescriptorFactory.defaultMarker(colorVals.get(evType))));



            // initialize the first event shown on the map
            if (key.equals(userBirth)){
                startEvent = marker;
            }

            // put marker in map with event
            markersToEvents.put(marker, ev);
        }

        if (mSelectedEventID != null){
            centerMap(mSelectedEventID);
            Event e = model.getEvent(mSelectedEventID);
            drawLines(e);

        }


    }

    public void populateMap(boolean hasEventData){

        if (hasEventData){
            mMap.clear();
            markersToEvents.clear();
            lines.clear();
        }
        // fill the map and store markers
        String userBirth = model.getUserBirth();
        Marker marker;
        HashMap<String, Float> colorVals = settings.getMarkerColors();  // was: getDefaultColors();
        for(Map.Entry<String, Event> pair: model.getFilteredEvents().entrySet()){  // was: getEvents().entrySet()){
            Event ev = pair.getValue();
            String key = pair.getKey();
            String evType = ev.getEventType().toLowerCase();
            LatLng loc = new LatLng(ev.getLatitude(), ev.getLongitude());

            marker = mMap.addMarker(new MarkerOptions()
                    .position(loc)
                    .icon(BitmapDescriptorFactory.defaultMarker(colorVals.get(evType))));



            // initialize the first event shown on the map
            if (key.equals(userBirth)){
                startEvent = marker;
            }

            // put marker in map with event
            markersToEvents.put(marker, ev);
        }
        if(hasEventData){
            centerMap(mSelectedEventID);
        }

    }


    private void drawLines(Event clickedEvent){
        POLYLINE_STROKE_WIDTH_PX = 16;

        removePolyLines();

        lines = new ArrayList<>();
        if (clickedEvent != null){
            Person selectedPerson = model.getPerson(clickedEvent.getPerson());
            ArrayList<Event> selectedPersonEvents = model.getPersonEvents(selectedPerson.getPersonID());
            Event firstEvent = selectedPersonEvents.get(0);
            LatLng loc = new LatLng(clickedEvent.getLatitude(), clickedEvent.getLongitude());
            LatLng firstEventLoc = new LatLng(firstEvent.getLatitude(), firstEvent.getLongitude());


            String father = selectedPerson.getFather();
            String mother = selectedPerson.getMother();

            fatherSide(father, loc, POLYLINE_STROKE_WIDTH_PX);
            motherSide(mother, loc, POLYLINE_STROKE_WIDTH_PX);
            drawSpouse(selectedPerson, loc);
            drawCurPersonLines(selectedPerson, firstEventLoc);
        }


    }

    private void fatherSide(String fID, LatLng prevLoc, int width){
        // get the latitude and longitude points
        // to connect the lines by passing in
        // person ID, latlng of previous person
        boolean canDrawLine = settings.getDrawLine().get("father side");
        int lineColor = settings.getEventGroupToLineColor().get("father side");
        int lineWidth = width - 3;


        if (fID != null && canDrawLine){
            Person curPerson = model.getPerson(fID);
            String father = curPerson.getFather();
            String mother = curPerson.getMother();
            LatLng curLoc = null;

            ArrayList<Event> curEvents = model.getPersonEvents(fID);

            // make sure curEvents is not empty and there is a location
            if (curEvents.size() != 0 && prevLoc != null){
                Event firstEv = curEvents.get(0);
                curLoc = new LatLng(firstEv.getLatitude(), firstEv.getLongitude());

                Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(prevLoc, curLoc).color(lineColor).width(lineWidth));
                lines.add(polyline1);
            }

            // call fatherSide Recursively
            fatherSide(father, curLoc, lineWidth);
            fatherSide(mother, curLoc, lineWidth);

        }




    }
    private void motherSide(String mID, LatLng prevLoc, int width){
        // get the latitude and longitude points
        // to connect the lines by passing in
        // person ID, latlng of previous person
        boolean canDrawLine = settings.getDrawLine().get("mother side");
        int lineColor = settings.getEventGroupToLineColor().get("mother side");
        int lineWidth = width - 3;


        if (mID != null && canDrawLine){
            Person curPerson = model.getPerson(mID);
            String father = curPerson.getFather();
            String mother = curPerson.getMother();
            LatLng curLoc = null;

            ArrayList<Event> curEvents = model.getPersonEvents(mID);

            // make sure curEvents is not empty and there is a location
            if (curEvents.size() != 0 && prevLoc != null){
                Event firstEv = curEvents.get(0);
                curLoc = new LatLng(firstEv.getLatitude(), firstEv.getLongitude());

                Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(prevLoc, curLoc).color(lineColor).width(lineWidth));
                lines.add(polyline1);
            }

            // call fatherSide Recursively
            motherSide(father, curLoc, lineWidth);
            motherSide(mother, curLoc, lineWidth);

        }
    }
    private void drawSpouse(Person p, LatLng prevLoc){
        boolean canDrawLine = settings.getDrawLine().get("spouse");
        int lineColor = settings.getEventGroupToLineColor().get("spouse");

        if (p.getSpouse() != null && canDrawLine){
            Person spouse = model.getPerson(p.getSpouse());
            ArrayList<Event> curEvents = model.getPersonEvents(spouse.getPersonID());

            if (curEvents.size() != 0){
                Event firstEv = curEvents.get(0);
                LatLng curLoc = new LatLng(firstEv.getLatitude(), firstEv.getLongitude());

                Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(prevLoc, curLoc).color(lineColor).width(POLYLINE_STROKE_WIDTH_PX -3));
                lines.add(polyline1);
            }

        }
    }

    private void drawCurPersonLines(Person p, LatLng prevLoc){
        boolean canDrawLine = settings.getDrawLine().get("life story");
        int lineColor = settings.getEventGroupToLineColor().get("life story");

        ArrayList<Event> curEvents = model.getPersonEvents(p.getPersonID());
        ArrayList<LatLng> locations = new ArrayList<>();
        locations.add(prevLoc);

        if (curEvents.size() != 0 &&  canDrawLine){
            for (Event e: curEvents){
                LatLng loc = new LatLng(e.getLatitude(), e.getLongitude());
                locations.add(loc);
            }

            Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .addAll(locations).color(lineColor).width(POLYLINE_STROKE_WIDTH_PX - 3));
            lines.add(polyline1);
        }
    }

    private void removePolyLines(){
        for (Polyline line: lines){
            line.remove();
        }
        lines.clear();
    }

    private void setMapType(){
        String typeOfMap = settings.getMapType();

        if (typeOfMap.equals("normal")){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        else if (typeOfMap.equals("hybrid")){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        else if (typeOfMap.equals("satellite")){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }

    }
}
