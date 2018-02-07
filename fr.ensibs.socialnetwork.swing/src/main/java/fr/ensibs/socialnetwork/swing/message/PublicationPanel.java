package fr.ensibs.socialnetwork.swing.message;

import fr.ensibs.socialnetwork.core.Publication;
import fr.ensibs.socialnetwork.swing.SocialNetworkFrame;
import fr.ensibs.socialnetwork.swing.image.ImagePanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * A panel to display a list of messages to/from a friend
 *
 * @author Pascale Launay
 */
public class PublicationPanel extends AbstractMessagePanel<Publication> {

    /**
     * Constructor
     *
     * @param frame the parent frame
     */
    public PublicationPanel(SocialNetworkFrame frame) {
        super(frame);
    }

    @Override
    protected PublicationRenderer makeMessageRenderer() {
        return new PublicationRenderer();
    }

    /**
     * Renderer for the publications in the list
     */
    class PublicationRenderer extends AbstractMessageRenderer {

        private final ImagePanel imagePanel;

        /**
         * Constructor
         */
        public PublicationRenderer() {
            imagePanel = new ImagePanel();
            imagePanel.setBackground(Color.white);
            contentPanel.add(imagePanel, BorderLayout.SOUTH);
        }

        @Override
        public JPanel getListCellRendererComponent(JList<? extends Publication> list, Publication publication, int idx, boolean selected, boolean hasFocus) {
            super.getListCellRendererComponent(list, publication, idx, selected, hasFocus);
            String text = publication.getMessage();
            String imageKey = publication.getImageKey();
            Image image = (imageKey != null ? socialNetworkFrame.getImage(imageKey) : null);
            if (text != null && !text.equals("")) {
                textArea.setPreferredSize(new Dimension(250, 40));
            } else {
                textArea.setPreferredSize(new Dimension(0, 0));
            }
            if (image != null) {
                imagePanel.setPreferredSize(new Dimension(250, 100));
                imagePanel.setImage(image);
            } else if (imageKey != null) {
                imagePanel.setPreferredSize(new Dimension(250, 30));
                imagePanel.removeImage();
                imagePanel.setText(imageKey);
            } else {
                imagePanel.setPreferredSize(new Dimension(0, 0));
                imagePanel.removeImage();
            }
            return this;
        }
    }
}
