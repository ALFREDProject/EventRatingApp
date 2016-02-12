package alfred.eu.eventratingapp.actions;

import android.util.Log;

import java.util.Map;

import alfred.eu.eventratingapp.MainActivity;
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
        Log.i("EventRating-Log", map.get("SubmitRating"));
        UserProfile hereIsSomethingMissing = null;
        Eventrating hereIsSomethingMissing2 = null;
        recommendationManager.createEventRecommendationAnswer(hereIsSomethingMissing2, hereIsSomethingMissing);
        throw new UnsupportedOperationException("Here is some logic missing, but it should return data ");
    }
}
