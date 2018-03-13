package fr.ensibs.socialnetwork.logic.image;

import java.awt.Image;
import java.io.Closeable;

/**
 * Represents an object used to manage images associated to keys
 *
 * @author Pascale Launay
 */
public interface ImageManager extends Closeable {

    /**
     * Add a new image
     *
     * @param image the image
     * @return the key associated to the image
     * @throws java.lang.Exception
     */
    public String addImage(Image image) throws Exception;

    /**
     * Give an image from its key
     *
     * @param imageKey a key
     * @return the image associated to the key or null
     * @throws java.lang.Exception
     */
    public Image getImage(String imageKey) throws Exception;

}
