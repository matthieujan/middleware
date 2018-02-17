package fr.ensibs.socialnetwork.server;

import fr.ensibs.openjms.OpenJms;
import fr.ensibs.socialnetwork.common.RMIProfileManagerRemote;
import fr.ensibs.socialnetwork.configuration.ConfigurationManager;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Main class launching the server
 */
public class ServerMain {

    private static ConfigurationManager conf;
    private static String server_host;

    private static int rmi_port;
    private static Registry reg;
    private static RMIProfileManagerRemoteImpl profileManager;
    private static RMIProfileManagerRemote profileManagerStub;


    private static int jms_port;
    private static String jms_home;
    private static OpenJms openJmsServer;


    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        //Adding conf to ServerMain to facilitate reading and writing the code
        conf = ConfigurationManager.getInstance();
        //Setting host and rmi_port from parameters
        server_host = conf.getProperty(ConfigurationManager.SERVER_HOST,"localhost");

        //Launching start method
        ServerMain.start();

        //Hooking a stop method when killed
        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                ServerMain.stop();
            }
        });
    }

    private static void start() throws RemoteException, AlreadyBoundException {
        startRmi();
        startJms();
        System.out.println("Test1");
    }

    private static void startRmi() throws AlreadyBoundException, RemoteException {
       //Setting RMI
        System.setProperty("java.rmi.server.hostname",server_host);
        System.setProperty("java.security.policy","$HOME/.java.policy");
        profileManager = new RMIProfileManagerRemoteImpl();
        profileManagerStub = (RMIProfileManagerRemote) UnicastRemoteObject.exportObject(profileManager, 0);
        //Binding RMI
        rmi_port = conf.getIntegerProperty(ConfigurationManager.RMI_PORT,5000);
        reg = LocateRegistry.createRegistry(rmi_port);
        reg.bind(conf.getProperty(ConfigurationManager.RMI_OBJ,"RMI_OBJ"), profileManagerStub);
    }

    private static void startJms(){
        jms_port = conf.getIntegerProperty(ConfigurationManager.JMS_PORT,5001);
        jms_home = ConfigurationManager.getInstance().getProperty(ConfigurationManager.JMS_HOME,"/tmp/openjms-0-7.7-beta-1");
        openJmsServer = new OpenJms(server_host,jms_port,jms_home);
        openJmsServer.start();
    }

    private static void stop(){
        stopRmi();
        stopJms();
    }

    private static void stopRmi(){
        //Des trucs
    }

    private static void stopJms(){
        openJmsServer.stop();
    }


    public static OpenJms getOpenJmsServer(){
        return openJmsServer;
    }


}
