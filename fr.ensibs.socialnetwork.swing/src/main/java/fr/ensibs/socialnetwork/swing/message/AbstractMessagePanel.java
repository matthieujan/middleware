package fr.ensibs.socialnetwork.swing.message;

import fr.ensibs.socialnetwork.core.Message;
import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.swing.SocialNetworkFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import static javax.swing.border.BevelBorder.RAISED;
import javax.swing.border.Border;

/**
 * A panel to display a list of messages to/from a friend
 *
 * @param <T> Message or Publication
 *
 * @author Pascale Launay
 */
public class AbstractMessagePanel<T extends Message> extends JPanel {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");
    protected static final Border BEVEL_BORDER = BorderFactory.createBevelBorder(RAISED);
    private static final Border LEFT_BORDER = BorderFactory.createEmptyBorder(2, 5, 2, 30);
    private static final Border RIGHT_BORDER = BorderFactory.createEmptyBorder(2, 30, 2, 5);

    private final JList<T> messagesList; // the list
    private final DefaultListModel<T> messagesModel; // the messages
    protected Profile me;
    protected SocialNetworkFrame socialNetworkFrame;

    /**
     * Constructor
     *
     * @param frame the parent frame
     */
    public AbstractMessagePanel(SocialNetworkFrame frame) {
        this.socialNetworkFrame = frame;
        setBackground(Color.white);
        setLayout(new BorderLayout(5, 5));
        messagesModel = new DefaultListModel<>();
        messagesList = new JList<>(messagesModel);
        messagesList.setCellRenderer(makeMessageRenderer());
        JScrollPane scrollPane = new JScrollPane(messagesList);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Open the panel
     *
     * @param me my profile
     */
    public void open(Profile me) {
        this.me = me;
    }

    /**
     * Add a message on top of the list
     *
     * @param message a message
     */
    public void addMessage(T message) {
        messagesModel.add(0, message);
    }

    /**
     * Initialize the list with the given messages
     *
     * @param messages a list of messages
     */
    public void setMessages(Collection<T> messages) {
        if (messages != null) {
            for (T message : messages) {
                messagesModel.addElement(message);
            }
        }
    }

    /**
     * Remove all publications in the list
     */
    public void clear() {
        messagesModel.clear();
    }

    protected AbstractMessageRenderer makeMessageRenderer() {
        return new AbstractMessageRenderer();
    }

    /**
     * Renderer for the messages in the list
     */
    protected class AbstractMessageRenderer extends JPanel implements ListCellRenderer<T> {

        protected final JTextArea textArea; // message text
        protected final JLabel label; // message date
        protected final JPanel contentPanel; // message content

        /**
         * Constructor
         */
        public AbstractMessageRenderer() {
            contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBorder(BEVEL_BORDER);
            label = new JLabel();
            textArea = new JTextArea();
            textArea.setPreferredSize(new Dimension(250, 40));
            contentPanel.add(textArea, BorderLayout.CENTER);
            setLayout(new BorderLayout());
            add(label, BorderLayout.NORTH);
            add(contentPanel, BorderLayout.CENTER);
        }

        @Override
        public JPanel getListCellRendererComponent(JList<? extends T> list, T message, int idx, boolean selected, boolean hasFocus) {
            String pseudo = getPseudo(message.getSource());
            Border border = (me.getEmail().equals(message.getSource()) ? LEFT_BORDER : RIGHT_BORDER);
            label.setText("[" + DATE_FORMAT.format(message.getDate()) + "] " + pseudo);
            textArea.setText(message.getMessage());
            setBorder(border);
            return this;
        }

        /**
         * Give the pseudo associated to an email
         *
         * @param email an email
         * @return the corresponding pseudo
         */
        protected String getPseudo(String email) {
            if (me.getEmail().equals(email)) {
                return me.getPseudo();
            } else {
                Profile profile = socialNetworkFrame.getProfile(email);
                if (profile != null) {
                    return profile.getPseudo();
                } else {
                    return email;
                }
            }
        }
    }
}
