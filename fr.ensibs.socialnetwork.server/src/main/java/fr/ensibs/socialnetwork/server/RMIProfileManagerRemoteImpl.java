package fr.ensibs.socialnetwork.server;

import fr.ensibs.socialnetwork.common.RMICallback;
import fr.ensibs.socialnetwork.common.RMIProfileManagerRemote;
import fr.ensibs.socialnetwork.configuration.ConfigurationManager;
import fr.ensibs.socialnetwork.core.Profile;
import org.exolab.jms.administration.AdminConnectionFactory;
import org.exolab.jms.administration.JmsAdminServerIfc;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

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
            if(!createJmsUser(email,password)){
                System.out.println("Jms Failed, transaction reseted");
                this.registered.remove(email);
                this.password.remove(email);
                ret = null;
            }
        }
        return ret;
    }

    /**
     * Custom method to create the user environement on the jms server
     * @param email
     * @param password
     * @return
     */
    private boolean createJmsUser(String email, String password) {
        boolean ret = true;
        try {
            //TODO Correct the dot in mail problem
            String server_host = ConfigurationManager.getInstance().getProperty(ConfigurationManager.SERVER_HOST,"localhost");
            int jms_port = ConfigurationManager.getInstance().getIntegerProperty(ConfigurationManager.JMS_PORT,5001);
            String url = "tcp://"+server_host+":"+jms_port+"/";

        Hashtable properties = new Hashtable();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
        properties.put(Context.PROVIDER_URL, "tcp://"+server_host+":"+jms_port+"/");
        Context context = new InitialContext(properties);
        ConnectionFactory cf = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = cf.createConnection();
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        String pub = email+"pub";
        String priv = email+"priv";
        try{
                Destination destination = (Destination) context.lookup(pub);
        }catch (NameNotFoundException e){
            Topic topic = session.createTopic(pub);
            context.bind(pub,topic);
        }

        try{
                Destination destination = (Destination) context.lookup(priv);
        }catch (NameNotFoundException e){
            Queue queue = session.createQueue(priv);
            context.bind(priv,queue);
        }

            /*JmsAdminServerIfc admin = AdminConnectionFactory.create(url);
            admin.addUser(email,password);
            admin.addDestination(email+"priv",true);
            admin.addDestination(email+"pub",false);
            admin.close();*/

        }catch (Exception e){
            e.printStackTrace();
            ret = false;
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
        //If the token exist AND if the "token's" email match the profile email
        if (connected.containsKey(token) && profile.getEmail().equals(connected.get(token))) {
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
                    callback.get(key).fireEvent(type,profile);

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


