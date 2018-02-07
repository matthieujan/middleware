package fr.ensibs.socialnetwork;

import fr.ensibs.socialnetwork.core.Profile;

import java.io.IOException;
import java.rmi.Remote;

public interface ProfileManagerRemote extends Remote {

    public Profile signUp(String mail, String pseudo, String password) throws Exception;

    public String logIn(String email, String password) throws Exception;

    public boolean logOut(String token) throws Exception;

    public boolean updateProfile(String token, Profile profile) throws Exception;

    public Profile getProfile(String email) throws Exception;
}
