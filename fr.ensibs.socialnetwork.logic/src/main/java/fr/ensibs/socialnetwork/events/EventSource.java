package fr.ensibs.socialnetwork.events;

import java.util.HashSet;
import java.util.Set;

/**
 * A generic class to represent objects that fires events of some type
 *
 * @param <T> the type of the events
 *
 * @author Pascale Launay
 */
public class EventSource<T> {

    private final Set<EventListener<T>> listeners; // the listeners

    public EventSource() {
        listeners = new HashSet<>();
    }

    /**
     * Add a listener for the events fired by this source
     *
     * @param listener a listener
     */
    public void addEventListener(EventListener<T> listener) {
        listeners.add(listener);
    }

    /**
     * Remove a listener for the events fired by this source
     *
     * @param listener a listener
     */
    public void removeEventListener(EventListener<T> listener) {
        listeners.remove(listener);
    }

    /**
     * The method to be called by the descendant of this class to
     * asynchronousely notify the listeners that an event is fired
     *
     * @param event an event
     */
    protected void fireEvent(T event) {
        for (EventListener<T> listener : listeners) {
            new Thread() {
                @Override
                public void run() {
                    listener.onEvent(event);
                }
            }.start();
        }
    }

}
