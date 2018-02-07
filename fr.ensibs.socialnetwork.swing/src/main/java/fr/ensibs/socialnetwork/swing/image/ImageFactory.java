package fr.ensibs.socialnetwork.swing.image;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * A factory to create images loaded from input streams
 *
 * @author Pascale Launay
 */
public class ImageFactory {

    private static ImageFactory instance; // a unique instance of this class

    /**
     * Give a unique instance of this class
     *
     * @return a unique instance of this class
     */
    public static ImageFactory getInstance() {
        if (instance == null) {
            instance = new ImageFactory();
        }
        return instance;
    }

    /**
     * Private constructor to avoid the creation of multiple instances of this
     * class (singleton)
     */
    private ImageFactory() {

    }

    /**
     * Load an image from an input stream
     *
     * @param in the image input stream
     * @return the image loaded or null if an error occured
     */
    public Image makeImage(InputStream in) {
        Image image = null;
        try {
            if (in != null) {
                image = ImageIO.read(in);
                in.close();
            }
        } catch (IOException e) {
            System.err.println("Cannot load image: " + e.getMessage());
        }
        return image;
    }
}
