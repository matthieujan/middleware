package fr.ensibs.socialnetwork.logic.profile;

import fr.ensibs.socialnetwork.core.Profile;
import java.io.Closeable;

/**
 * Represents an object used to manage the user's profile: email, pseudo,
 * password and interests
 *
 * @author Pascale Launay
 */
public interface ProfileManager extends Closeable {

    /**
     * Registers a new user, provided that no other user exists with the same
     * email address.
     *
     * @param email the new user's email
     * @param pseudo the new user's pseudo
     * @param password the new user's password
     * @return the user's profile if he has been registered or else null
     * @throws java.lang.Exception
     */
    public Profile signUp(String email, String pseudo, String password) throws Exception;

    /**
     * Checks whether a user is allowed to log in.
     *
     * @param email the user's email
     * @param password the user's pseudo
     * @return a token identifying the user's session
     * @throws java.lang.Exception
     */
    public String logIn(String email, String password) throws Exception;

    /**
     * Logs out a user
     *
     * @param token the token identifying the user's session
     * @return true if the user has been logged out
     * @throws java.lang.Exception
     */
    public boolean logOut(String token) throws Exception;

    /**
     * Update the profile of the user connected with the given token
     *
     * @param token the token identifying the user's session
     * @param profile the user's new profile
     * @return true if the profile has been updated
     * @throws java.lang.Exception
     */
    public boolean updateProfile(String token, Profile profile) throws Exception;

    /**
     * Give the profile of the user having the given email address
     *
     * @param email the user's email
     * @return the user's profile
     * @throws java.lang.Exception
     */
    public Profile getProfile(String email) throws Exception;
}
