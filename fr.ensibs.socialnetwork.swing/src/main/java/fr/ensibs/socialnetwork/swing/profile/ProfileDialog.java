package fr.ensibs.socialnetwork.swing.profile;

import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.swing.SocialNetworkFrame;
import fr.ensibs.socialnetwork.swing.DefaultDialog;
import static fr.ensibs.socialnetwork.swing.SocialNetworkFrame.DEBUG;
import static fr.ensibs.socialnetwork.swing.profile.ProfilePanel.LOGIN;
import static fr.ensibs.socialnetwork.swing.profile.ProfilePanel.PROFILE;
import static fr.ensibs.socialnetwork.swing.profile.ProfilePanel.SIGNUP;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A panel with fields allowing the user to log in or sign up
 *
 * @author Pascale Launay
 */
public class ProfileDialog extends DefaultDialog {

    private static final int RIGHT_FIELDS = 0, MISSING_FIELDS = 1, WRONG_FIELDS = 2, NOT_ALLOWED = 3;
    private static final String OK_TEXT = "OK";
    private static final String CANCEL_TEXT = "Cancel";
    private static final String MISSING_ERROR_TEXT = "Some values are missing";
    private static final String PASSWORD_ERROR_TEXT = "Passwords do not match";
    private static final String PERMISSION_ERROR_TEXT = "The user is not allowed";

    private Profile profile; // a profile used to initialise the panel's types

    private final int type; // LOGIN|SIGNUP|PROFILE
    private final ProfilePanel profilePanel; // the panel that contains the profile's fields
    private final JLabel messageLabel; // a red message to display errors
    private final JButton cancelButton; // button used to close the dialog without doing anything
    private final JButton okButton; // button used to validate the dialog and close

    /**
     * Constructor
     *
     * @param frame the parent frame
     * @param title the dialog's title
     * @param type the profile panel's type
     */
    public ProfileDialog(SocialNetworkFrame frame, String title, int type) {
        super(frame, title, true);
        this.type = type;
        profilePanel = new ProfilePanel(this, type);
        messageLabel = new JLabel();
        messageLabel.setBackground(Color.white);
        messageLabel.setForeground(Color.red);
        cancelButton = new JButton(new AbstractAction(CANCEL_TEXT) {
            @Override
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
            }
        });
        okButton = new JButton(new AbstractAction(OK_TEXT) {
            @Override
            public void actionPerformed(ActionEvent event) {
                ok();
            }
        });

        fillPanel();
        pack();
        setLocationRelativeTo(socialNetworkFrame);
    }

    /**
     * Set a profile to initialise the dialog's fields
     *
     * @param profile the profile to initialise the dialog's fields
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
        profilePanel.setProfile(profile);
    }

    @Override
    public void setVisible(boolean visible) {
        if (!visible) {
            profilePanel.reset();
            messageLabel.setText("");
        }
        super.setVisible(visible);
    }

    /**
     * OK button clicked
     */
    private void ok() {
        int ret = checkFields();
        switch (ret) {
            case RIGHT_FIELDS:
                setVisible(false);
                break;
            case MISSING_FIELDS:
                messageLabel.setText(MISSING_ERROR_TEXT);
                break;
            case WRONG_FIELDS:
                messageLabel.setText(PASSWORD_ERROR_TEXT);
                break;
            case NOT_ALLOWED:
                messageLabel.setText(PERMISSION_ERROR_TEXT);
                break;
        }
    }

    /**
     * When OK button is clicked, check the fields and invoke, if possible, the
     * profile manager
     *
     * @return RIGHT_FIELDS|MISSING_FIELDS|WRONG_FIELDS|NOT_ALLOWED
     */
    private int checkFields() {
        String email = profilePanel.getEmail();
        String password = profilePanel.getPassword();
        String password2 = profilePanel.getPassword2();
        String pseudo = profilePanel.getPseudo();
        Set<String> interests = profilePanel.getInterests();
        switch (type) {
            case LOGIN:
                return checkLogin(email, password);
            case SIGNUP:
                return checkSignUp(email, pseudo, password, password2);
            case PROFILE:
                return checkProfile(email, pseudo, interests);
        }
        return RIGHT_FIELDS;
    }

    /**
     * Check the fields for the SIGNUP panel's type
     *
     * @param email the email's field value
     * @param pseudo the pseudo's field value
     * @param password the password's field value
     * @param password2 the second password's field value
     * @return RIGHT_FIELDS|MISSING_FIELDS|WRONG_FIELDS|NOT_ALLOWED
     */
    private int checkSignUp(String email, String pseudo, String password, String password2) {
        if (email != null && pseudo != null && password != null && password2 != null) {
            if (!password.equals(password2)) {
                return WRONG_FIELDS;
            } else {
                if (DEBUG) {
                    System.out.println("[swing] ProfileDialog#signUp email=" + email
                            + ", pseudo=" + pseudo
                            + ", password=" + password);
                }
                Profile profile = socialNetworkFrame.signUp(email, pseudo, password);
                if (profile == null) {
                    return NOT_ALLOWED;
                }
                return RIGHT_FIELDS;
            }
        }
        return MISSING_FIELDS;
    }

    /**
     * Check the fields for the LOGIN panel's type
     *
     * @param email the email's field value
     * @param password the password's field value
     * @return RIGHT_FIELDS|MISSING_FIELDS|NOT_ALLOWED
     */
    private int checkLogin(String email, String password) {
        if (email != null && password != null) {
            if (DEBUG) {
                System.out.println("[swing] ProfileDialog#logIn email=" + email
                        + ", password=" + password);
            }
            Profile profile = socialNetworkFrame.logIn(email, password);
            if (profile == null) {
                return NOT_ALLOWED;
            }
            return RIGHT_FIELDS;
        }
        return MISSING_FIELDS;
    }

    /**
     * Check the fields for the PROFILE panel's type
     *
     * @param email the email's field value
     * @param pseudo the pseudo's field value
     * @return RIGHT_FIELDS|MISSING_FIELDS|NOT_ALLOWED
     */
    private int checkProfile(String email, String pseudo, Set<String> interests) {
        if (email != null && pseudo != null) {
            if (profile != null && interests != null) {
                boolean pseudoChanged = !pseudo.equals(profile.getPseudo());
                boolean interestsChanged = !equals(interests, profile.getInterests());
                if (pseudoChanged || interestsChanged) {
                    Profile updated = new Profile(email, pseudo);
                    updated.setInterests(interests);
                    if (DEBUG) {
                        System.out.println("[swing] ProfileDialog#updateProfile profile=" + updated);
                    }
                    boolean changed = socialNetworkFrame.updateProfile(updated, pseudoChanged, interestsChanged);
                    if (!changed) {
                        return NOT_ALLOWED;
                    }
                    profile.setPseudo(pseudo);
                    profile.setInterests(interests);
                }
            }
            return RIGHT_FIELDS;
        }
        return MISSING_FIELDS;
    }

    /**
     * Return true if the sets have the same elements
     *
     * @param set1 a set
     * @param set2 another set
     * @return true if the sets have the same elements
     */
    private boolean equals(Set<?> set1, Set<?> set2) {
        return set1.size() == set2.size() && set1.containsAll(set2);
    }

    /**
     * Put fields and buttons on the panel
     */
    private void fillPanel() {
        setLayout(new BorderLayout());
        add(profilePanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.white);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(okButton);
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(Color.white);
        messagePanel.add(messageLabel);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.white);
        southPanel.add(buttonsPanel, BorderLayout.CENTER);
        southPanel.add(messagePanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
    }
}
