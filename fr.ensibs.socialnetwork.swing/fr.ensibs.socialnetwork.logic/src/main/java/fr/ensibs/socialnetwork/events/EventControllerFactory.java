package fr.ensibs.socialnetwork.events;

import fr.ensibs.socialnetwork.logic.profile.ProfileEvent;
import fr.ensibs.socialnetwork.logic.message.MessageEvent;
import fr.ensibs.socialnetwork.logic.recommandation.RecommandationEvent;
import fr.ensibs.socialnetwork.logic.friend.FriendEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * A factory to create unique event controllers for each type of event
 *
 * @author Pascale Launay
 */
public class EventControllerFactory {

    private static EventControllerFactory instance; // singleton instance

    private final Map<Class, EventController> controllers; // the existing instances of controllers, for each type of event

    /**
     * Give a unique instance (singleton) of this class
     *
     * @return a unique instance (singleton) of this class
     */
    public static EventControllerFactory getInstance() {
        if (instance == null) {
            instance = new EventControllerFactory();
        }
        return instance;
    }

    /**
     * Private constructor to prevent from creating multiple instances of this
     * class (singleton pattern)
     */
    private EventControllerFactory() {
        controllers = new HashMap<>();
    }

    /**
     * Give a unique instance of controller for events related to friends
     *
     * @return a unique <code>EventControler<FriendEvent></code> instance
     */
    public EventController<FriendEvent> makeFriendEventController() {
        return makeEventController(FriendEvent.class);
    }

    /**
     * Give a unique instance of controller for events related to profiles
     *
     * @return a unique <code>EventControler<ProfileEvent></code> instance
     */
    public EventController<ProfileEvent> makeProfileEventController() {
        return makeEventController(ProfileEvent.class);
    }

    /**
     * Give a unique instance of controller for events related to messages
     *
     * @return a unique <code>EventControler<MessageEvent></code> instance
     */
    public EventController<MessageEvent> makeMessageEventController() {
        return makeEventController(MessageEvent.class);
    }

    /**
     * Give a unique instance of controller for events related to
     * recommendations
     *
     * @return a unique <code>EventControler<RecommendationEvent></code>
     * instance
     */
    public EventController<RecommandationEvent> makeRecommendationEventController() {
        return makeEventController(RecommandationEvent.class);
    }

    /**
     * Give a unique event controller for a given event type
     *
     * @param <T> the event type
     * @param eventClass the event class
     * @return a unique event controller for this event type
     */
    private <T> EventController<T> makeEventController(Class<T> eventClass) {
        if (!controllers.containsKey(eventClass)) {
            EventController<T> controler = new EventController<>();
            controllers.put(eventClass, controler);
            return controler;
        }
        return controllers.get(eventClass);
    }

}
