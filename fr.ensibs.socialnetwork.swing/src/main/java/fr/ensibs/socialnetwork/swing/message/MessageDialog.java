package fr.ensibs.socialnetwork.swing.message;

import fr.ensibs.socialnetwork.core.Message;
import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.swing.SocialNetworkFrame;
import fr.ensibs.socialnetwork.swing.DefaultDialog;
import static fr.ensibs.socialnetwork.swing.SocialNetworkFrame.DEBUG;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import static javax.swing.border.BevelBorder.LOWERED;
import javax.swing.border.Border;

/**
 * A dialog to display the messages to/from a friend and allow to send new
 * messages
 *
 * @author Pascale Launay
 */
public class MessageDialog extends DefaultDialog {

    private static final Border BEVEL_BORDER = BorderFactory.createBevelBorder(LOWERED);
    private static final String SEND_TEXT = "Send";

    private final MessageEventListener eventListener; // listener for this dialog's users messages
    private final MessagePanel messagePanel; // panel containing the messages list
    private final JTextArea textArea; // message input area
    private final JButton sendButton; // message send button
    private final Profile me, other; // users of this chat

    /**
     * Constructor
     *
     * @param frame the parent frame
     * @param me my email address
     * @param other the other email address
     * @param messages the initial messages
     */
    public MessageDialog(SocialNetworkFrame frame, Profile me, Profile other, List<Message> messages) {
        super(frame, other.getPseudo(), false);
        this.me = me;
        this.other = other;
        messagePanel = new MessagePanel(frame, me, other, messages);
        eventListener = new MessageEventListener(other, messagePanel);
        textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(250, 40));
        textArea.setBorder(BEVEL_BORDER);
        sendButton = new JButton(new AbstractAction(SEND_TEXT) {
            @Override
            public void actionPerformed(ActionEvent event) {
                send(textArea.getText());
                textArea.setText("");
            }
        });

        fillPanel();
        pack();
        setLocationRelativeTo(socialNetworkFrame);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            eventListener.open(me);
        } else if (eventListener != null) {
            eventListener.close();
        }
    }

    /**
     * Send a message with the given text content
     *
     * @param text the message content
     */
    private void send(String text) {
        if (text != null && !text.equals("")) {
            if (DEBUG) {
                System.out.println("[swing] MessagesDialog#send source=" + me
                        + ", target=" + other
                        + ", message=" + text);
            }
            Message message = new Message(System.currentTimeMillis(), me.getEmail(), other.getEmail(), text);
            socialNetworkFrame.sendMessage(message);
            messagePanel.addMessage(message);
        }
    }

    /**
     * Put the widgets on the panel
     */
    private void fillPanel() {
        setLayout(new BorderLayout(5, 5));
        add(messagePanel, BorderLayout.CENTER);
        JPanel northPanel = new JPanel(new BorderLayout(5, 5));
        northPanel.setBackground(Color.white);
        northPanel.add(textArea, BorderLayout.CENTER);
        northPanel.add(makeComponentPanel(sendButton), BorderLayout.EAST);
        add(northPanel, BorderLayout.NORTH);
    }
}
