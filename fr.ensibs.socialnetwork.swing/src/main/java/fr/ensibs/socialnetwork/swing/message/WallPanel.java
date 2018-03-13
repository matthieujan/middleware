package fr.ensibs.socialnetwork.swing.message;

import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.core.Publication;
import fr.ensibs.socialnetwork.swing.DefaultPanel;
import fr.ensibs.socialnetwork.swing.SocialNetworkFrame;
import static fr.ensibs.socialnetwork.swing.SocialNetworkFrame.DEBUG;
import fr.ensibs.socialnetwork.swing.image.ImageFactory;
import fr.ensibs.socialnetwork.swing.image.ImagePanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import static javax.swing.border.BevelBorder.LOWERED;
import javax.swing.border.Border;

/**
 * A panel allowing the user to post images and text messages and to display his
 * friends' publications
 *
 * @author Pascale Launay
 */
public class WallPanel extends DefaultPanel {

    private static final Border BEVEL_BORDER = BorderFactory.createBevelBorder(LOWERED);
    private static final String PUBLISH_TEXT = "Publish";
    private static final String ADD_PICTURE_TEXT = "Add Picture";
    private static final String LOAD_ERROR_TEXT = "Cannot load image";

    private final JTextArea textArea; // to input the text content
    private final JButton publishButton; // button to publish text and/or image
    private final JButton pictureButton; // button to open a file chooser to select an image
    private final PublicationPanel publicationPanel; // the panel containing the list of publication messages
    private final ImagePanel imagePanel; // contains the image to be sent

    /**
     * Constructor
     *
     * @param frame the parent frame
     */
    public WallPanel(SocialNetworkFrame frame) {
        super(frame);
        // create the panel's widgets
        textArea = new JTextArea();
        textArea.setBorder(BEVEL_BORDER);
        textArea.setBackground(new Color(250, 250, 250));
        textArea.setEnabled(false);
        imagePanel = new ImagePanel();
        publishButton = new JButton(new AbstractAction(PUBLISH_TEXT) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                publish();
            }
        });
        publishButton.setEnabled(false);
        pictureButton = new JButton(new AbstractAction(ADD_PICTURE_TEXT) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addPicture();
            }
        });
        pictureButton.setEnabled(false);

        publicationPanel = new PublicationPanel(frame);

        fillPanel();
    }

    @Override
    public synchronized void close() {
        super.close();
        if (isClosed()) {
            textArea.setEnabled(false);
            pictureButton.setEnabled(false);
            publishButton.setEnabled(false);
            publicationPanel.clear();
        }
    }

    @Override
    public synchronized void open(Profile me) {
        super.open(me);
        if (!isClosed()) {
            publicationPanel.open(me);
            textArea.setEnabled(true);
            pictureButton.setEnabled(true);
            publishButton.setEnabled(true);
            Set<Publication> publications = new TreeSet<>();
            List<Publication> list = socialNetworkFrame.getPublications(me.getEmail());
            if (list != null) {
                publications.addAll(list);
            }
            Set<String> myFriends = socialNetworkFrame.getFriends();
            if (myFriends != null) {
                for (String friend : myFriends) {
                    list = socialNetworkFrame.getPublications(friend);
                    if (list != null) {
                        publications.addAll(list);
                    }
                }
            }
            publicationPanel.setMessages(publications);
        }
    }

    /**
     * Add a message published by me or a friend to the wall
     *
     * @param publication the publication message
     */
    public void addMessage(Publication publication) {
        publicationPanel.addMessage(publication);
    }

    /**
     * Called when the "Publish" button is clicked
     */
    private void publish() {
        String text = textArea.getText();
        Image image = imagePanel.getImage();
        if ((text != null && !text.equals("")) || image != null) {
            String imageKey = null;
            if (image != null) {
                if (DEBUG) {
                    System.out.println("[swing] WallPanel#addImage image=" + image.getWidth(null) + "x" + image.getHeight(null));
                }
                imageKey = socialNetworkFrame.addImage(image);
            }
            if ((text != null && !text.equals("")) || imageKey != null) {
                if (DEBUG) {
                    System.out.println("[swing] WallPanel#publish text=" + text
                            + ", imageKey=" + imageKey);
                }
                Publication publication = new Publication(System.currentTimeMillis(), me.getEmail(), text, imageKey);
                socialNetworkFrame.sendMessage(publication);
                textArea.setText("");
            }
            imagePanel.removeImage();
        }
    }

    /**
     * Called when the "Add Picture" button is clicked
     */
    private void addPicture() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                InputStream in = new FileInputStream(fileChooser.getSelectedFile());
                Image image = ImageFactory.getInstance().makeImage(in);
                if (image != null) {
                    imagePanel.setMinimumSize(new Dimension(100, 100));
                    imagePanel.setImage(image);
                } else {
                    JOptionPane.showMessageDialog(this, LOAD_ERROR_TEXT);
                }
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, LOAD_ERROR_TEXT);
            }
        }
    }

    /**
     * Put widgets on the main panel
     */
    private void fillPanel() {
        setLayout(new BorderLayout());

        // north: text area & buttons
        JPanel northPanel = new JPanel(new BorderLayout());
        textArea.setPreferredSize(new Dimension(250, 60));
        northPanel.add(textArea, BorderLayout.CENTER);
        northPanel.add(makeComponentPanel(publishButton), BorderLayout.EAST);
        JPanel picturePanel = new JPanel(new BorderLayout());
        picturePanel.add(makeComponentPanel(pictureButton), BorderLayout.WEST);
        picturePanel.add(imagePanel, BorderLayout.CENTER);
        northPanel.add(picturePanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        // center: friends publications
        add(publicationPanel, BorderLayout.CENTER);
    }
}
