package alfred.eu.eventratingapp;


import android.os.Bundle;

import java.util.Map;

import alfred.eu.eventratingapp.actions.SubmitRatingForEvent;
import eu.alfred.api.PersonalAssistantConnection;
import eu.alfred.api.event.webservice.RecommendationManager;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.CircleButton;

public class MainActivity extends AppActivity {
    RecommendationManager recommendationManager;
    ;
    private static final String SUBMIT_RATING = "SubmitRating";
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
}
