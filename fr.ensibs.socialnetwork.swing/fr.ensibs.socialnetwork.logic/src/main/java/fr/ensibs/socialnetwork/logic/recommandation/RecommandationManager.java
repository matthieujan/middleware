package fr.ensibs.socialnetwork.logic.recommandation;

import java.io.Closeable;
import java.util.Set;

/**
 * Represents an object used to manage recommandations
 *
 * @author Pascale Launay
 */
public interface RecommandationManager extends Closeable {

    /**
     * Register the friends of a user when those friends have changed
     *
     * @param email the user's email
     * @param friends the user's friends
     * @throws java.lang.Exception
     */
    public void registerFriends(String email, Set<String> friends) throws Exception;

    /**
     * Register the interests of a user when those interests have changed
     *
     * @param email the user's email
     * @param interests the user's interests
     * @throws java.lang.Exception
     */
    public void registerInterests(String email, Set<String> interests) throws Exception;

    /**
     * Give a user's friends
     *
     * @param email the user's email
     * @return the user's friends
     * @throws java.lang.Exception
     */
    public Set<String> getFriends(String email) throws Exception;
}
