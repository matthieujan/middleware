package fr.ensibs.socialnetwork.core;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * A text message sent by a user to another user
 *
 * @author Pascale Launay
 */
public class Message implements Comparable<Message>, Serializable {

    protected static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");

    private final long date; // the date when this message was sent
    private final String message; // the content of this chat message
    private final String source; // the sender of this message
    private final String target; // the recipient of this message

    /**
     * Constructor.
     *
     * @param date the date when this message was sent
     * @param source the sender of this message
     * @param target the recipient of this message
     * @param message a text content
     */
    public Message(long date, String source, String target, String message) {
        this.date = date;
        this.message = message;
        this.source = source;
        this.target = target;
    }

    /**
     * Give the date when this message was sent
     *
     * @return the date when this message was sent
     */
    public long getDate() {
        return date;
    }

    /**
     * Give the text content of this chat message
     *
     * @return the text content of this chat message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Give the sender of this message
     *
     * @return the sender of this message
     */
    public String getSource() {
        return source;
    }

    /**
     * Give the recipient of this message
     *
     * @return the recipient of this message
     */
    public String getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "[Date: " + DATE_FORMAT.format(date)
                + " -- From: " + source
                + (target != null ? " -- To: " + target : "")
                + (message != null ? " -- Message: " + message : "")
                + "]";
    }

    @Override
    public int compareTo(Message other) {
        if (date < other.date) {
            return 1;
        } else if (date > other.date) {
            return -1;
        } else {
            return source.compareTo(other.source);
        }
    }
}
