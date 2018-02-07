package fr.ensibs.socialnetwork.swing;

import fr.ensibs.socialnetwork.core.Message;
import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.core.Publication;
import fr.ensibs.socialnetwork.logic.SocialNetwork;
import fr.ensibs.socialnetwork.logic.friend.FriendEvent;
import fr.ensibs.socialnetwork.logic.recommandation.RecommandationEvent;
import fr.ensibs.socialnetwork.swing.friend.FriendEventListener;
import fr.ensibs.socialnetwork.swing.friend.FriendPanel;
import fr.ensibs.socialnetwork.swing.friend.Friends;
import fr.ensibs.socialnetwork.swing.image.ImageFactory;
import fr.ensibs.socialnetwork.swing.image.ImagePanel;
import fr.ensibs.socialnetwork.swing.image.Images;
import fr.ensibs.socialnetwork.swing.message.MessageEventListener;
import fr.ensibs.socialnetwork.swing.message.Messages;
import fr.ensibs.socialnetwork.swing.message.WallPanel;
import fr.ensibs.socialnetwork.swing.notification.NotificationPanel;
import fr.ensibs.socialnetwork.swing.notification.RecommandationEventListener;
import fr.ensibs.socialnetwork.swing.profile.ProfileButtonsPanel;
import fr.ensibs.socialnetwork.swing.profile.ProfileEventListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

/**
 * The social network main frame composed of the profile, wall, notification,
 * friend and image panels
 *
 * @author Pascale Launay
 */
public class SocialNetworkFrame extends JFrame {

    public static boolean DEBUG; // if true, print debug messages

    private final SocialNetwork socialNetwork;
    private Profile me;
    private final Messages messages;
    private final Images images;
    private final Friends friends;
    private final Map<String, Profile> users;
    private String token; // the session token

    // the application's panels
    private final ProfileButtonsPanel profileButtonsPanel;
    private final WallPanel wallPanel;
    private final NotificationPanel notificationPanel;
    private final FriendPanel friendsPanel;
    private final ImagePanel imagePanel;

    // the event listeners
    private final RecommandationEventListener recommendationEventListener;
    private final FriendEventListener friendEventListener;
    private final ProfileEventListener profileEventListener;
    private final MessageEventListener messageEventListener;

    //------------------------------------------------------------------------
    // Open/close user's profile
    //------------------------------------------------------------------------
    /**
     * Registers a new user, provided that no other user exists with the same
     * email address.
     *
     * @param email the new user's email
     * @param pseudo the new user's pseudo
     * @param password the new user's password
     * @return the user's profile if he has been registered or else null
     */
    public Profile signUp(String email, String pseudo, String password) {
        try {
            Profile profile = socialNetwork.signUp(email, pseudo, password);
            if (profile != null) {
                String token = socialNetwork.logIn(email, password);
                if (token != null) {
                    setProfile(profile, token);
                    return profile;
                }
            }
        } catch (Exception ex) {
            showError(ex);
        }
        return null;
    }

    /**
     * Checks whether a user is allowed to log in.
     *
     * @param email the user's email
     * @param password the user's pseudo
     * @return a token identifying the user's session
     */
    public Profile logIn(String email, String password) {
        try {
            String token = socialNetwork.logIn(email, password);
            if (token != null) {
                Profile profile = socialNetwork.getProfile(email);
                if (profile != null) {
                    setProfile(profile, token);
                    return profile;
                }
            }
        } catch (Exception ex) {
            showError(ex);
        }
        return null;
    }

    /**
     * Log out if the current user is logged in
     */
    public void logOut() {
        try {
            if (socialNetwork.logOut(token)) {
                removeProfile();
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    /**
     * Initialise the user's profile
     *
     * @param me the user's profile
     * @param token the session token
     */
    private void setProfile(Profile me, String token) {
        this.token = token;
        this.me = me;
        updateTitle();

        try {
            String myEmail = me.getEmail();
            Set<String> myFriends = socialNetwork.getFriends(me.getEmail());
            if (myFriends != null) {
                friends.setFriends(myEmail, myFriends);
                for (String friend : myFriends) {
                    socialNetwork.subscribe(myEmail, friend);
                }
            }
        } catch (Exception ex) {
            showError(ex);
        }

        wallPanel.open(me);
        friendsPanel.open(me);
        profileButtonsPanel.open(me);
        notificationPanel.open(me);

        recommendationEventListener.open(me);
        friendEventListener.open(me);
        profileEventListener.open(me);
        messageEventListener.open(me);
    }

    /**
     * Close the current user's profile
     */
    private void removeProfile() {
        try {
            String myEmail = me.getEmail();
            Set<String> myFriends = friends.getFriends(myEmail);
            if (myFriends != null) {
                for (String friend : myFriends) {
                    socialNetwork.unsubscribe(myEmail, friend);
                }
            }
        } catch (Exception ex) {
            showError(ex);
        }

        this.token = null;
        this.me = null;
        updateTitle();

        wallPanel.close();
        friendsPanel.close();
        profileButtonsPanel.close();
        notificationPanel.close();

        recommendationEventListener.close();
        friendEventListener.close();
        profileEventListener.close();
        messageEventListener.close();
    }

    /**
     * Update the connected user's profile
     *
     * @param profile the new profile
     * @param pseudo true if the pseudo has changed
     * @param interests true if the interests have changed
     * @return true if the profile has been changed
     */
    public boolean updateProfile(Profile profile, boolean pseudo, boolean interests) {
        try {
            if (profile.getEmail().equals(me.getEmail()) && socialNetwork.updateProfile(token, profile)) {
                this.me = profile;
                if (pseudo) {
                    updateTitle();
                }
                if (interests) {
                    socialNetwork.registerInterests(profile.getEmail(), profile.getInterests());
                }
                return true;
            }
        } catch (Exception ex) {
            showError(ex);
        }
        return false;
    }

    /**
     * Update a user's profile
     *
     * @param profile the user's profile
     * @return true if the profile has been changed
     */
    public boolean updateProfile(Profile profile) {
        if (!profile.getEmail().equals(me.getEmail())) {
            users.put(profile.getEmail(), profile);
            if (hasFriend(profile.getEmail())) {
                friendsPanel.updateFriend(profile);
                return true;
            }
        }
        return false;
    }

    /**
     * Give the user's profile
     *
     * @return the user's profile
     */
    public Profile getProfile() {
        return this.me;
    }

    //------------------------------------------------------------------------
    // FriendsPanel
    //------------------------------------------------------------------------
    /**
     * Check whether the friends manager exists
     *
     * @return true if the friends manager exists
     */
    public boolean hasFriendManager() {
        return socialNetwork.hasFriendManager();
    }

    /**
     * Remove frienship link between two friends
     *
     * @param email the email address of the friend
     */
    public void banFriend(String email) {
        try {
            socialNetwork.banFriend(me.getEmail(), email);
            removeFriend(email);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    /**
     * Accept friendship
     *
     * @param email the email address of the friend
     */
    public void acceptFriend(String email) {
        try {
            socialNetwork.acceptFriend(me.getEmail(), email);
            addFriend(email);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    /**
     * Request for a user to become a friend
     *
     * @param email the email address of the user that is asked for friendship
     */
    public void requestFriend(String email) {
        try {
            socialNetwork.requestFriend(me.getEmail(), email);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    /**
     * Check whether a friend is in the friend's list
     *
     * @param email an email address
     * @return true if a friend with this address is in the list
     */
    public boolean hasFriend(String email) {
        return this.friends.areFriends(me.getEmail(), email);
    }

    /**
     * Add a friend in the list
     *
     * @param email the new friend's email
     * @return true if the friend has been added
     */
    public boolean addFriend(String email) {
        try {
            if (!hasFriend(email)) {
                Profile profile = getProfile(email);
                if (profile != null && this.friends.addFriendship(me.getEmail(), email)) {
                    this.friendsPanel.addFriend(profile);
                    this.users.put(email, profile);
                    this.socialNetwork.subscribe(me.getEmail(), email);
                    this.socialNetwork.registerFriends(me.getEmail(), this.friends.getFriends(me.getEmail()));
                    this.notificationPanel.removeNotifications(email);
                    return true;
                }
            }
        } catch (Exception ex) {
            showError(ex);
        }
        return false;
    }

    /**
     * Remove a friend from the list
     *
     * @param email the friend's email
     * @return true if the friend has been removed
     */
    public boolean removeFriend(String email) {
        try {
            if (hasFriend(email)) {
                Profile profile = users.get(email);
                if (profile != null) {
                    this.friendsPanel.removeFriend(profile);
                    this.friends.removeFriendship(me.getEmail(), email);
                    this.socialNetwork.unsubscribe(me.getEmail(), email);
                    this.socialNetwork.registerFriends(me.getEmail(), this.friends.getFriends(me.getEmail()));
                    return true;
                }
            }
        } catch (Exception ex) {
            showError(ex);
        }
        return false;
    }

    /**
     * Give the user's friends
     *
     * @return the user's friends emails
     */
    public Set<String> getFriends() {
        return this.friends.getFriends(me.getEmail());
    }

    /**
     * Give the profile of a known user
     *
     * @param email the user's email
     * @return the profile
     */
    public Profile getProfile(String email) {
        Profile profile = users.get(email);
        if (profile == null) {
            try {
                profile = socialNetwork.getProfile(email);
                users.put(email, profile);
            } catch (Exception ex) {
                showError(ex);
            }
        }
        return profile;
    }

    //------------------------------------------------------------------------
    // NotificationPanel
    //------------------------------------------------------------------------
    /**
     * Add a notification related to a request of another user to be my friend
     *
     * @param event the friend event (REQUEST)
     */
    public void addFriendRequest(FriendEvent event) {
        if (!hasFriend(event.getSource())) {
            notificationPanel.addFriendRequest(event);
        }
    }

    /**
     * Add a notification related to a recommendation
     *
     * @param event the recommendation event
     */
    public void addRecommandation(RecommandationEvent event) {
        notificationPanel.addRecommandation(event);
    }

    //------------------------------------------------------------------------
    // Messages
    //------------------------------------------------------------------------
    /**
     * Send or publish the given message and adds it
     *
     * @param message a chat or publication message
     */
    public void sendMessage(Message message) {
        try {
            if (message instanceof Publication) {
                socialNetwork.publish((Publication) message);
            } else {
                socialNetwork.send(message);
            }
            addMessage(message);
        } catch (Exception e) {
            showError(e);
        }
    }

    /**
     * Add a chat or publication message
     *
     * @param message a chat or publication message
     */
    public void addMessage(Message message) {
        messages.addMessage(message);
        if (message instanceof Publication) {
            wallPanel.addMessage((Publication) message);
        } else {
            String friend = message.getSource();
            if (friend.equals(me.getEmail())) {
                friend = message.getTarget();
            }
            int nbMessages = messages.getChatMessages(message.getSource(), message.getTarget()).size();
            friendsPanel.updateMessages(friend, nbMessages);
        }
    }

    /**
     * Give all the chat messages exchanged between two given users
     *
     * @param user1 a user's email address
     * @param user2 another user's email address
     * @return all the chat messages exchanged between the two users
     */
    public List<Message> getChatMessages(String user1, String user2) {
        return messages.getChatMessages(user1, user2);
    }

    /**
     * Give all the publication messages published by a given user
     *
     * @param user a user's email address
     * @return all the publication messages published by the user
     */
    public List<Publication> getPublications(String user) {
        return messages.getPublications(user);
    }

    //------------------------------------------------------------------------
    // Images
    //------------------------------------------------------------------------
    /**
     * Add an image
     *
     * @param image the image
     * @return the key associated to the image
     */
    public String addImage(Image image) {
        try {
            String imageKey = socialNetwork.addImage(image);
            if (imageKey != null) {
                images.put(imageKey, image);
            }
            return imageKey;
        } catch (Exception ex) {
            showError(ex);
        }
        return null;
    }

    /**
     * Give the image associated to the given key
     *
     * @param key a key
     * @return the image associated to the key
     */
    public Image getImage(String key) {
        Image image = images.get(key);
        if (image == null) {
            try {
                image = socialNetwork.getImage(key);
                if (image != null) {
                    images.put(key, image);
                }
            } catch (Exception ex) {
                showError(ex);
            }
        }
        return image;
    }

    //------------------------------------------------------------------------
    // Friends
    //------------------------------------------------------------------------
    /**
     * Give the friends of a given user
     *
     * @param user a user
     * @return the user's friends
     */
    public Set<String> getFriends(String user) {
        return friends.getFriends(user);
    }

    //------------------------------------------------------------------------
    // Private methods
    //------------------------------------------------------------------------
    /**
     * Create the application's panel
     *
     * @param socialNetwork
     */
    public SocialNetworkFrame(SocialNetwork socialNetwork) {
        super("Social Network");
        this.socialNetwork = socialNetwork;
        setPreferredSize(new Dimension(800, 800));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        System.setProperty("user.language", "en");
        System.setProperty("user.country", "US");

        // close all resources when closing the frame
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        // Look & feel: white backgrounds
        UIManager.put("OptionPane.background", Color.white);
        UIManager.put("Panel.background", Color.white);
        UIManager.put("Dialog.background", Color.white);

        // make the application's panels
        InputStream in = SocialNetworkFrame.class.getResourceAsStream("/images/logo-ensibs.jpg");
        imagePanel = new ImagePanel(in);
        profileButtonsPanel = new ProfileButtonsPanel(this);
        wallPanel = new WallPanel(this);
        friendsPanel = new FriendPanel(this);
        notificationPanel = new NotificationPanel(this);

        fillPanel();

        // make the listeners
        recommendationEventListener = new RecommandationEventListener(this);
        friendEventListener = new FriendEventListener(this);
        profileEventListener = new ProfileEventListener(this);
        messageEventListener = new MessageEventListener(this);

        messages = new Messages();
        images = new Images();
        friends = new Friends();
        users = new HashMap<>();

        this.pack();
        this.setLocationByPlatform(true);
    }

    /**
     * Update the frame title
     */
    private void updateTitle() {
        if (me != null) {
            setTitle("Social Network -- " + me.getPseudo());
        } else {
            setTitle("Social Network");
        }
    }

    /**
     * Put application's panels on the main panel
     */
    private void fillPanel() {
        // north: image & profile
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(profileButtonsPanel, BorderLayout.EAST);
        northPanel.add(imagePanel, BorderLayout.CENTER);

        // east: chat & notifications
        JSplitPane eastPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, friendsPanel, notificationPanel);
        eastPane.setResizeWeight(.5);

        // center: wall & east
        JSplitPane centerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, wallPanel, eastPane);
        centerPane.setResizeWeight(.67);

        // main
        JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, northPanel, centerPane);
        mainPane.setResizeWeight(.15);
        add(mainPane);

        // icon
        InputStream in = SocialNetworkFrame.class.getResourceAsStream("/images/small-logo-ensibs.jpg");
        Image icon = ImageFactory.getInstance().makeImage(in);
        if (icon != null) {
            setIconImage(icon);
        }
    }

    /**
     * Close all application's panels
     */
    private synchronized void close() {
        if (DEBUG) {
            System.out.println("[swing] SocialNetworkFrame#close");
        }
        if (token != null) {
            logOut();
        }
        try {
            socialNetwork.close();
        } catch (IOException ex) {
            // showError(ex);
        }
    }

    /**
     * Show an error message in a popup
     *
     * @param e the error
     */
    private void showError(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "[ERROR]" + e.getClass().getName() + ": " + e.getMessage());
    }
}
