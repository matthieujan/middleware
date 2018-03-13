package fr.ensibs.socialnetwork.client;

import fr.ensibs.socialnetwork.logic.CommunicationManagerFactory;
import fr.ensibs.socialnetwork.logic.friend.FriendManager;
import fr.ensibs.socialnetwork.logic.image.ImageManager;
import fr.ensibs.socialnetwork.logic.message.MessageManager;
import fr.ensibs.socialnetwork.logic.profile.ProfileManager;
import fr.ensibs.socialnetwork.logic.recommandation.RecommandationManager;

public class RMICommunicationManagerFactory implements CommunicationManagerFactory {
    public ProfileManager makeProfileManager() {
        return new RMIProfileManager();
    }

    public FriendManager makeFriendManager() {
        return null;
    }

    public MessageManager makeMessageManager() {
        return null;
    }

    public ImageManager makeImageManager() {
        return null;
    }

    public RecommandationManager makeRecommandationManager() {
        return null;
    }
}
