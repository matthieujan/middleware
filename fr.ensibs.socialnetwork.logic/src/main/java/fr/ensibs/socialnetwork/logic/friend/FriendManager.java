package fr.ensibs.socialnetwork.logic.friend;

import java.io.Closeable;

/**
 * Represents an object used to manage the user's profile
 *
 * @author Pascale Launay
 */
public interface FriendManager extends Closeable {

    /**
     * Request for a user to become a friend
     *
     * @param source the email address of the user that requests friendship
     * @param target the email address of the user that is asked for friendship
     */
    public void requestFriend(String source, String target) throws Exception;

    /**
     * Accept friendship
     *
     * @param source the email address of the user that accepts friendship
     * @param target the email address of the user that is accepted
     */
    public void acceptFriend(String source, String target) throws Exception;

    /**
     * Remove a frienship link between two friends
     *
     * @param source the email address of a user
     * @param target the email address of the other user
     */
    public void banFriend(String source, String target) throws Exception;
}
