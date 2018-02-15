package fr.ensibs.socialnetwork.server;

import fr.ensibs.socialnetwork.common.RMICallback;
import fr.ensibs.socialnetwork.common.RMIProfileManagerRemote;
import fr.ensibs.socialnetwork.core.Profile;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.util.HashMap;

/**
 * Implementation of the ProfileManagerRemote for the server.
 * This object handle every server sided profile actions.
 */
public class RMIProfileManagerRemoteImpl extends RemoteServer implements RMIProfileManagerRemote {

    private HashMap<String, Profile> registered; //List the registered users by mail/profile
    private HashMap<String, String> connected; //List the connected users by token/mail
    private HashMap<String, String> password; //List the user's password by email/password
    private HashMap<String, RMICallback> callback; //List the user's password by email/password

    /**
     * Initialize the data structures
     */
    public RMIProfileManagerRemoteImpl() throws RemoteException {
        super();
        registered = new HashMap<String, Profile>();
        connected = new HashMap<String, String>();
        password = new HashMap<String, String>();
        callback = new HashMap<String, RMICallback>();
    }

    /**
     * This method register an user to the server, if the server doesn't already contains an user with the same email
     *
     * @param email    user's email
     * @param pseudo   user's displayed name
     * @param password user's password
     * @return the new user's Profile object
     * @throws Exception
     */
    public Profile signUp(String email, String pseudo, String password) {
        Profile ret = null;
        if (!registered.containsKey(email)) {
            ret = new Profile(email, pseudo);
            this.registered.put(email, ret); //Adding the new profile to the map
            this.password.put(email, password); //Adding the new password to the map
        }
        return ret;
    }

    /**
     * This method log the user to the server, if it exist and the password is correct
     *
     * @param email    user's email
     * @param password user's password
     * @return user's session token, or null if it didn't worked
     * @throws Exception
     */
    public String logIn(String email, String password, RMICallback cb) {
        String token = null;
        if (registered.containsKey(email) && password.equals(this.password.get(email))) {
            token = "RandomToken" + (Math.random() * 1000); //Basic random token
            while (connected.containsKey(token)) { //Verifying that the token doesn't already exists
                token = "RandomToken" + (Math.random() * 1000);
            }
            connected.put(token, email);
            callback.put(token,cb);
        }
        return token;
    }

    /**
     * Log out the user's with the associated session's token
     *
     * @param token the user's session's token
     * @return true if it worked
     * @throws Exception
     */
    public boolean logOut(String token) {
        boolean ret = false;
        if (connected.containsKey(token)) {
                ret = true;
                connected.remove(token);
                callback.remove(token);
        }
        return ret;
    }

    /**
     * Update the user's profile associated with this token.
     *
     * @param token   the user's session's token
     * @param profile the user's profile
     * @return true if it worked
     * @throws Exception
     */
    public boolean updateProfile(String token, Profile profile) throws Exception {
        boolean ret = false;
        System.out.println("Test1");
        //If the token exist AND if the "token's" email match the profile email
        if (connected.containsKey(token) && profile.getEmail().equals(connected.get(token))) {
			System.out.println("Test2");

            ret = true;

            Profile oldProfile = registered.get(profile.getEmail());
            int type = 0;
            if (!oldProfile.getPseudo().equals(profile.getPseudo())) {
                type += 1;
            } else if (!oldProfile.getInterests().equals(profile.getInterests())) {
                type += 2;
            }

            if (type != 0) {
                for(String key : callback.keySet()){
					System.out.println("Test3");
                    callback.get(key).fireEvent(type,profile);
                    System.out.println("Test4");

                }
            }

        }
        return ret;
    }

    /**
     * Get the user's profile
     *
     * @param email
     * @return the user's profile, or null
     * @throws Exception
     */
    public Profile getProfile(String email) {
        return registered.get(email);
    }
}


