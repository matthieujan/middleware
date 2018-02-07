package fr.ensibs.socialnetwork.swing.message;

import fr.ensibs.socialnetwork.core.Message;
import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.swing.SocialNetworkFrame;
import java.util.List;

/**
 * A panel to display a list of messages to/from a friend
 *
 * @author Pascale Launay
 */
public class MessagePanel extends AbstractMessagePanel<Message> {

    private final Profile other; // the users of these messages

    /**
     * Constructor
     *
     * @param frame the parent frame
     * @param me a user
     * @param other the other user
     * @param messages messages to initialize the list
     */
    public MessagePanel(SocialNetworkFrame frame, Profile me, Profile other, List<Message> messages) {
        super(frame);
        open(me);
        this.other = other;
        setMessages(messages);
    }

    @Override
    protected MessageRenderer makeMessageRenderer() {
        return new MessageRenderer();
    }

    /**
     * Renderer for the messages in the list
     */
    class MessageRenderer extends AbstractMessageRenderer {

        @Override
        protected String getPseudo(String email) {
            if (me.getEmail().equals(email)) {
                return me.getPseudo();
            } else {
                return other.getPseudo();
            }
        }
    }
}
