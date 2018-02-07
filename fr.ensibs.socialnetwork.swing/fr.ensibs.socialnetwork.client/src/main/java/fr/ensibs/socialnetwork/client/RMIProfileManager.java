package fr.ensibs.socialnetwork.client;

import fr.ensibs.socialnetwork.ProfileManagerRemote;
import fr.ensibs.socialnetwork.configuration.ConfigurationManager;
import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.logic.profile.ProfileEvent;
import fr.ensibs.socialnetwork.logic.profile.ProfileManager;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIProfileManager implements ProfileManager{
    public Profile signUp(String email, String pseudo, String password) throws Exception {
        return getRemote().signUp(email, pseudo, password);
    }

    public String logIn(String email, String password) throws Exception {
        String token = getRemote().logIn(email, password);
        System.out.println(token);
        return token;
    }

    public boolean logOut(String token) throws Exception {
        return getRemote().logOut(token);
    }

    public boolean updateProfile(String token, Profile profile) throws Exception {
        boolean ret = getRemote().updateProfile(token, profile);



        return getRemote().updateProfile(token, profile);
    }

    public Profile getProfile(String email) throws Exception {
        return getRemote().getProfile(email);
    }

    public void close() throws IOException {

    }

    private ProfileManagerRemote getRemote() throws UnknownHostException, RemoteException, NotBoundException {
        //Comm phase
        String server_host = ConfigurationManager.getInstance().getProperty("SERVER_HOST",ConfigurationManager.SERVER_HOST);
        String client_host = Inet4Address.getLocalHost().getHostName();
        Integer port = Integer.parseInt(ConfigurationManager.getInstance().getProperty("RMI_PORT",ConfigurationManager.RMI_PORT));

        Registry reg = LocateRegistry.getRegistry(server_host,port);

        ProfileManagerRemote profileManagerRemote = (ProfileManagerRemote) reg.lookup(ConfigurationManager.getInstance().getProperty("RMI_OBJECT",ConfigurationManager.RMI_OBJ));
        return profileManagerRemote;
    }
}
