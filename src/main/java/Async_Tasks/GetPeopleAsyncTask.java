package Async_Tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.bradl.familymapserver.Model;
import com.example.bradl.familymapserver.ServerProxy;

import java.sql.Connection;
import java.util.ArrayList;

import Models.Person;
import Request.PersonRequest;
import Response.PersonResponse;

public class GetPeopleAsyncTask extends AsyncTask<PersonRequest, String, PersonResponse> {

    public interface Context {
        void onGetPeopleComplete();
    }
    public interface SettingsActivity{
        void onGetPeopleCompleteActivity();
    }

    private Context context;
    private SettingsActivity activity;
    private String port;
    private String host;

    public GetPeopleAsyncTask(Context c, String p, String h){
        context = c;
        port = p;
        host = h;
    }
    public GetPeopleAsyncTask(SettingsActivity c, String p, String h){
        activity = c;
        port = p;
        host = h;
    }

    @Override
    protected void onPostExecute(PersonResponse personResponse) {
        super.onPostExecute(personResponse);

        ArrayList<Person> people = personResponse.getPersonArray();
        Model model = Model.getInstance();
        // put whole family tree in Model
        model.fillPeople(people);
        if (context != null){
            context.onGetPeopleComplete();
        }
        if (activity != null){
            activity.onGetPeopleCompleteActivity();
        }
    }

    @Override
    protected PersonResponse doInBackground(PersonRequest... personRequests) {
        ServerProxy serverProxy = new ServerProxy(host, port);
        return serverProxy.getPeople(personRequests[0]);
    }
}
