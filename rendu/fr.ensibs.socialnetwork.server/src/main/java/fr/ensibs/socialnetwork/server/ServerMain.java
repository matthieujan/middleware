package fr.ensibs.socialnetwork.server;

import fr.ensibs.socialnetwork.ProfileManagerRemote;
import fr.ensibs.socialnetwork.configuration.ConfigurationManager;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Main class launching the server
 *
 * @author Matthieu Jan
 */
public class ServerMain {

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        //Binding the profile manager to the associated name and port
        Integer port = Integer.parseInt(ConfigurationManager.getInstance().getProperty("RMI_PORT",ConfigurationManager.RMI_PORT));
        Registry reg = LocateRegistry.createRegistry(port);
        ProfileManagerRemote p = new ProfileManagerRemoteImpl();
        ProfileManagerRemote pStub = (ProfileManagerRemote) UnicastRemoteObject.exportObject(p,0);
        reg.bind(ConfigurationManager.getInstance().getProperty("RMI_OBJECT",ConfigurationManager.RMI_OBJ),pStub);

    }
}
