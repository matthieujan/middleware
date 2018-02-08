package fr.ensibs.socialnetwork;


import fr.ensibs.socialnetwork.core.Profile;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface to enable callback from the server, server side
 * Inspired by the University of Illinois at Chicago website examples
 *
 * @author Matthieu Jan
 */
public interface RMICallBackServer extends Remote {

    public void registerForCallback(RMICallBackClient callbackClientObject ) throws java.rmi.RemoteException;

    public void unregisterForCallback( RMICallBackClient callbackClientObject) throws java.rmi.RemoteException;
}
