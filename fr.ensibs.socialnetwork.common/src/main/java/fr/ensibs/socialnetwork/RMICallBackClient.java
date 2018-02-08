package fr.ensibs.socialnetwork;


import fr.ensibs.socialnetwork.core.Profile;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface to enable callback from the server, client side
 * Inspired by the University of Illinois at Chicago website examples
 * @author Matthieu Jan
 */
public interface RMICallBackClient extends Remote {

    /**
     * This methode is used to fire a ProfileEvent in the client
     * @param type the event type (PSEUDO_CHANGED|INTEREST_CHANGED|BOTH_CHANGED)
     * @param profile the user's profile
     * @throws RemoteException
     */
    void fireEvent(int type, Profile profile) throws RemoteException;
}
