package fr.ensibs.socialnetwork.swing.profile;

import fr.ensibs.socialnetwork.core.Profile;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import static javax.swing.border.BevelBorder.LOWERED;
import javax.swing.border.Border;

/**
 * A panel that contains fields related to a profile. The fields are displayed
 * and activated according to the application needs described by a type:
 * SIGNUP|LOGIN|PROFILE
 *
 * @author Pascale Launay
 */
public class ProfilePanel extends JPanel {

    public static final int LOGIN = 1, SIGNUP = 2, PROFILE = 3;
    private static final Border BEVEL_BORDER = BorderFactory.createBevelBorder(LOWERED);
    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    private static final String INTERESTS_TEXT = "Your interests";
    private static final String INTEREST_TEXT = "Your new interest";
    private static final String ADD_INTEREST_TEXT = "Add interest";
    private static final String PSEUDO_TEXT = "Pseudo";
    private static final String EMAIL_TEXT = "Email";
    private static final String PASSWORD_TEXT = "Password";

    private final JDialog parent; // the dialog that contains this panel
    private final JTextField emailField; // the text field to input the email
    private JTextField pseudoField; // the text field to input the pseudo
    private JTextField passwordField; // the text field to input the password
    private JTextField passwordField2; // the text field to input the second password
    private DefaultListModel<String> interests; // contains the user's interests (raw texts)
    private JList<String> interestList; // the list to display the user's interests
    private JButton addButton; // the button to add a user's interest

    /**
     * Constructor
     *
     * @param parent the dialog that contains this panel
     * @param type the dialog's type (LOGIN|SIGNUP|PROFILE)
     */
    public ProfilePanel(JDialog parent, int type) {
        this.parent = parent;
        setBackground(Color.white);
        emailField = makeTextField();
        if (type != LOGIN && type != SIGNUP) {
            emailField.setEnabled(false);
        }
        if (type == LOGIN || type == SIGNUP) {
            passwordField = makePasswordField();
        }
        if (type != LOGIN) { // add a pseudo and a second password field
            pseudoField = makeTextField();
        }
        if (type == SIGNUP) { // add a pseudo and a second password field
            passwordField2 = makePasswordField();
        }
        if (type == PROFILE) {
            interests = new DefaultListModel<>();
            interestList = new JList<>(interests);
            interestList.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent event) {
                    if (event.getKeyChar() == KeyEvent.VK_DELETE) {
                        removeInterest(interestList.getSelectedValue());
                    }
                }
            });
            interestList.setFocusable(true);
            addButton = new JButton(new AbstractAction(ADD_INTEREST_TEXT) {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    addInterest();
                }
            });
        }
        fillPanel();
    }

    /**
     * Reset all the panel's fields
     */
    public void reset() {
        emailField.setText("");
        if (passwordField != null) {
            passwordField.setText("");
        }
        if (pseudoField != null) {
            pseudoField.setText("");
        }
        if (interests != null) {
            interests.clear();
        }
    }

    /**
     * Initialize the dialog's fields with the given profile
     *
     * @param profile a user's profile
     */
    public void setProfile(Profile profile) {
        emailField.setText(profile.getEmail());
        if (pseudoField != null) {
            pseudoField.setText(profile.getPseudo());
        }
        if (interests != null) {
            for (String interest : profile.getInterests()) {
                interests.addElement(interest);
            }
        }
    }

    /**
     * Give the password's field value
     *
     * @return the password's field value
     */
    public String getPassword() {
        return getValue(passwordField);
    }

    /**
     * Give the second password's field value
     *
     * @return the password's field value
     */
    public String getPassword2() {
        return getValue(passwordField2);
    }

    /**
     * Give the email's field value
     *
     * @return the email's field value
     */
    public String getEmail() {
        return getValue(emailField);
    }

    /**
     * Give the pseudo's field value
     *
     * @return the pseudo's field value
     */
    public String getPseudo() {
        return getValue(pseudoField);
    }

    /**
     * Give the interests in the list
     *
     * @return the interests in the list
     */
    public Set<String> getInterests() {
        if (interests != null) {
            Set<String> ret = new TreeSet<>();
            for (Enumeration<String> e = interests.elements(); e.hasMoreElements();) {
                ret.add(e.nextElement());
            }
            return ret;
        }
        return null;
    }

    //-------------------------------------------------------------------------
    // Private methos
    //-------------------------------------------------------------------------
    /**
     * Invoked when the "add interest" button is clicked
     */
    private void addInterest() {
        String input = JOptionPane.showInputDialog(parent, INTEREST_TEXT);
        if (input != null && !input.equals("")) {
            interests.addElement(input);
            parent.pack();
        }
    }

    /**
     * Invoked when the "suppr" key is typed
     *
     * @param interest the selected value in the interests list
     */
    private void removeInterest(String interest) {
        if (interest != null && interests.contains(interest)) {
            interests.removeElement(interest);
            parent.pack();
        }
    }

    /**
     * Give the value of the given text field or null if the text field is null
     * or empty
     *
     * @param field a text field
     * @return the value or null
     */
    private String getValue(JTextField field) {
        if (field != null) {
            String value = field.getText();
            if (value != null && !value.equals("")) {
                return value;
            }
        }
        return null;
    }

    /**
     * Put fields on the panel
     */
    private void fillPanel() {
        JPanel labelsPanel = new JPanel();
        labelsPanel.setBackground(Color.white);
        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.PAGE_AXIS));
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.PAGE_AXIS));
        if (pseudoField != null) {
            labelsPanel.add(makeLabel(PSEUDO_TEXT));
            fieldsPanel.add(makePanel(pseudoField));
        }
        if (emailField != null) {
            labelsPanel.add(makeLabel(EMAIL_TEXT));
            fieldsPanel.add(makePanel(emailField));
        }
        if (passwordField != null) {
            labelsPanel.add(makeLabel(PASSWORD_TEXT));
            fieldsPanel.add(makePanel(passwordField));
        }
        if (passwordField2 != null) {
            labelsPanel.add(makeLabel(PASSWORD_TEXT));
            fieldsPanel.add(makePanel(passwordField2));
        }
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.white);
        panel.add(labelsPanel, BorderLayout.WEST);
        panel.add(fieldsPanel, BorderLayout.CENTER);
        if (interestList != null) {
            JPanel listPanel = new JPanel(new BorderLayout(5, 5));
            listPanel.setBackground(Color.white);
            listPanel.setBorder(BEVEL_BORDER);
            listPanel.add(new JLabel(INTERESTS_TEXT), BorderLayout.NORTH);
            listPanel.add(interestList, BorderLayout.CENTER);
            listPanel.add(makePanel(addButton), BorderLayout.EAST);
            panel.add(listPanel, BorderLayout.SOUTH);
        }
        add(panel);
    }

    /**
     * Create a new {@link JTextField} instance
     *
     * @return {@link JTextField} instance
     */
    private JTextField makeTextField() {
        JTextField textField = new JTextField(20);
        textField.setBackground(new Color(250, 250, 250));
        // textField.setBorder(BEVEL_BORDER);
        return textField;
    }

    /**
     * Create a new {@link JPasswordField} instance
     *
     * @return {@link JTextField} instance
     */
    private JTextField makePasswordField() {
        JTextField textField = new JPasswordField(20);
        textField.setBackground(new Color(250, 250, 250));
        // textField.setBorder(BEVEL_BORDER);
        return textField;
    }

    /**
     * Create a panel that contains a label
     *
     * @param text the label text
     * @return panel that contains the label
     */
    private JPanel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setBackground(Color.white);
        return makePanel(label);
    }

    /**
     * Create a panel that contains a component
     *
     * @param text the component
     * @return panel that contains the component
     */
    private JPanel makePanel(JComponent component) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setBorder(EMPTY_BORDER);
        panel.add(component);
        return panel;
    }
}
