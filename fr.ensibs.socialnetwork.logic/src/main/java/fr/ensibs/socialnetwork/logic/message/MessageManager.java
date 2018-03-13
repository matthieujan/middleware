package fr.ensibs.socialnetwork.logic.message;

import fr.ensibs.socialnetwork.core.Message;
import fr.ensibs.socialnetwork.core.Publication;
import java.io.Closeable;

/**
 * Represents an object used to manage the users' chat messages and publications
 *
 * @author Pascale Launay
 */
public interface MessageManager extends Closeable {

    /**
     * Forward a message from its source to its targer
     *
     * @param message a chat message
     * @throws java.lang.Exception
     */
    public void send(Message message) throws Exception;

    /**
     * Forward a message from its source to its potential receivers (subscribers
     * of the message source)
     *
     * @param publication a publication message
     * @throws java.lang.Exception
     */
    public void publish(Publication publication) throws Exception;

    /**
     * Subscribe a user in order to receive the messages published by an other
     * user
     *
     * @param subscriber the user that subscribes to receive messages
     * @param source the user source of the messages the subscriber is
     * interested in
     * @throws java.lang.Exception
     */
    public void subscribe(String subscriber, String source) throws Exception;

    /**
     * Unsubscribe a user in order to stop receiving the messages published by
     * an other user
     *
     * @param subscriber the user that unsubscribes to stop receiving messages
     * @param source the user source of the messages the subscriber is not more
     * interested in
     * @throws java.lang.Exception
     */
    public void unsubscribe(String subscriber, String source) throws Exception;
}
