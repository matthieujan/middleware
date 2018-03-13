package fr.ensibs.socialnetwork.logic;

import fr.ensibs.socialnetwork.core.Message;
import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.core.Publication;
import fr.ensibs.socialnetwork.logic.friend.FriendEvent;
import fr.ensibs.socialnetwork.logic.friend.FriendManager;
import fr.ensibs.socialnetwork.logic.image.ImageManager;
import fr.ensibs.socialnetwork.logic.message.MessageEvent;
import fr.ensibs.socialnetwork.logic.message.MessageManager;
import fr.ensibs.socialnetwork.logic.profile.ProfileEvent;
import fr.ensibs.socialnetwork.logic.profile.ProfileManager;
import fr.ensibs.socialnetwork.logic.recommandation.RecommandationEvent;
import fr.ensibs.socialnetwork.logic.recommandation.RecommandationManager;
import fr.ensibs.socialnetwork.events.EventControllerFactory;
import fr.ensibs.socialnetwork.events.EventListener;
import java.awt.Image;
import java.io.IOException;
import java.util.Set;

/**
 * The socialNetwork facade class
 *
 * @author Pascale Launay
 */
public class SocialNetwork implements FriendManager, ProfileManager, MessageManager, ImageManager, RecommandationManager {

    public static boolean DEBUG; // if true, print debug messages

    private ProfileManager profileManager; // the delegate object that manages profiles
    private FriendManager friendManager; // the delegate object that manages friends
    private MessageManager messageManager; // the delegate object that manages messages
    private ImageManager imageManager; // the delegate object that manages images
    private RecommandationManager recommandationManager; // the delegate object that manages recommendations

    /**
     * Constructor
     *
     * @param managerFactory the factory to create communication managers
     */
    public SocialNetwork(CommunicationManagerFactory managerFactory) {
        EventControllerFactory controllerFactory = EventControllerFactory.getInstance();
        controllerFactory.makeProfileEventController().addEventListener(new ProfileEventListener());
        controllerFactory.makeFriendEventController().addEventListener(new FriendEventListener());
        controllerFactory.makeMessageEventController().addEventListener(new MessageEventListener());
        controllerFactory.makeRecommendationEventController().addEventListener(new RecommendationEventListener());

        profileManager = managerFactory.makeProfileManager();
        friendManager = managerFactory.makeFriendManager();
        messageManager = managerFactory.makeMessageManager();
        imageManager = managerFactory.makeImageManager();
        recommandationManager = managerFactory.makeRecommandationManager();
    }

    //------------------------------------------------------------------------
    // Delegate managers
    //------------------------------------------------------------------------
    /**
     * Check whether the friends manager exists
     *
     * @return true if the friends manager exists
     */
    public boolean hasFriendManager() {
        return this.friendManager != null;
    }

    @Override
    public void close() throws IOException {
        if (profileManager != null) {
            profileManager.close();
        }
        if (friendManager != null) {
            friendManager.close();
        }
        if (messageManager != null) {
            messageManager.close();
        }
        if (imageManager != null) {
            imageManager.close();
        }
        if (recommandationManager != null) {
            recommandationManager.close();
        }
    }

    //------------------------------------------------------------------------
    // ProfileManager
    //------------------------------------------------------------------------
    @Override
    public Profile signUp(String email, String pseudo, String password) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#signUp email=" + email
                    + ", pseudo=" + pseudo
                    + ", password=" + password);
        }
        if (profileManager != null) {
            Profile profile = profileManager.signUp(email, pseudo, password);
            if (DEBUG) {
                System.out.println("[core] ... profile=" + profile);
            }
            return profile;
        } else if (DEBUG) {
            System.out.println("[core] ... no ProfileManager !!!");
        }
        return null;
    }

    @Override
    public String logIn(String email, String password) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#logIn email=" + email
                    + ", password=" + password);
        }
        if (profileManager != null) {
            String token = profileManager.logIn(email, password);
            if (DEBUG) {
                System.out.println("[core] ... token=" + token);
            }
            return token;
        } else if (DEBUG) {
            System.out.println("[core] ... no ProfileManager !!!");
        }
        return null;
    }

    @Override
    public boolean logOut(String token) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#logOut token=" + token);
        }
        if (profileManager != null) {
            boolean loggedOut = profileManager.logOut(token);
            if (DEBUG) {
                System.out.println("[core] ... loggedOut=" + loggedOut);
            }
            return loggedOut;
        } else if (DEBUG) {
            System.out.println("[core] ... no ProfileManager !!!");
        }
        return false;
    }

    @Override
    public boolean updateProfile(String token, Profile profile) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#updateProfile token=" + token
                    + ", profile=" + profile);
        }
        if (profileManager != null) {
            boolean changed = profileManager.updateProfile(token, profile);
            if (DEBUG) {
                System.out.println("[core] ... changed=" + changed);
            }
            return changed;
        } else if (DEBUG) {
            System.out.println("[core] ... no ProfileManager !!!");
        }
        return false;
    }

    @Override
    public Profile getProfile(String email) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#getProfile email=" + email);
        }
        if (profileManager != null) {
            Profile profile = profileManager.getProfile(email);
            if (DEBUG) {
                System.out.println("[core] ... profile=" + profile);
            }
            return profile;
        } else if (DEBUG) {
            System.out.println("[core] ... no ProfileManager !!!");
        }
        return null;
    }

    //------------------------------------------------------------------------
    // FriendManager
    //------------------------------------------------------------------------
    @Override
    public void requestFriend(String source, String target) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#requestFriend source=" + source
                    + ", targetEmail=" + target);
        }
        if (friendManager != null) {
            friendManager.requestFriend(source, target);
            if (DEBUG) {
                System.out.println("[core] ... DONE");
            }
        } else if (DEBUG) {
            System.out.println("[core] ... no FriendManager !!!");
        }
    }

    @Override
    public void acceptFriend(String source, String target) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#acceptFriend source=" + source
                    + ", target=" + target);
        }
        if (friendManager != null) {
            friendManager.acceptFriend(source, target);
            if (DEBUG) {
                System.out.println("[core] ... DONE");
            }
        } else if (DEBUG) {
            System.out.println("[core] ... no FriendManager !!!");
        }
    }

    @Override
    public void banFriend(String source, String target) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#banFriend source=" + source
                    + ", target=" + target);
        }
        if (friendManager != null) {
            friendManager.banFriend(source, target);
            if (DEBUG) {
                System.out.println("[core] ... DONE");
            }
        } else if (DEBUG) {
            System.out.println("[core] ... no FriendManager !!!");
        }
    }

    //------------------------------------------------------------------------
    // MessageManager
    //------------------------------------------------------------------------
    @Override
    public void send(Message message) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#send message=" + message);
        }
        if (messageManager != null) {
            messageManager.send(message);
            if (DEBUG) {
                System.out.println("[core] ... DONE");
            }
        } else if (DEBUG) {
            System.out.println("[core] ... no MessageManager !!!");
        }
    }

    @Override
    public void publish(Publication publication) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#publish publication=" + publication);
        }
        if (messageManager != null) {
            messageManager.publish(publication);
            if (DEBUG) {
                System.out.println("[core] ... DONE");
            }
        } else if (DEBUG) {
            System.out.println("[core] ... no MessageManager !!!");
        }
    }

    @Override
    public void subscribe(String subscriber, String source) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#subscribe subscriber=" + subscriber
                    + ", source=" + source);
        }
        if (messageManager != null) {
            messageManager.subscribe(subscriber, source);
            if (DEBUG) {
                System.out.println("[core] ... DONE");
            }
        } else if (DEBUG) {
            System.out.println("[core] ... no MessageManager !!!");
        }
    }

    @Override
    public void unsubscribe(String subscriber, String source) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#unsubscribe subscriber=" + subscriber
                    + ", source=" + source);
        }
        if (messageManager != null) {
            messageManager.unsubscribe(subscriber, source);
            if (DEBUG) {
                System.out.println("[core] ... DONE");
            }
        } else if (DEBUG) {
            System.out.println("[core] ... no MessageManager !!!");
        }
    }

    //------------------------------------------------------------------------
    // ImageManager
    //------------------------------------------------------------------------
    @Override
    public String addImage(Image image) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#addImage image=" + image.getWidth(null) + "x" + image.getHeight(null));
        }
        if (imageManager != null) {
            String imageKey = imageManager.addImage(image);
            if (DEBUG) {
                System.out.println("[core] ... imageKey=" + imageKey);
            }
            return imageKey;
        } else if (DEBUG) {
            System.out.println("[core] ... no ImageManager !!!");
        }
        return null;
    }

    @Override
    public Image getImage(String imageKey) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#getImage imageKey=" + imageKey);
        }
        if (imageManager != null) {
            Image image = imageManager.getImage(imageKey);
            if (DEBUG) {
                if (image != null) {
                    System.out.println("[core] ... image=" + image.getWidth(null) + "x" + image.getHeight(null));
                } else {
                    System.out.println("[core] ... image=null");
                }
            }
            return image;
        } else if (DEBUG) {
            System.out.println("[core] ... no ImageManager !!!");
        }
        return null;
    }

    //------------------------------------------------------------------------
    // RecommandationManager
    //------------------------------------------------------------------------
    @Override
    public void registerFriends(String email, Set<String> friends) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#registerFriends email=" + email
                    + ", friends=" + friends);
        }
        if (recommandationManager != null) {
            recommandationManager.registerFriends(email, friends);
            if (DEBUG) {
                System.out.println("[core] ... DONE");
            }
        } else if (DEBUG) {
            System.out.println("[core] ... no RecommandationManager !!!");
        }
    }

    @Override
    public void registerInterests(String email, Set<String> interests) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#registerInterests email=" + email
                    + ", interests=" + interests);
        }
        if (recommandationManager != null) {
            recommandationManager.registerInterests(email, interests);
            if (DEBUG) {
                System.out.println("[core] ... DONE");
            }
        } else if (DEBUG) {
            System.out.println("[core] ... no RecommandationManager !!!");
        }
    }

    @Override
    public Set<String> getFriends(String email) throws Exception {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#getFriends email=" + email);
        }
        if (recommandationManager != null) {
            Set<String> friends = recommandationManager.getFriends(email);
            if (DEBUG) {
                if (friends != null) {
                    System.out.println("[core] ... friends=" + friends);
                } else {
                    System.out.println("[core] ... friends=null");
                }
            }
            return friends;
        } else if (DEBUG) {
            System.out.println("[core] ... no RecommandationManager !!!");
        }
        return null;
    }

    //------------------------------------------------------------------------
    // Listeners
    //------------------------------------------------------------------------
    /**
     * Listener of profile events to display debug messages when events are
     * received
     */
    class ProfileEventListener implements EventListener<ProfileEvent> {

        @Override
        public void onEvent(ProfileEvent event) {
            displayEvent(event);
        }
    }

    /**
     * Listener of friend events to display debug messages when events are
     * received and manage the lists of known friends
     */
    class FriendEventListener implements EventListener<FriendEvent> {

        @Override
        public void onEvent(FriendEvent event) {
            displayEvent(event);
        }
    }

    /**
     * Listener of message events to display debug messages when events are
     * received
     */
    class MessageEventListener implements EventListener<MessageEvent> {

        @Override
        public void onEvent(MessageEvent event) {
            displayEvent(event);
        }
    }

    /**
     * Listener of recommendation events to display debug messages when events
     * are received
     */
    class RecommendationEventListener implements EventListener<RecommandationEvent> {

        @Override
        public void onEvent(RecommandationEvent event) {
            displayEvent(event);
        }
    }

    /**
     * Display the given event in DEBUG mode
     *
     * @param event an event
     */
    private void displayEvent(Object event) {
        if (DEBUG) {
            System.out.println("[core] SocialNetwork#onEvent " + event + " (" + event.getClass().getName() + ")");
        }
    }
}
