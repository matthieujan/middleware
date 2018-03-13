package fr.ensibs.socialnetwork.client;

import fr.ensibs.socialnetwork.logic.CommunicationManagerFactory;
import fr.ensibs.socialnetwork.logic.SocialNetwork;
import fr.ensibs.socialnetwork.swing.SocialNetworkFrame;

public class ClientMain {
    public static void main(String[] args){
        System.out.println("Bonjour, je suis le client");
        CommunicationManagerFactory cmf = new RMICommunicationManagerFactory();
        SocialNetwork socialNetwork = new SocialNetwork(cmf);
        SocialNetworkFrame frame = new SocialNetworkFrame(socialNetwork);
        frame.show();
    }
}
