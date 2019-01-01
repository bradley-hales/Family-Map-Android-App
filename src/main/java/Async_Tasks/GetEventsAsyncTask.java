package Async_Tasks;

import android.os.AsyncTask;

import com.example.bradl.familymapserver.Model;
import com.example.bradl.familymapserver.ServerProxy;

import java.util.ArrayList;

import Models.Event;
import Request.EventRequest;
import Response.EventResponse;

public class GetEventsAsyncTask extends AsyncTask<EventRequest, String, EventResponse> {

    public interface Context {
        void onGetEventsComplete();
    }
    public interface SettingsActivity{
        void onGetEventsCompleteActivity();
    }

    private Context context;
    private SettingsActivity activity;
    private String port;
    private String host;

    public GetEventsAsyncTask(Context c, String p, String h){
        context = c;
        port = p;
        host = h;
    }
    public GetEventsAsyncTask(SettingsActivity c, String p, String h){
        activity = c;
        port = p;
        host = h;
    }

    @Override
    protected void onPostExecute(EventResponse eventResponse) {
        super.onPostExecute(eventResponse);

        ArrayList<Event> events = eventResponse.getEventList();
        Model model = Model.getInstance();
        // fill Model with Events
        model.fillEvents(events);
        if (context != null){
            context.onGetEventsComplete();
        }
        if (activity != null){
            activity.onGetEventsCompleteActivity();
        }
    }

    @Override
    protected EventResponse doInBackground(EventRequest... eventRequests) {
        ServerProxy serverProxy = new ServerProxy(host, port);
        return serverProxy.getEvents(eventRequests[0]);
    }
}
