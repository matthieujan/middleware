package fr.ensibs.socialnetwork.swing.friend;

import fr.ensibs.socialnetwork.core.Message;
import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.swing.SocialNetworkFrame;
import static fr.ensibs.socialnetwork.swing.SocialNetworkFrame.DEBUG;
import fr.ensibs.socialnetwork.swing.message.MessageDialog;
import fr.ensibs.socialnetwork.swing.DefaultPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import static javax.swing.border.BevelBorder.RAISED;
import javax.swing.border.Border;

/**
 * A panel to display a list of friends, allowing to open dialogs for each
 * friend's private messages
 *
 * @author Pascale Launay
 */
public class FriendPanel extends DefaultPanel {

    private static final Border BEVEL_BORDER = BorderFactory.createBevelBorder(RAISED);
    private static final Color FOCUS_COLOR = new Color(204, 230, 255), DEFAULT_COLOR = Color.white;

    private static final String ADD_FRIEND_TEXT = "Add Friend";
    private static final String REQUEST_FRIEND_TEXT = "Request Friend";
    private static final String EMAIL_TEXT = "User's email";
    private static final String BAN_QUESTION_TEXT = "Really ban";

    private SocialNetworkFrame frame;
    private final FriendsModel friends; // contains friends listed on this panel
    private JList<Profile> friendsList;
    private final JButton addButton;
    private final Map<String, Profile> friendsMap;
    private final Map<String, Integer> messagesMap;

    /**
     * Constructor
     *
     * @param frame the parent frame
     */
    public FriendPanel(SocialNetworkFrame frame) {
        super(frame);
        this.frame = frame;
        this.friends = new FriendsModel();
        friendsMap = new HashMap<>();
        messagesMap = new HashMap<>();
        friendsList = new JList<>(friends);
        friendsList.setCellRenderer(new FriendCellRenderer());
        friendsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    int idx = friendsList.locationToIndex(event.getPoint());
                    if (idx >= 0) {
                        Profile friend = friends.elementAt(idx);
                        friendClicked(friend);
                    }
                }
            }
        });
        friendsList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent event) {
                if (event.getKeyChar() == KeyEvent.VK_DELETE) {
                    banFriend(friendsList.getSelectedValue());
                }
            }
        });
        friendsList.setFocusable(true);

        if (socialNetworkFrame.hasFriendManager()) {
            addButton = new JButton(new AbstractAction(REQUEST_FRIEND_TEXT) {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    requestFriend();
                }
            });
        } else {
            addButton = new JButton(new AbstractAction(ADD_FRIEND_TEXT) {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    addFriend();
                }
            });
        }
        addButton.setEnabled(false);

        fillPanel();
    }

    @Override
    public synchronized void close() {
        super.close();
        if (isClosed()) {
            friends.removeAllElements();
            messagesMap.clear();
            addButton.setEnabled(false);
        }
    }

    @Override
    public synchronized void open(Profile me) {
        super.open(me);
        if (!isClosed()) {
            addButton.setEnabled(true);
            Set<String> friends = socialNetworkFrame.getFriends();
            if (friends != null) {
                for (String email : friends) {
                    Profile profile = socialNetworkFrame.getProfile(email);
                    if (profile != null) {
                        addFriend(profile);
                    }
                }
            }
        }
    }

    /**
     * Add a friend in the list
     *
     * @param profile the new friend's profile
     */
    public void addFriend(Profile profile) {
        if (DEBUG) {
            System.out.println("[swing] FriendsPanel#addFriend profile=" + profile);
        }
        friends.addElement(profile);
        friendsMap.put(profile.getEmail(), profile);
        List<Message> messages = socialNetworkFrame.getChatMessages(me.getEmail(), profile.getEmail());
        messagesMap.put(profile.getEmail(), (messages == null ? 0 : messages.size()));
    }

    /**
     * Remove a friend from the list
     *
     * @param profile the friend to be removed
     * @return true if the friend has been removed
     */
    public boolean removeFriend(Profile profile) {
        if (DEBUG) {
            System.out.println("[swing] FriendsPanel#removeFriend profile=" + profile);
        }
        friendsMap.remove(profile.getEmail());
        return friends.removeElement(profile);
    }

    /**
     * Invoked when something has changed concerning a friend in the list
     *
     * @param friend the friend that has changed
     * @param nbMessages the number of messages exchanged with this friend
     */
    public void updateMessages(String friend, int nbMessages) {
        if (DEBUG) {
            System.out.println("[swing] FriendsPanel#updateMessages friend=" + friend);
        }
        messagesMap.put(friend, nbMessages);
        this.friends.updateFriend(friend);
    }

    /**
     * Invoked when something has changed concerning a friend in the list
     *
     * @param friend the friend that has changed
     */
    public void updateFriend(Profile friend) {
        if (DEBUG) {
            System.out.println("[swing] FriendsPanel#updateFriend friend=" + friend);
        }
        Profile profile = friendsMap.get(friend.getEmail());
        if (profile != null) {
            profile.setPseudo(friend.getPseudo());
            profile.setInterests(friend.getInterests());
            this.friends.updateFriend(profile);
        }
    }

    //-------------------------------------------------------------------------
    // Private methods
    //-------------------------------------------------------------------------
    /**
     * Invoked when a friend in the list is double clicked. Open the message
     * dialog
     *
     * @param friend the selected friend object
     */
    private void friendClicked(Profile friend) {
        List<Message> messages = socialNetworkFrame.getChatMessages(me.getEmail(), friend.getEmail());
        MessageDialog dialog = new MessageDialog(socialNetworkFrame, me, friend, messages);
        dialog.setVisible(true);
    }

    /**
     * Invoked when the request button is clicked
     */
    private void requestFriend() {
        String input = JOptionPane.showInputDialog(this, EMAIL_TEXT);
        if (input != null && !input.equals("") && !socialNetworkFrame.hasFriend(input)) {
            if (DEBUG) {
                System.out.println("[swing] FriendPanel#requestFriend friend=" + input);
            }
            socialNetworkFrame.requestFriend(input);
        }
    }

    /**
     * Invoked when the add button is clicked
     */
    private void addFriend() {
        String input = JOptionPane.showInputDialog(this, EMAIL_TEXT);
        if (input != null && !input.equals("") && !socialNetworkFrame.hasFriend(input)) {
            if (DEBUG) {
                System.out.println("[swing] FriendPanel#addFriend friend=" + input);
            }
            socialNetworkFrame.addFriend(input);
        }
    }

    /**
     * Invoked when the delete key is pressed and a friend is selected
     *
     * @param friend the selected friend
     */
    private void banFriend(Profile friend) {
        if (friends.contains(friend)) {
            int option = JOptionPane.showConfirmDialog(this, BAN_QUESTION_TEXT + " " + friend.getPseudo() + "?");
            if (option == JOptionPane.OK_OPTION) {
                if (DEBUG) {
                    System.out.println("[swing] FriendPanel#banFriend friend=" + friend.getEmail());
                }
                socialNetworkFrame.banFriend(friend.getEmail());
            }
        }
    }

    /**
     * Put components on the panel
     */
    private void fillPanel() {
        setLayout(new BorderLayout(5, 5));
        JScrollPane pane = new JScrollPane(friendsList);
        add(pane, BorderLayout.CENTER);
        add(makeComponentPanel(addButton), BorderLayout.SOUTH);
    }

    /**
     * The data model that manages the friends displayed in this panel
     */
    class FriendsModel extends DefaultListModel<Profile> {

        /**
         * Invoked when something has changed concerning a friend in the list
         *
         * @param friend the email address of thefriend that has changed
         */
        void updateFriend(String friend) {
            Profile profile = new Profile(friend, null);
            int idx = indexOf(profile);
            if (idx >= 0) {
                this.fireContentsChanged(this, idx, idx);
            }
        }

        /**
         * Invoked when something has changed concerning a friend in the list
         *
         * @param friend the friend that has changed
         */
        void updateFriend(Profile friend) {
            int idx = indexOf(friend);
            if (idx >= 0) {
                set(idx, friend);
                this.fireContentsChanged(this, idx, idx);
            }
        }
    }

    /**
     * The renderer to display the list's elements
     */
    class FriendCellRenderer extends JLabel implements ListCellRenderer<Profile> {

        /**
         * Constructor
         */
        public FriendCellRenderer() {
            setBorder(BEVEL_BORDER);
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Profile> list, Profile profile, int idx, boolean selected, boolean hasFocus) {
            Integer nbMessages = messagesMap.get(profile.getEmail());
            int nb = (nbMessages == null ? 0 : nbMessages);
            setText(profile.getPseudo() + " (" + nb + " message" + (nb > 1 ? "s" : "") + ")");
            if (selected) {
                setBackground(FOCUS_COLOR);
            } else {
                setBackground(DEFAULT_COLOR);
            }
            String text = profile.toString();
            setToolTipText(text.substring(1, text.length() - 1));
            return this;
        }
    }
}
