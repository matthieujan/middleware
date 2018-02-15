package fr.ensibs.socialnetwork.client;

import fr.ensibs.socialnetwork.common.RMICallback;
import fr.ensibs.socialnetwork.common.RMIProfileManagerRemote;
import fr.ensibs.socialnetwork.configuration.ConfigurationManager;
import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.logic.profile.ProfileManager;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * The RMI implementation for the client ProfileManager.
 * Mostly using the ProfileManagerRemote from the common package (mirroring it).
 */
public class RMIProfileManager implements ProfileManager{

    /**
     * Sign up the user to the server
     * @param email user's email
     * @param pseudo user's pseudo
     * @param password user's password
     * @return the user's profile, null if error
     * @throws Exception
     */
    public Profile signUp(String email, String pseudo, String password) throws Exception {
        //Basically just use the same method from the remote object
        return getRemoteProfileManager().signUp(email, pseudo, password);
    }

    /**
     * Log in the user to the server
     * @param email user's mail
     * @param password user's password
     * @return
     * @throws Exception
     */
    public String logIn(String email, String password) throws Exception {
        //Use the remote methode
        RMICallback cb = new RMICallbackImpl();
        String token = getRemoteProfileManager().logIn(email, password,cb);
        return token;
    }

    /**
     * Log out user from the server
     * @param token user's session token
     * @return true if it worked
     * @throws Exception
     */
    public boolean logOut(String token) throws Exception {
        boolean ret = false;
        try {
            //Use the remote method
            ret = getRemoteProfileManager().logOut(token);
        }catch (Exception e){
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    /**
     * Update the user's profile to the server
     * @param token user's session token
     * @param profile user's new Profile
     * @return true if it worked
     * @throws Exception
     */
    public boolean updateProfile(String token, Profile profile) throws Exception {
        boolean ret = false;
        try{
            ret=getRemoteProfileManager().updateProfile(token, profile);
        }catch (Exception e){
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    /**
     * Get the specified user's profile
     * @param email the user's mail
     * @return the user's profile, or null if it doesn't exist
     * @throws Exception
     */
    public Profile getProfile(String email) throws Exception {
        return getRemoteProfileManager().getProfile(email);
    }

    //Exists ...
    public void close() throws IOException {

    }

    /*
     * Private method to easily get the remote ProfileManager from the configuration file.
     */
    private RMIProfileManagerRemote getRemoteProfileManager() throws UnknownHostException, RemoteException, NotBoundException {
        //Comm phase
        String server_host = ConfigurationManager.getInstance().getProperty("SERVER_HOST",ConfigurationManager.SERVER_HOST);
        Integer port = Integer.parseInt(ConfigurationManager.getInstance().getProperty("RMI_PORT",ConfigurationManager.RMI_PORT));
		System.out.println(server_host+" "+port);
        Registry reg = LocateRegistry.getRegistry(server_host,port);

        RMIProfileManagerRemote profileManagerRemote = (RMIProfileManagerRemote) reg.lookup(ConfigurationManager.getInstance().getProperty("RMI_OBJECT",ConfigurationManager.RMI_OBJ));
        return profileManagerRemote;
    }
}
