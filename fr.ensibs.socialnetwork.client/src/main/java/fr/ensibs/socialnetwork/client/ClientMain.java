package fr.ensibs.socialnetwork.client;

import fr.ensibs.socialnetwork.logic.CommunicationManagerFactory;
import fr.ensibs.socialnetwork.logic.SocialNetwork;
import fr.ensibs.socialnetwork.swing.SocialNetworkFrame;

/**
 * Client main class
 */
public class ClientMain {

    /**
     * Simple launcher for the application
     * @param args
     */
    public static void main(String[] args){

        //We use a custom CommunicationManagerFactory, only handling RMI for profile interaction at the moment.
        CommunicationManagerFactory cmf = new CustomCommunicationManagerFactory();
        //Creating the socialNetwork object and launching the GUI
        SocialNetwork socialNetwork = new SocialNetwork(cmf);
        SocialNetworkFrame frame = new SocialNetworkFrame(socialNetwork);
        frame.show();
    }
}
