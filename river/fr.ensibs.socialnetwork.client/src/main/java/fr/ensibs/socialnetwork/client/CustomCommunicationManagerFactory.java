package fr.ensibs.socialnetwork.client;

import fr.ensibs.socialnetwork.client.image.AxisImageManager;
import fr.ensibs.socialnetwork.client.friend.JmsFriendManager;
import fr.ensibs.socialnetwork.client.message.JmsMessageManager;
import fr.ensibs.socialnetwork.client.profile.RMIProfileManager;
import fr.ensibs.socialnetwork.client.recommandation.JSRecommandationManager;
import fr.ensibs.socialnetwork.logic.CommunicationManagerFactory;
import fr.ensibs.socialnetwork.logic.friend.FriendManager;
import fr.ensibs.socialnetwork.logic.image.ImageManager;
import fr.ensibs.socialnetwork.logic.message.MessageManager;
import fr.ensibs.socialnetwork.logic.profile.ProfileManager;
import fr.ensibs.socialnetwork.logic.recommandation.RecommandationManager;

/**
 * Custom CommunicationManagerFactory to create the RMIProfileManager as the only manager
 */
public class CustomCommunicationManagerFactory implements CommunicationManagerFactory {

    /**
     * ProfileManager maker
     * @return an RMI using Manager
     */
    public ProfileManager makeProfileManager() {
        return new RMIProfileManager();
    }

    public FriendManager makeFriendManager() {
        return new JmsFriendManager();
    }

    public MessageManager makeMessageManager() {
        return new JmsMessageManager();
    }

    public ImageManager makeImageManager() {
        return new AxisImageManager();
    }

    public RecommandationManager makeRecommandationManager() {
        return new JSRecommandationManager();
    }
}
