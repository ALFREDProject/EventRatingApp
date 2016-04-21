package alfred.eu.eventratingapp.actions;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import alfred.eu.eventratingapp.MainActivity;
import eu.alfred.api.personalization.model.UserProfile;
import eu.alfred.api.personalization.model.eventrecommendation.Eventrating;
import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.api.speech.Cade;

public class SubmitRatingForEvent implements ICadeCommand {

    private static final String LOGTAG = SubmitRatingForEvent.class.getSimpleName();
    private static final String SUBMITRATING = "SubmitRating";


    private MainActivity main;
    private Cade cade;

    public SubmitRatingForEvent(MainActivity main, Cade cade) {
        this.main = main;
        this.cade = cade;
    }

    @Override
    public void performAction(String s, Map<String, String> map) {
        Log.i(LOGTAG, map.get(SUBMITRATING));
        //Receives JSON parameters to create recommendations answer
        Type responseType1 = new TypeToken<UserProfile>(){}.getType();
        UserProfile userProfile = (new Gson()).fromJson(map.get("userProfile"), responseType1);
        Type responseType2 = new TypeToken<Eventrating>(){}.getType();
        Eventrating eventrating = (new Gson()).fromJson(map.get("eventrating"), responseType2);

        //TODO somehow send the rating to the recommendation manager
        //this is not available
        //recommendationManager.createEventRecommendationAnswer(eventrating, userProfile);
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
}
