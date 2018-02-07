package fr.ensibs.socialnetwork.swing.message;

import fr.ensibs.socialnetwork.core.Message;
import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.events.EventController;
import fr.ensibs.socialnetwork.events.EventControllerFactory;
import fr.ensibs.socialnetwork.events.EventListener;
import fr.ensibs.socialnetwork.logic.message.MessageEvent;
import static fr.ensibs.socialnetwork.logic.message.MessageEvent.PUB;
import fr.ensibs.socialnetwork.swing.SocialNetworkFrame;
import java.io.Closeable;
import static fr.ensibs.socialnetwork.logic.message.MessageEvent.PRIVATE;

/**
 * A listener that reacts to events related to friends by forwarding them to the
 * right panels
 *
 * @author Pascale Launay
 */
public class MessageEventListener implements EventListener<MessageEvent>, Closeable {

    private String me; // my email address
    private final EventController<MessageEvent> messageEventControler; // the event controler to (un)register this instance
    private MessagePanel messagePanel;
    private String source;
    private SocialNetworkFrame frame;

    /**
     * Constructor for a listener that manages all messages sent by my friends
     *
     * @param frame the application frame
     */
    public MessageEventListener(SocialNetworkFrame frame) {
        messageEventControler = EventControllerFactory.getInstance().makeMessageEventController();
        this.frame = frame;
    }

    /**
     * Constructor for a listener when a message dialog for a given source is
     * opened
     *
     * @param source a friend's email address source of messages this listener
     * manages
     * @param messagePanel a panel that displays chat messages
     */
    public MessageEventListener(Profile source, MessagePanel messagePanel) {
        messageEventControler = EventControllerFactory.getInstance().makeMessageEventController();
        this.messagePanel = messagePanel;
        this.source = source.getEmail();
    }

    /**
     * Start receiving events related to the given user
     *
     * @param me a user
     */
    public void open(Profile me) {
        this.me = me.getEmail();
        messageEventControler.addEventListener(this);
    }

    /**
     * Stop receiving events
     */
    @Override
    public void close() {
        messageEventControler.removeEventListener(this);
    }

    @Override
    public void onEvent(MessageEvent event) {
        Message message = event.getMessage();
        if (event.getType() == PUB || me.equals(message.getTarget())) {
            if (frame != null) {
                if (event.getType() == PRIVATE || frame.hasFriend(message.getSource())) {
                    frame.addMessage(message);
                }
            } else if (event.getType() == PRIVATE && source != null && source.equals(message.getSource())) {
                messagePanel.addMessage(message);
            }
        }
    }
}
