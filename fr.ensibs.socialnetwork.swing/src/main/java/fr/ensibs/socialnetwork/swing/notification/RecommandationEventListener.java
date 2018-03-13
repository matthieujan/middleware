package fr.ensibs.socialnetwork.swing.notification;

import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.events.EventController;
import fr.ensibs.socialnetwork.events.EventControllerFactory;
import fr.ensibs.socialnetwork.events.EventListener;
import fr.ensibs.socialnetwork.logic.recommandation.RecommandationEvent;
import fr.ensibs.socialnetwork.swing.SocialNetworkFrame;
import static fr.ensibs.socialnetwork.swing.SocialNetworkFrame.DEBUG;
import java.io.Closeable;

/**
 * A listener that reacts to events related to recommendations
 *
 * @author Pascale Launay
 */
public class RecommandationEventListener implements EventListener<RecommandationEvent>, Closeable {

    private Profile me; // my profile
    private final SocialNetworkFrame frame; // the application frame
    private final EventController<RecommandationEvent> recommandationEventController; // the event controler to (un)register this instance

    /**
     * Constructor
     *
     * @param frame the application frame
     */
    public RecommandationEventListener(SocialNetworkFrame frame) {
        this.frame = frame;
        recommandationEventController = EventControllerFactory.getInstance().makeRecommendationEventController();
    }

    /**
     * Start receiving events related to the given user
     *
     * @param me a user
     */
    public void open(Profile me) {
        this.me = me;
        recommandationEventController.addEventListener(this);
    }

    /**
     * Stop receiving events
     */
    @Override
    public void close() {
        recommandationEventController.removeEventListener(this);
    }

    @Override
    public void onEvent(RecommandationEvent event) {
        if (DEBUG) {
            System.out.println("[swing] RecommandationEventListener#onEvent " + event);
        }
        if (me.getEmail().equals(event.getFriend1())
                || me.getEmail().equals(event.getFriend2())) { // I am concerned by the event
            frame.addRecommandation(event);
        }
    }
}
