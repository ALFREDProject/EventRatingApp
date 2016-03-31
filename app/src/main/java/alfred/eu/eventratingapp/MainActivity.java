package alfred.eu.eventratingapp;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Map;

import alfred.eu.eventratingapp.actions.SubmitRatingForEvent;
import eu.alfred.api.PersonalAssistantConnection;
import eu.alfred.api.event.webservice.RecommendationManager;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.CircleButton;

public class MainActivity extends AppActivity {

    private static final String LOGTAG = MainActivity.class.getSimpleName();

    //Action
    private static final String SUBMIT_RATING = "SubmitRating";

    private RecommendationManager recommendationManager;

    private Boolean[] rate = {true, false, false, false, false};
    private ImageButton[] stars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        personalAssistant.setOnPersonalAssistantConnectionListener(new PersonalAssistantConnection() {
            @Override
            public void OnConnected() {
                recommendationManager = new RecommendationManager(personalAssistant.getMessenger());
                onNewIntent(getIntent());
            }

            @Override
            public void OnDisconnected() {
                // Do some cleanup stuff
            }
        });

        //Stars buttons
        stars = new ImageButton[5];
        stars[0] = (ImageButton) findViewById(R.id.imageButtonStar1);
        stars[1] = (ImageButton) findViewById(R.id.imageButtonStar2);
        stars[2] = (ImageButton) findViewById(R.id.imageButtonStar3);
        stars[3] = (ImageButton) findViewById(R.id.imageButtonStar4);
        stars[4] = (ImageButton) findViewById(R.id.imageButtonStar5);


        //Change your view contents. Note, the the button has to be included last.
        setContentView(alfred.eu.eventratingapp.R.layout.activity_main);

        circleButton = (CircleButton) findViewById(R.id.voiceControlBtn);
        circleButton.setOnTouchListener(new CircleTouchListener());
    }

    @Override
    public void performAction(String command, Map<String, String> map) {
        switch (command) {
            case (SUBMIT_RATING):
                SubmitRatingForEvent cta = new SubmitRatingForEvent(this, cade,recommendationManager);
                cta.performAction(command, map);
                break;
            default:
                break;

        }
    }


    private void displayRate() {
        Log.d(LOGTAG, "Submit button");

    }

    public void onClickStar1(View v) {
        Log.d(LOGTAG, "Star button 1");
        
    }
    public void onClickStar2(View v) {
        Log.d(LOGTAG, "Star button 2");

    }
    public void onClickStar3(View v) {
        Log.d(LOGTAG, "Star button 3");

    }
    public void onClickStar4(View v) {
        Log.d(LOGTAG, "Star button 4");

    }
    public void onClickStar5(View v) {
        Log.d(LOGTAG, "Star button 5");

    }

    public void onClickSubmit(View v) {
        Log.d(LOGTAG, "Submit button");
        //TODO
        Toast.makeText(MainActivity.this, "Submit!", Toast.LENGTH_SHORT).show();
    }

    public void onClickCancel(View v) {
        Log.d(LOGTAG, "Cancel button");
        finish();
    }
}
