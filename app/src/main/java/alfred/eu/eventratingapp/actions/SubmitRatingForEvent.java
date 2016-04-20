package alfred.eu.eventratingapp.actions;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import alfred.eu.eventratingapp.MainActivity;
import eu.alfred.api.event.model.Event;
import eu.alfred.api.event.recommendation.Eventrating;
import eu.alfred.api.event.webservice.RecommendationManager;
import eu.alfred.api.personalization.model.UserProfile;
import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.api.speech.Cade;

/**
 * Created by Tobias on 12/02/2016.
 */
public class SubmitRatingForEvent implements ICadeCommand {
    MainActivity main;
    Cade cade;
    RecommendationManager recommendationManager;

    public SubmitRatingForEvent(MainActivity main, Cade cade, RecommendationManager recommendationManager) {
        this.main = main;
        this.cade = cade;
        this.recommendationManager = recommendationManager;
    }

    @Override
    public void performAction(String s, Map<String, String> map) {
        //Log.i("EventRating-Log", map.get("SubmitRating"));
        //Receives JSON parameters to create recommendations answer
        Type responseType1 = new TypeToken<UserProfile>(){}.getType();
        UserProfile userProfile = (new Gson()).fromJson(map.get("userProfile"), responseType1);
        Type responseType2 = new TypeToken<Eventrating>(){}.getType();
        Eventrating eventrating = (new Gson()).fromJson(map.get("eventrating"), responseType2);

        recommendationManager.createEventRecommendationAnswer(eventrating, userProfile);
    }
}
