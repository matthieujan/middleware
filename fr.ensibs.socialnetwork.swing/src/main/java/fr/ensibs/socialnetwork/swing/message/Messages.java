package fr.ensibs.socialnetwork.swing.message;

import fr.ensibs.socialnetwork.core.Message;
import fr.ensibs.socialnetwork.core.Publication;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A class that manages the chat and publication messages between users
 *
 * @author Pascale Launay
 */
public class Messages {

    private final Map<String, Map<String, List<Message>>> chatMessages;
    private final Map<String, List<Publication>> publications;

    /**
     * Private constructor
     */
    public Messages() {
        chatMessages = new HashMap<>();
        publications = new HashMap<>();
    }

    /**
     * Add a chat or publication message
     *
     * @param message a chat or publication message
     */
    public void addMessage(Message message) {
        if (message instanceof Publication) {
            addPublication(message.getSource(), (Publication) message);
        } else {
            addMessage(message.getSource(), message.getTarget(), message);
            addMessage(message.getTarget(), message.getSource(), message);
        }
    }

    /**
     * Give all the chat messages exchanged between two given users
     *
     * @param user1 a user's email address
     * @param user2 another user's email address
     * @return all the chat messages exchanged between the two users
     */
    public List<Message> getChatMessages(String user1, String user2) {
        Map<String, List<Message>> map = this.chatMessages.get(user1);
        if (map != null) {
            return map.get(user2);
        }
        return null;
    }

    /**
     * Give all the publication messages published by a given user
     *
     * @param user a user's email address
     * @return all the publication messages published by the user
     */
    public List<Publication> getPublications(String user) {
        return this.publications.get(user);
    }

    /**
     * Add a publication message
     *
     * @param user the source of the message
     * @param publication the publication message
     */
    private void addPublication(String user, Publication publication) {
        List<Publication> list = publications.get(user);
        if (list == null) {
            list = new LinkedList<>();
            publications.put(user, list);
        }
        list.add(0, publication);
    }

    /**
     * Add a chat message
     *
     * @param user1 the source of the message
     * @param user2 the target of the message
     * @param message the chat message
     */
    private void addMessage(String user1, String user2, Message message) {
        Map<String, List<Message>> map = this.chatMessages.get(user1);
        if (map == null) {
            map = new HashMap<>();
            this.chatMessages.put(user1, map);
        }
        List<Message> list = map.get(user2);
        if (list == null) {
            list = new LinkedList<>();
            map.put(user2, list);
        }
        list.add(0, message);
    }
}
