package fr.ensibs.socialnetwork.swing.profile;

import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.events.EventController;
import fr.ensibs.socialnetwork.events.EventControllerFactory;
import fr.ensibs.socialnetwork.events.EventListener;
import fr.ensibs.socialnetwork.logic.profile.ProfileEvent;
import fr.ensibs.socialnetwork.swing.SocialNetworkFrame;
import static fr.ensibs.socialnetwork.swing.SocialNetworkFrame.DEBUG;
import java.io.Closeable;

/**
 * A listener that update the frame when a friends profile is modified
 *
 * @author Pascale Launay
 */
public class ProfileEventListener implements EventListener<ProfileEvent>, Closeable {

    private Profile me; // my profile
    private final EventController<ProfileEvent> profileEventControler; // the event controler to (un)register this instance
    private final SocialNetworkFrame frame; // the application frame

    /**
     * Constructor
     *
     * @param frame the application frame
     */
    public ProfileEventListener(SocialNetworkFrame frame) {
        this.frame = frame;
        profileEventControler = EventControllerFactory.getInstance().makeProfileEventController();
    }

    /**
     * Start receiving events related to the given user
     *
     * @param me a user
     */
    public void open(Profile me) {
        this.me = me;
        profileEventControler.addEventListener(this);
    }

    /**
     * Stop receiving events
     */
    @Override
    public void close() {
        profileEventControler.removeEventListener(this);
    }

    @Override
    public void onEvent(ProfileEvent event) {
        Profile profile = event.getProfile();
        if (!me.getEmail().equals(profile.getEmail())) {
            if (DEBUG) {
                System.out.println("[swing] ProfileEventListener#onEvent profile=" + profile);
            }
            frame.updateProfile(profile);
        }
    }

}
