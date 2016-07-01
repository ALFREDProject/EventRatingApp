package alfred.eu.eventratingapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Map;

import eu.alfred.api.personalization.helper.eventrecommendation.EventHelper;
import eu.alfred.api.personalization.helper.eventrecommendation.EventRatingTransfer;
import eu.alfred.api.personalization.model.eventrecommendation.Event;
import eu.alfred.api.personalization.model.eventrecommendation.Eventrating;
import eu.alfred.api.personalization.model.eventrecommendation.GlobalsettingsKeys;
import eu.alfred.api.personalization.webservice.PersonalizationManager;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.BackToPAButton;
import eu.alfred.ui.CircleButton;


public class MainActivity extends AppActivity {

    private static final String LOGTAG = MainActivity.class.getSimpleName();
    private View noEvent;
    private View main;

    //Action
    private static final String SUBMIT_RATING = "SubmitRating";
    private PersonalizationManager personalizationManager;   //for UserProfile
    ArrayList<EventRatingTransfer> eventsTobeRated = new ArrayList<>();
    private EventRatingTransfer currentEvent;
    private int currentIndex = 0;
    private Gson g = new Gson();
    private int currentRate = 3;
    private ImageButton[] stars;
    private TextView textViewTitle;
    private TextView textViewTime;
    private MainActivity instance;
    private String userId;

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        instance = this;
        try
        {
            getSharedPreferences("global_settings", MODE_ENABLE_WRITE_AHEAD_LOGGING);
            this.userId  = prefs.getString(GlobalsettingsKeys.userId,"");
            if(userId=="")
            {
                new AlertDialog.Builder(this)
                        .setTitle("Not logged in")
                        .setMessage("Please login to use eventrecommendations by using the ProfileEditorApp")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        })

                    /*.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })*/
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else
            {

                main = findViewById(R.id.viewMainRating);
                noEvent = findViewById(R.id.viewNoFurtherEvents);

                getSharedPreferences("global_settings", MODE_ENABLE_WRITE_AHEAD_LOGGING);
                String json = prefs.getString(GlobalsettingsKeys.userEventsAccepted,"");
                eventsTobeRated = EventHelper.jsonToEventTransferList(json);
                setView();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void setView() {
        try
        {
            if(eventsTobeRated.size()!=0)
            {
                if(currentIndex<=eventsTobeRated.size()) {
                    currentEvent = eventsTobeRated.get(currentIndex);
                    currentIndex++;

                    main.setVisibility(View.VISIBLE);
                    noEvent.setVisibility(View.INVISIBLE);
                    displayEvent(currentEvent);
                }
            }
            else
            {
                noEventsAvailable();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void noEventsAvailable() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                findViewById(R.id.viewMainRating).setVisibility(View.INVISIBLE);
                findViewById(R.id.viewNoFurtherEvents).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //GUI
        onCreateBuildGui();
    }

    private void onCreateBuildGui()
    {
        setContentView(alfred.eu.eventratingapp.R.layout.activity_main);

        //Stars buttons
        stars  = new ImageButton[5];
        TextView title = (TextView) findViewById(R.id.eventTitle);

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        // TextView startDate = (TextView) findViewById(R.id.startDate);
        //TextView endDate = (TextView) findViewById(R.id.endDate);

        if(currentEvent!=null)
        {
            textViewTitle.setText(currentEvent.getTitle());
            title.setText(currentEvent.getTitle());
        }
        stars[0] = (ImageButton) findViewById(R.id.imageButtonStar1);
        stars[1] = (ImageButton) findViewById(R.id.imageButtonStar2);
        stars[2] = (ImageButton) findViewById(R.id.imageButtonStar3);
        stars[3] = (ImageButton) findViewById(R.id.imageButtonStar4);
        stars[4] = (ImageButton) findViewById(R.id.imageButtonStar5);
        displayRate();

        //Alfred buttons
        circleButton = (CircleButton) findViewById(R.id.voiceControlBtn);
        circleButton.setOnTouchListener(new MicrophoneTouchListener());

        backToPAButton = (BackToPAButton) findViewById(R.id.backControlBtn);
        backToPAButton.setOnTouchListener(new BackTouchListener());
    }

    @Override
    public void performAction(String command, Map<String, String> map) {
        switch (command) {
            case ("RateEventAction"):
                this.currentRate = Integer.parseInt(map.get("selected_stars_size").replace("stars_size_",""));
                this.displayRate();
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


    private void displayEvent(EventRatingTransfer event) {
        Log.d(LOGTAG, "Display event: " + event.getTitle());

        textViewTitle.setText(event.getTitle());
        //TODO convert date in
        // - today
        // - tomorrow
        // - 16 april...
        textViewTime.setText(DateFormat.getDateTimeInstance().format(event.getStart_date()));
        onCreateBuildGui();
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
        Log.d(LOGTAG, "Submit button");//TODO Webservice call here
        Toast.makeText(MainActivity.this, "Submit", Toast.LENGTH_SHORT).show();



        Eventrating r = new Eventrating(true,currentRate,currentEvent.getEventID(),this.userId);
        eventrecommendationManager.submitRating(r);

        //currentIndex++;
        if(eventsTobeRated.size()<currentIndex)
            noEventsAvailable();
        else
            loadNextEvent();
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
        public void onReceive(Context context, Intent intent) {//Probably wrong
            Type responseType = new TypeToken<Event>(){}.getType();
            displayEvent((EventRatingTransfer) (new Gson()).fromJson(intent.getStringExtra("event"), responseType) );
        }
    };
}
