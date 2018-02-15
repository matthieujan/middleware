package fr.ensibs.socialnetwork.server;

import fr.ensibs.socialnetwork.configuration.ConfigurationManager;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Main class launching the server
 */
public class ServerMain {

    public static RMIProfileManagerRemoteImpl profileManager;

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {

        String server_host = ConfigurationManager.getInstance().getProperty("SERVER_HOST",ConfigurationManager.SERVER_HOST);
        System.setProperty("java.rmi.server.hostname",server_host);
        System.setProperty("java.security.policy","$HOME/.java.policy");
        //Binding the profile manager to the associated name and port
        Integer port = Integer.parseInt(ConfigurationManager.getInstance().getProperty("RMI_PORT", ConfigurationManager.RMI_PORT));

        //ProfileManagerRemote
        profileManager = new RMIProfileManagerRemoteImpl();

        //Binding
        Registry reg = LocateRegistry.createRegistry(port);
        reg.bind(ConfigurationManager.getInstance().getProperty("RMI_OBJECT", ConfigurationManager.RMI_OBJ), profileManager);
    }
}
