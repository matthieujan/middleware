package fr.ensibs.socialnetwork.core;

/**
 * A publication composed of a text message and an image, published by a user.
 * Image or text message can be null (exclusive or)
 *
 * @author Pascale Launay
 */
public class Publication extends Message {

    private final String imageKey; // a key representing an image

    /**
     * Constructor. The text message or the image can be null, but not both
     *
     * @param date the date when this publication was sent
     * @param source the sender of this publication
     * @param message a text message
     * @param imageKey an image
     */
    public Publication(long date, String source, String message, String imageKey) {
        super(date, source, null, message);
        this.imageKey = imageKey;
    }

    /**
     * Give the image of this publication
     *
     * @return the image of this publication
     */
    public String getImageKey() {
        return imageKey;
    }

    @Override
    public String toString() {
        String str = super.toString();
        return str.substring(0, str.length() - 1)
                + (imageKey != null ? " -- Image: " + imageKey : "")
                + "]";
    }
}
