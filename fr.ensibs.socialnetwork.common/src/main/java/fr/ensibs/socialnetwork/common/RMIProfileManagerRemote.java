package fr.ensibs.socialnetwork.common;

import fr.ensibs.socialnetwork.core.Profile;

import java.rmi.Remote;

/**
 * A Remote interface to handle profile related actions.
 * More or less identical to the ProfileManager class from logic to ease understanding and manipulation.
 *
 * @author Matthieu Jan
 */
public interface RMIProfileManagerRemote extends Remote {

    /**
     * Sign up an user to the server
     * @param mail the user's mail, can't be changed, can't be used for another user
     * @param pseudo the user's displayed name
     * @param password the user's password
     * @return return a profile object
     * @throws Exception
     */
    public Profile signUp(String mail, String pseudo, String password) throws Exception;

    /**
     * Log in an user to the server
     * @param email the user's mail
     * @param password the user's password
     * @return the session token
     * @throws Exception
     */
    public String logIn(String email, String password) throws Exception;

    /**
     * Log out an user from the server, and revoke the session token
     * @param token the user's session token
     * @return true if the action worked
     * @throws Exception
     */
    public boolean logOut(String token) throws Exception;

    /**
     * Update the user's profile
     * @param token the user's session token
     * @param profile the user's new profile
     * @return true if the action worked
     * @throws Exception
     */
    public boolean updateProfile(String token, Profile profile) throws Exception;

    /**
     * Get the user's profile
     * @param email user's mail of the profile we want
     * @return associated profile of the user, null if not found
     * @throws Exception
     */
    public Profile getProfile(String email) throws Exception;
}
