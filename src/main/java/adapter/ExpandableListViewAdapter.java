package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import Activities.EventActivity;
import com.example.bradl.familymapserver.Model;
import Activities.PersonActivity;
import com.example.bradl.familymapserver.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Models.Event;
import Models.Person;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;

    // group titles
    private List<String> listDataGroup;

    // child data
    private HashMap<String, List<String>> listDataChild;
    private ArrayList<Person> family;
    private ArrayList<Event> events;
    private Model model = Model.getInstance();
    private String currentGroup;
    private Person mSelectedPerson;
    private ImageView mGenderImage;
    private View mPersonInfoRow;
    private String PERSON_INTENT_KEY = "person";
    private String EVENT_INTENT_KEY = "event";

    public ExpandableListViewAdapter(Context context, List<String> listDataGroup,
                                     HashMap<String, List<String>> listChildData, String pID,
                                     ArrayList<Person> family, ArrayList<Event> events) {
        this.context = context;
        this.listDataGroup = listDataGroup;
        this.listDataChild = listChildData;
        this.mSelectedPerson = model.getPerson(pID);
        this.family = family;
        this.events = events;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataGroup.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
//        LayoutInflater layoutInflater;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.event_list_row_child, null);
        }
        final PersonActivity personActivity = (PersonActivity) context;
//        final EventActivity eventActivity = (EventActivity) context;
        mPersonInfoRow = (View) convertView.findViewById(R.id.person_info_row);
        mPersonInfoRow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (v.getTag() instanceof Person){
                    // it is a person activity
//                    final PersonActivity personActivity = (PersonActivity) context;
                    Intent intent = new Intent(personActivity, PersonActivity.class);
                    Person selectedPerson = (Person) v.getTag();
                    intent.putExtra(PERSON_INTENT_KEY, selectedPerson.getPersonID());
                    personActivity.startActivity(intent);
                }
                else{
                    // it is an event activity
//                    final EventActivity eventActivity = (EventActivity) context; // doesnt like the casting object
                    Intent intent = new Intent(personActivity, EventActivity.class);
                    Event selectedEvent = (Event) v.getTag();
                    intent.putExtra(EVENT_INTENT_KEY, selectedEvent.getEventID());
                    personActivity.startActivity(intent);
                }

            }
        });
        mGenderImage = (ImageView) convertView.findViewById(R.id.gender_image_family);
        TextView textViewChildEvent = convertView
                .findViewById(R.id.textViewChildEvent);
        TextView textViewChildName  = convertView.findViewById(R.id.textViewChildName);

        if (currentGroup.equals("LIFE EVENTS")){
            // if it is a life event list
            String name = model.getPersonName(mSelectedPerson);
            // get the corresponding event
            Event curEvent = events.get(childPosition);
            mPersonInfoRow.setTag(curEvent);

            setEventImage();
            textViewChildEvent.setText(childText);
            textViewChildName.setText(name);
        }
        else{
            String name = childText.substring(1);
            // if it is a family list
            Person curPerson = family.get(childPosition);

            // set the tag to be the person object
            mPersonInfoRow.setTag(curPerson);

            setFamilyImage(curPerson.getGender());
            if (childText.charAt(0) == 's'){
                // type is spouse
                textViewChildEvent.setText(name);
                textViewChildName.setText("Spouse");
            }
            // TODO: added in
            else if (childText.charAt(0) == 'M'){
                // type is Mom
                textViewChildEvent.setText(name);
                textViewChildName.setText("Mom");
            }
            else if (childText.charAt(0) == 'D'){
                // type is Dad
                textViewChildEvent.setText(name);
                textViewChildName.setText("Dad");
            }
            // TODO: till here
            else{
                // type is child
                textViewChildEvent.setText(name);
                textViewChildName.setText("Child");
            }


        }


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataGroup.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataGroup.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataGroup.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        currentGroup = headerTitle;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.events_list_row_group, null);
        }

        TextView textViewGroup = convertView
                .findViewById(R.id.textViewGroup);
        textViewGroup.setTypeface(null, Typeface.BOLD);
        textViewGroup.setText(headerTitle);

        return convertView;
    }
    private void setEventImage(){
        Drawable genderIcon;
        PersonActivity personActivity = (PersonActivity) context;


        genderIcon = new IconDrawable(personActivity, FontAwesomeIcons.fa_map_marker).sizeDp(40);
        mGenderImage.setImageDrawable(genderIcon);

    }
    private void setFamilyImage(String gend){
        Drawable genderIcon;
        PersonActivity personActivity = (PersonActivity) context;

        if (gend.equals("m")){
            genderIcon = new IconDrawable(personActivity, FontAwesomeIcons.fa_male).
                    colorRes(R.color.male_icon).sizeDp(40);
            mGenderImage.setImageDrawable(genderIcon);
        }
        else if (gend.equals("f")){
            genderIcon = new IconDrawable(personActivity, FontAwesomeIcons.fa_female).
                    colorRes(R.color.female_icon).sizeDp(40);
            mGenderImage.setImageDrawable(genderIcon);
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
