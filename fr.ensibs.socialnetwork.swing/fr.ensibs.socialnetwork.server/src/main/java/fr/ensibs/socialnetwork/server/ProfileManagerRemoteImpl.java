package fr.ensibs.socialnetwork.server;

import fr.ensibs.socialnetwork.ProfileManagerRemote;
import fr.ensibs.socialnetwork.core.Profile;

import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteServer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ProfileManagerRemoteImpl extends RemoteServer implements ProfileManagerRemote {

    private HashMap<String,Profile> registered;
    private HashMap<String,String> connected;
    private HashMap<String,String> password;
    private HashMap<String, RemoteRef> callbackTable;

    public ProfileManagerRemoteImpl(){
        registered = new HashMap<String, Profile>();
        connected = new HashMap<String, String>();
        password = new HashMap<String, String>();
        callbackTable = new HashMap<String,RemoteRef>();
    }

    public Profile signUp(String email, String pseudo, String password) throws Exception {

        Profile ret = null;
        if(!registered.containsKey(email)){
            ret = new Profile(email, pseudo);
            this.registered.put(email,ret);
            this.password.put(email,password);
        }
        return ret;
    }

    public String logIn(String email, String password) throws Exception {
        String token = null;
        //TODO CHECK IF ALREADY CONNECTED
        if(registered.containsKey(email) && password.equals(this.password.get(email))){
            token = "RandomToken"+(Math.random()*1000);
            connected.put(token,email);
            callbackTable.put(token,this.getRef());
            System.out.println("Connected : "+email+" with token"+token);
        }
        return token;
    }

    public boolean logOut(String token) throws Exception {
        boolean ret = false;
        if(connected.containsKey(token)){
            ret = true;
            System.out.println("Disconnected : "+connected.get(token)+"with token : "+token);
            connected.remove(token);
        }
        return ret;
    }

    public boolean updateProfile(String token, Profile profile) throws Exception {
        boolean ret = false;
        if(connected.containsKey(token)){
            ret = true;
            registered.put(connected.get(token),profile);
        }
        return ret;
    }

    public Profile getProfile(String email) throws Exception {
        return registered.get(email);
    }
}
