package fr.ensibs.socialnetwork.swing;

import fr.ensibs.socialnetwork.core.Profile;
import java.awt.Color;
import java.io.Closeable;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A generic application panel that can be opened/closed
 *
 * @author Pascale Launay
 */
public class DefaultPanel extends JPanel implements Closeable {

    private boolean closed = true; // true if the application panel is closed
    protected Profile me; // my email address
    protected SocialNetworkFrame socialNetworkFrame;

    public DefaultPanel(SocialNetworkFrame frame) {
        this.socialNetworkFrame = frame;
    }

    /**
     * Return true if the application panel is closed
     *
     * @return true if the application panel is closed
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Open this application panel
     *
     * @param me my email address
     */
    public synchronized void open(Profile me) {
        if (closed) {
            closed = false;
            this.me = me;
        }
    }

    /**
     * Close this application panel
     */
    @Override
    public synchronized void close() {
        if (!closed) {
            closed = true;
            this.me = null;
        }
    }

    /**
     * Make a panel containing the given component
     *
     * @param component a component
     * @return a panel containing the component
     */
    protected JPanel makeComponentPanel(JComponent component) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.add(component);
        return panel;
    }
}
