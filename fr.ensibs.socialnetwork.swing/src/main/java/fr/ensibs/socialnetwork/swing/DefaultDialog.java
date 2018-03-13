package fr.ensibs.socialnetwork.swing;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * A generic application dialog
 *
 * @author Pascale Launay
 */
public class DefaultDialog extends JDialog {

    protected final SocialNetworkFrame socialNetworkFrame;

    /**
     * Constructor
     *
     * @param frame the parent frame
     * @param title the dialog title
     * @param modal true if the dialog is modal
     */
    public DefaultDialog(SocialNetworkFrame frame, String title, boolean modal) {
        super(frame, modal);
        this.socialNetworkFrame = frame;
        setTitle(title);
    }

    /**
     * Give a panel that contains the given component
     *
     * @param component a component
     * @return a panel that contains the component
     */
    protected JPanel makeComponentPanel(JComponent component) {
        JPanel panel = new JPanel();
        panel.add(component);
        return panel;
    }

}
