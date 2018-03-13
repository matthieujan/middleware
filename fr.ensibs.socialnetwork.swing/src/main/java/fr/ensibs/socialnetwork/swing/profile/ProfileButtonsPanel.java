package fr.ensibs.socialnetwork.swing.profile;

import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.swing.SocialNetworkFrame;
import fr.ensibs.socialnetwork.swing.DefaultPanel;
import static fr.ensibs.socialnetwork.swing.SocialNetworkFrame.DEBUG;
import static fr.ensibs.socialnetwork.swing.profile.ProfilePanel.LOGIN;
import static fr.ensibs.socialnetwork.swing.profile.ProfilePanel.PROFILE;
import static fr.ensibs.socialnetwork.swing.profile.ProfilePanel.SIGNUP;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;

/**
 * A panel with buttons to open the dialogs related to the user's profile
 *
 * @author Pascale Launay
 */
public class ProfileButtonsPanel extends DefaultPanel {

    private static final String LOGIN_TEXT = "Log In";
    private static final String LOGOUT_TEXT = "Log Out";
    private static final String SIGNUP_TEXT = "Sign Up";
    private static final String PROFILE_TEXT = "Profile";

    private final JButton loginButton, signupButton, profileButton;

    /**
     * Constructor
     *
     * @param frame the parent frame
     */
    public ProfileButtonsPanel(SocialNetworkFrame frame) {
        super(frame);
        // make buttons
        loginButton = new JButton(new AbstractAction(LOGIN_TEXT) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String text = loginButton.getText();
                if (text.equals(LOGIN_TEXT)) {
                    ProfileDialog dialog = new ProfileDialog(socialNetworkFrame, text, LOGIN);
                    dialog.setVisible(true);
                } else {
                    logout(me);
                }
            }
        });
        signupButton = new JButton(new AbstractAction(SIGNUP_TEXT) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ProfileDialog dialog = new ProfileDialog(socialNetworkFrame, SIGNUP_TEXT, SIGNUP);
                dialog.setVisible(true);
            }
        });
        profileButton = new JButton(new AbstractAction(PROFILE_TEXT) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ProfileDialog dialog = new ProfileDialog(socialNetworkFrame, PROFILE_TEXT, PROFILE);
                dialog.setProfile(socialNetworkFrame.getProfile());
                dialog.setVisible(true);
            }
        });

        profileButton.setEnabled(false);
        fillPanel();
    }

    @Override
    public synchronized void close() {
        super.close();
        if (isClosed()) {
            loginButton.setText(LOGIN_TEXT);
            profileButton.setEnabled(false);
            signupButton.setEnabled(true);
        }
    }

    @Override
    public synchronized void open(Profile me) {
        super.open(me);
        if (!isClosed()) {
            loginButton.setText(LOGOUT_TEXT);
            profileButton.setEnabled(true);
            signupButton.setEnabled(false);
        }
    }

    /**
     * Invoked when the logout button is licked
     *
     * @param profile the user's profile
     */
    private void logout(Profile profile) {
        if (profile != null) {
            if (DEBUG) {
                System.out.println("[swing] ProfileDialog#logout");
            }
            socialNetworkFrame.logOut();
        }
    }

    /**
     * Put buttons on the buttons panel
     */
    private void fillPanel() {
        add(signupButton);
        add(loginButton);
        add(profileButton);
    }
}
