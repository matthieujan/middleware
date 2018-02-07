package fr.ensibs.socialnetwork.swing.notification;

import fr.ensibs.socialnetwork.swing.DefaultPanel;
import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.logic.friend.FriendEvent;
import fr.ensibs.socialnetwork.logic.recommandation.RecommandationEvent;
import static fr.ensibs.socialnetwork.logic.recommandation.RecommandationEvent.COMMON_FRIENDS;
import fr.ensibs.socialnetwork.swing.SocialNetworkFrame;
import static fr.ensibs.socialnetwork.swing.SocialNetworkFrame.DEBUG;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.CANCEL_OPTION;
import static javax.swing.JOptionPane.NO_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import static javax.swing.border.BevelBorder.RAISED;
import javax.swing.border.Border;

/**
 * A panel to display the notifications to the user and allow him to answer
 * them.
 *
 * @author Pascale Launay
 */
public class NotificationPanel extends DefaultPanel {

    private static final Border BEVEL_BORDER = BorderFactory.createBevelBorder(RAISED);
    private static final Color FOCUS_COLOR = new Color(204, 230, 255), DEFAULT_COLOR = Color.white;
    private static final String ACCEPT_QUESTION_TEXT = "Accept?";
    private static final String REQUEST_QUESTION_TEXT = "Request?";
    private static final String REQUEST_FRIEND_TEXT = "wants to be your friend";
    private static final String RECO_INTEREST_TEXT = "You have common interests with";
    private static final String RECO_FRIEND_TEXT = "You have common friends with";

    private JList<Notification> notificationsList;
    private DefaultListModel<Notification> notifications;

    /**
     * Constructor
     *
     * @param frame the parent frame
     */
    public NotificationPanel(SocialNetworkFrame frame) {
        super(frame);
        notifications = new DefaultListModel<>();
        notificationsList = new JList<>(notifications);
        notificationsList.setCellRenderer(new NotificationRenderer());
        notificationsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    int idx = notificationsList.locationToIndex(event.getPoint());
                    if (idx >= 0) {
                        notificationClicked(notifications.elementAt(idx));
                    }
                }
            }

        });
        fillPanel();
    }

    /**
     * Add a notification related to a request of another user to be my friend
     *
     * @param event the friend event (REQUEST)
     */
    public void addFriendRequest(FriendEvent event) {
        Notification notification = new Notification(event);
        if (!notifications.contains(notification)) {
            notifications.addElement(notification);
        }
    }

    /**
     * Add a notification related to a recommendation
     *
     * @param event the recommendation event
     */
    public void addRecommandation(RecommandationEvent event) {
        Notification notification = new Notification(event);
        if (!notifications.contains(notification)) {
            notifications.addElement(notification);
        }
    }

    /**
     * Remove all the notifications related to the given friend
     *
     * @param friend a friend
     */
    public void removeNotifications(String friend) {
        int i = 0;
        while (i < notifications.size()) {
            Notification notification = notifications.getElementAt(i);
            if (notification.email.equals(friend)) {
                notifications.removeElementAt(i);
            } else {
                i++;
            }
        }
    }

    @Override
    public synchronized void close() {
        super.close();
        if (isClosed()) {
            notifications.removeAllElements();
        }
    }

    /**
     * Called when the user double clicks on a notification in the list
     *
     * @param notification the selected notification
     */
    private void notificationClicked(Notification notification) {
        if (notification != null) {
            boolean processed = notification.process();
            if (processed) {
                notifications.removeElement(notification);
            }
        }
    }

    /**
     * Put components on the panel
     */
    private void fillPanel() {
        setLayout(new BorderLayout(5, 5));
        JScrollPane pane = new JScrollPane(notificationsList);
        add(pane, BorderLayout.CENTER);
    }

    /**
     * Notification related to an event
     */
    class Notification {

        private FriendEvent friendEvent; // a friend event (REQUEST)
        private RecommandationEvent recommendationEvent;
        private String email;
        private final String pseudo;

        /**
         * Constructor
         *
         * @param event a friend event (REQUEST)
         */
        public Notification(FriendEvent event) {
            this.friendEvent = event;
            email = friendEvent.getSource();
            Profile profile = socialNetworkFrame.getProfile(email);
            pseudo = (profile == null ? email : profile.getPseudo());
        }

        /**
         * Constructor
         *
         * @param event a recommendation event
         */
        public Notification(RecommandationEvent event) {
            this.recommendationEvent = event;
            email = recommendationEvent.getFriend1();
            if (email.equals(me.getEmail())) {
                email = recommendationEvent.getFriend2();
            }
            Profile profile = socialNetworkFrame.getProfile(email);
            pseudo = (profile == null ? email : profile.getPseudo());
        }

        /**
         * Process the notification according to the event it is related to
         */
        boolean process() {
            int option = CANCEL_OPTION;
            if (friendEvent != null) {
                option = JOptionPane.showConfirmDialog(notificationsList, this + ". " + ACCEPT_QUESTION_TEXT);
                if (option == JOptionPane.OK_OPTION) {
                    if (DEBUG) {
                        System.out.println("[swing] NotificationPanel#acceptFriend friend=" + email);
                    }
                    socialNetworkFrame.acceptFriend(email);
                }
            } else if (recommendationEvent != null) {
                option = JOptionPane.showConfirmDialog(notificationsList, this + ". " + REQUEST_QUESTION_TEXT);
                if (option == JOptionPane.OK_OPTION) {
                    if (DEBUG) {
                        System.out.println("[swing] NotificationPanel#requestFriend friend=" + email);
                    }
                    socialNetworkFrame.requestFriend(email);
                }
            }
            return (option == OK_OPTION) || (option == NO_OPTION);
        }

        @Override
        public String toString() {
            if (friendEvent != null) {
                return pseudo + " " + REQUEST_FRIEND_TEXT;
            } else if (recommendationEvent != null) {
                if (recommendationEvent.getType() == COMMON_FRIENDS) {
                    return RECO_FRIEND_TEXT + " " + pseudo;
                } else {
                    return RECO_INTEREST_TEXT + " " + pseudo;
                }
            }
            return "NULL";
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Notification) {
                Notification other = (Notification) o;
                if ((friendEvent != null && other.friendEvent != null)
                        || (recommendationEvent != null && other.recommendationEvent != null)) {
                    return hashCode() == o.hashCode();
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            if (friendEvent != null) {
                return friendEvent.hashCode();
            } else if (recommendationEvent != null) {
                return recommendationEvent.hashCode();
            }
            return super.hashCode();
        }
    }

    /**
     * To display the notifications in the list
     */
    class NotificationRenderer extends JLabel implements ListCellRenderer<Notification> {

        /**
         * Constructor
         */
        public NotificationRenderer() {
            setBorder(BEVEL_BORDER);
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Notification> list, Notification notification, int idx, boolean selected, boolean hasFocus) {
            setText(notification.toString());
            if (selected) {
                setBackground(FOCUS_COLOR);
            } else {
                setBackground(DEFAULT_COLOR);
            }
            return this;
        }

    }
}
