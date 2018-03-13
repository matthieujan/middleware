package fr.ensibs.socialnetwork.server;

import fr.ensibs.socialnetwork.ProfileManagerRemote;
import fr.ensibs.socialnetwork.configuration.ConfigurationManager;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain {

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        String server_host = ConfigurationManager.getInstance().getProperty("SERVER_HOST",ConfigurationManager.SERVER_HOST);
        Integer port = Integer.parseInt(ConfigurationManager.getInstance().getProperty("RMI_PORT",ConfigurationManager.RMI_PORT));

        Registry reg = LocateRegistry.createRegistry(port);

        ProfileManagerRemote p = new ProfileManagerRemoteImpl();
        ProfileManagerRemote pStub = (ProfileManagerRemote) UnicastRemoteObject.exportObject(p,0);
        reg.bind(ConfigurationManager.getInstance().getProperty("RMI_OBJECT",ConfigurationManager.RMI_OBJ),pStub);

    }
}
