package fr.ensibs.socialnetwork.events;

/**
 * Represents objects that reacts to events of some type
 *
 * @param <T> the type of the events
 *
 * @author Pascale Launay
 */
public interface EventListener<T> {

    /**
     * The method implementing the reaction to an event
     *
     * @param event an event
     */
    public void onEvent(T event);
}
