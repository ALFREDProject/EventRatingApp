package alfred.eu.eventratingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import alfred.eu.eventratingapp.actions.SubmitRatingForEvent;
import eu.alfred.api.personalization.helper.eventrecommendation.EventHelper;
import eu.alfred.api.personalization.model.UserProfile;
import eu.alfred.api.personalization.model.eventrecommendation.Event;
import eu.alfred.api.personalization.model.eventrecommendation.Eventrating;
import eu.alfred.api.personalization.model.eventrecommendation.GlobalsettingsKeys;
import eu.alfred.api.personalization.webservice.PersonalizationManager;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.CircleButton;


public class MainActivity extends AppActivity {

    private static final String LOGTAG = MainActivity.class.getSimpleName();

    //Action
    private static final String SUBMIT_RATING = "SubmitRating";
    private PersonalizationManager personalizationManager;   //for UserProfile
    ArrayList<Event> eventsTobeRated = new ArrayList<>();
    private Event currentEvent;
    private int currentIndex = 0;
    private Gson g = new Gson();
    private int currentRate = 3;
    private ImageButton[] stars;
    private TextView textViewTitle;
    private TextView textViewTime;
    private MainActivity instance;

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        instance = this;
        try
        {
            eventsTobeRated = (ArrayList<Event>) EventHelper.jsonToEventList(prefs.getString(GlobalsettingsKeys.userEventsAccepted,""));
            setView();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void setView() {

        if(eventsTobeRated.size()!=0)
        {
            currentEvent = eventsTobeRated.get(currentIndex);
            currentIndex++;
        }
        currentEvent = new Event();
        currentEvent.setTitle("Test event");
        currentEvent.setStart_date(new Date());
        displayEvent(currentEvent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //GUI
        setContentView(alfred.eu.eventratingapp.R.layout.activity_main);

        //Stars buttons
        stars  = new ImageButton[5];
        TextView title = (TextView) findViewById(R.id.eventTitle);
        if(currentEvent!=null)
            title.setText(currentEvent.getTitle());
        stars[0] = (ImageButton) findViewById(R.id.imageButtonStar1);
        stars[1] = (ImageButton) findViewById(R.id.imageButtonStar2);
        stars[2] = (ImageButton) findViewById(R.id.imageButtonStar3);
        stars[3] = (ImageButton) findViewById(R.id.imageButtonStar4);
        stars[4] = (ImageButton) findViewById(R.id.imageButtonStar5);
        displayRate();

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTime = (TextView) findViewById(R.id.textViewTime);

        //Alfred button
        circleButton = (CircleButton) findViewById(R.id.voiceControlBtn);
        circleButton.setOnTouchListener(new CircleTouchListener());
    }

    @Override
    public void performAction(String command, Map<String, String> map) {
        switch (command) {
            case (SUBMIT_RATING):
                // Somehow the eventId and the userId must be transfered to the action
                SubmitRatingForEvent srfe = new SubmitRatingForEvent(this, cade); //,recommendationManager);
                srfe.performAction(command, map);
                break;
            default:
                break;
        }
    }

    @Override
    public void performWhQuery(String s, Map<String, String> map) {

    }

    @Override
    public void performValidity(String s, Map<String, String> map) {

    }

    @Override
    public void performEntityRecognizer(String s, Map<String, String> map) {

    }


    private void displayEvent(Event event) {
        Log.d(LOGTAG, "Display event: " + event.getTitle());

        textViewTitle.setText(event.getTitle());
        //TODO convert date in
        // - today
        // - tomorrow
        // - 16 april...
        textViewTime.setText(DateFormat.getDateTimeInstance().format(event.getStart_date()));
    }


    private void displayRate() {
        Log.d(LOGTAG, "Display rate: " + currentRate);
        for (int i = 0; i < stars.length; i++) {
            Log.d(LOGTAG, "Setting display rate for " + i);
            if (i < currentRate) {
                stars[i].setImageResource(R.drawable.star_filled);
            } else {
                stars[i].setImageResource(R.drawable.star_empty);
            }
        }
    }

    public void onClickStar(View v) {
        Log.d(LOGTAG, "Star button");
        int clickedRate = 0;
        for (ImageButton ib : stars) {
            clickedRate++;
            if (ib.equals(v)) {
                Log.d(LOGTAG, "Clicked " + clickedRate);
                break;
            }
        }
        if (currentRate == clickedRate) {
            currentRate = clickedRate - 1;
        } else {
            currentRate = clickedRate;
        }
        displayRate();
    }

    public void onClickSubmit(View v) {
        Log.d(LOGTAG, "Submit button");
        Toast.makeText(MainActivity.this, "Submit", Toast.LENGTH_SHORT).show();
        Map<String, String> map = new HashMap<>();

        //TODO get the correct userProfile
        //UserProfile userProfile = personalizationManager.retrieveUserProfile(userId).get(0);
        UserProfile userProfile = new UserProfile();

        Eventrating eventrating = new Eventrating();
        eventrating.setRating(currentRate);

        map.put("userProfile", (new Gson()).toJson(userProfile));
        map.put("eventrating", (new Gson()).toJson(eventrating));

        loadNextEvent();
        currentIndex++;
        performAction(SUBMIT_RATING, map);
    }

    private void loadNextEvent() {
        setView();
    }

    public void onClickCancel(View v) {
        Log.d(LOGTAG, "Cancel button");
        finish();
    }


    /**
     * Receives the notification
     * TODO: here it is supposed there is a JSON in the intent with the event
     */
    private void setBroadcast() {
        //TODO set filter ?
        IntentFilter filter = new IntentFilter();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Type responseType = new TypeToken<Event>(){}.getType();
            displayEvent((Event) (new Gson()).fromJson(intent.getStringExtra("event"), responseType) );
        }
    };
}
