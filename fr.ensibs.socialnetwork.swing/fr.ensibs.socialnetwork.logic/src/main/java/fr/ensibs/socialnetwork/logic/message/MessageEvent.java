package fr.ensibs.socialnetwork.logic.message;

import fr.ensibs.socialnetwork.core.Message;
import fr.ensibs.socialnetwork.core.Publication;

/**
 * An event related to messages
 *
 * @author Pascale Launay
 */
public class MessageEvent {

    public static final int PRIVATE = 1;
    public static final int PUB = 2;

    private final int type; // the event type (CHAT|PUB)
    private final Message message; // a chat or publication message
    private Publication publication; // a publication message

    /**
     * Constructor
     *
     * @param message a chat or publication message
     */
    public MessageEvent(Message message) {
        if (message instanceof Publication) {
            this.type = PUB;
            this.publication = (Publication) message;
        } else {
            this.type = PRIVATE;
        }
        this.message = message;
    }

    /**
     * Give the event type (CHAT|PUB)
     *
     * @return the event type (CHAT|PUB)
     */
    public int getType() {
        return type;
    }

    /**
     * Give the chat or publication message
     *
     * @return the chat or publication message
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Give the publication message (PUB type)
     *
     * @return the publication message (PUB type) or null (CHAT type)
     */
    public Publication getPublication() {
        return publication;
    }

    @Override
    public String toString() {
        return "{ type: " + typeToString(type)
                + ", message: " + message + " }";
    }

    /**
     * Give a string representation of the given type
     *
     * @param type the type
     * @return the string representation of the type
     */
    private String typeToString(int type) {
        switch (type) {
            case PUB:
                return "PUB";
            case PRIVATE:
                return "CHAT";
        }
        return "UNKNOWN";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MessageEvent) {
            return hashCode() == o.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
