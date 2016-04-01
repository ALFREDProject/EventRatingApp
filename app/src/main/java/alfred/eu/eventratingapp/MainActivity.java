package alfred.eu.eventratingapp;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
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

    private int currentRate = 3;
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

        //GUI
        setContentView(alfred.eu.eventratingapp.R.layout.activity_main);

        //Stars buttons
        stars  = new ImageButton[5];
        stars[0] = (ImageButton) findViewById(R.id.imageButtonStar1);
        stars[1] = (ImageButton) findViewById(R.id.imageButtonStar2);
        stars[2] = (ImageButton) findViewById(R.id.imageButtonStar3);
        stars[3] = (ImageButton) findViewById(R.id.imageButtonStar4);
        stars[4] = (ImageButton) findViewById(R.id.imageButtonStar5);
        displayRate();

        //Alfred button
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
        //TODO
        Toast.makeText(MainActivity.this, "Submit!", Toast.LENGTH_SHORT).show();
    }

    public void onClickCancel(View v) {
        Log.d(LOGTAG, "Cancel button");
        finish();
    }
}
