package fr.ensibs.socialnetwork.swing.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.InputStream;
import javax.swing.JPanel;

/**
 * A panel to display an image loaded from an input stream
 *
 * @author Pascale Launay
 */
public class ImagePanel extends JPanel {

    private String text;
    private Image image; // the image displayed in this panel
    private double imageRatio; // the image's ratio (width/height)

    /**
     * Initialize the image to be displayed in this panel
     *
     * @param in the image input stream
     */
    public ImagePanel(InputStream in) {
        this(ImageFactory.getInstance().makeImage(in));
    }

    /**
     * Initialize the image to be displayed in this panel
     *
     * @param image the image to be displayed
     */
    public ImagePanel(Image image) {
        this();
        initImage(image);
    }

    /**
     * Constructor
     */
    public ImagePanel() {
        setBackground(Color.white);
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Set the image to be displayed in this panel
     *
     * @param in the image input stream
     */
    public void setImage(InputStream in) {
        initImage(ImageFactory.getInstance().makeImage(in));
        repaint();
    }

    /**
     * Set the image to be displayed in this panel
     *
     * @param image the image to be displayed
     */
    public void setImage(Image image) {
        initImage(image);
        repaint();
    }

    public void removeImage() {
        this.image = null;
        this.text = null;
        repaint();
    }

    /**
     * Give this panel's image
     *
     * @return this panel's image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Set the image to be displayed in this panel
     *
     * @param image the image to be displayed
     */
    private void initImage(Image image) {
        this.image = image;
        if (image != null) {
            this.imageRatio = image.getWidth(this) * 1.0 / image.getHeight(this);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            // compute the best image dimensions preserving the image ratio
            double width = Math.min(getWidth(), getHeight() * imageRatio);
            double height = width / imageRatio;
            // draw the image
            g.drawImage(image, (int) (getWidth() - width) / 2, (int) (getHeight() - height) / 2, (int) width, (int) height, this);
        } else if (text != null) {
            g.drawString(text, 10, 10);
        }
    }
}
