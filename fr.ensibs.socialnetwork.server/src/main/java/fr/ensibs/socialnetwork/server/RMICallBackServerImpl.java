package fr.ensibs.socialnetwork.server;

import fr.ensibs.socialnetwork.common.RMICallBackClient;
import fr.ensibs.socialnetwork.common.RMICallBackServer;
import fr.ensibs.socialnetwork.core.Profile;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

/**
 * This class implements the remote interface to callback the clients on profile update.
 *
 * Inspired by M.L.Liu ServerCallback implementation
 */

public class RMICallBackServerImpl extends UnicastRemoteObject implements RMICallBackServer {

    private Vector clientList;

    public RMICallBackServerImpl() throws RemoteException {
        super();
        clientList = new Vector();
    }

    public synchronized void registerForCallback( RMICallBackClient callbackClientObject) {
        // store the callback object into the vector
        if (!(clientList.contains(callbackClientObject))) {
            clientList.addElement(callbackClientObject);
        } // end if
    }

    // This remote method allows an object client to
    // cancel its registration for callback
    // @param id is an ID for the client; to be used by
    // the server to uniquely identify the registered client.
    public synchronized void unregisterForCallback( RMICallBackClient callbackClientObject) {
        if (clientList.removeElement(callbackClientObject)) {
        } else {
            System.out.println( "unregister: clientwasn't registered.");
        }
    }

    public synchronized void doCallbacks(int type,Profile profile) throws java.rmi.RemoteException {
        // make callback to each registered client
        for (int i = 0; i < clientList.size(); i++) {
            System.out.println("doing " + i + "-th callback\n");
            // convert the vector object to a callback object
            RMICallBackClient nextClient = (RMICallBackClient) clientList.elementAt(i);
            // invoke the callback method
            nextClient.fireEvent(type,profile);
        }// end for
    } // doCallbacks

}// end CallbackServerImpl class
