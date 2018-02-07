package fr.ensibs.socialnetwork.swing.friend;

import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.events.EventController;
import fr.ensibs.socialnetwork.events.EventControllerFactory;
import fr.ensibs.socialnetwork.events.EventListener;
import fr.ensibs.socialnetwork.logic.friend.FriendEvent;
import static fr.ensibs.socialnetwork.logic.friend.FriendEvent.ACCEPT;
import static fr.ensibs.socialnetwork.logic.friend.FriendEvent.BAN;
import static fr.ensibs.socialnetwork.logic.friend.FriendEvent.REQUEST;
import fr.ensibs.socialnetwork.swing.SocialNetworkFrame;
import java.io.Closeable;

/**
 * A listener that reacts to events related to friends by forwarding them to the
 * right panels
 *
 * @author Pascale Launay
 */
public class FriendEventListener implements EventListener<FriendEvent>, Closeable {

    private Profile me; // my profile
    private final SocialNetworkFrame frame; // the application frame
    private final EventController<FriendEvent> friendEventControler; // the event controler to (un)register this instance

    /**
     * Constructor
     *
     * @param frame the application frame
     */
    public FriendEventListener(SocialNetworkFrame frame) {
        this.frame = frame;
        friendEventControler = EventControllerFactory.getInstance().makeFriendEventController();
    }

    /**
     * Start receiving events related to the given user
     *
     * @param me a user
     */
    public void open(Profile me) {
        this.me = me;
        friendEventControler.addEventListener(this);
    }

    /**
     * Stop receiving events
     */
    @Override
    public void close() {
        friendEventControler.removeEventListener(this);
    }

    @Override
    public void onEvent(FriendEvent event) {
        if (me.getEmail().equals(event.getTarget())) { // I am concerned by the event
            switch (event.getType()) {
                case ACCEPT:
                    frame.addFriend(event.getSource());
                    break;
                case BAN:
                    frame.removeFriend(event.getSource());
                    break;
                case REQUEST:
                    frame.addFriendRequest(event);
                    break;
            }
        }
    }
}
