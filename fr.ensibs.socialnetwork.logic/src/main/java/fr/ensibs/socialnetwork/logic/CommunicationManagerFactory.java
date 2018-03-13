package fr.ensibs.socialnetwork.logic;

import fr.ensibs.socialnetwork.logic.friend.FriendManager;
import fr.ensibs.socialnetwork.logic.image.ImageManager;
import fr.ensibs.socialnetwork.logic.message.MessageManager;
import fr.ensibs.socialnetwork.logic.profile.ProfileManager;
import fr.ensibs.socialnetwork.logic.recommandation.RecommandationManager;

/**
 * The factory used to create communication managers. Should be implemented by
 * the communication layer
 *
 * @author Pascale Launay
 */
public interface CommunicationManagerFactory {

    /**
     * Gives an instance that manages profiles
     *
     * @return a profile manager
     */
    public ProfileManager makeProfileManager();

    /**
     * Gives an instance that manages friends
     *
     * @return a friends manager
     */
    public FriendManager makeFriendManager();

    /**
     * Gives an instance that manages messages
     *
     * @return a message manager
     */
    public MessageManager makeMessageManager();

    /**
     * Gives an instance that manages images
     *
     * @return an image manager
     */
    public ImageManager makeImageManager();

    /**
     * Gives an instance that manages recommandations
     *
     * @return a recommandation manager
     */
    public RecommandationManager makeRecommandationManager();
}
