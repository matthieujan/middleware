package fr.ensibs.socialnetwork.events;

import java.util.HashSet;
import java.util.Set;

/**
 * A controller that links together all source and listeners related to events
 * of a given type
 *
 * @param <T> the type of the events
 *
 * @author Pascale Launay
 */
public class EventController<T> {

    private final Set<EventSource<T>> sources;
    private final Set<EventListener<T>> listeners;

    /**
     * Constructor
     */
    public EventController() {
        sources = new HashSet<>();
        listeners = new HashSet<>();
    }

    /**
     * Add an event source and registers all known listeners to this source
     *
     * @param source an event source
     * @return true if the event source has been added
     */
    public boolean addEventSource(EventSource<T> source) {
        if (sources.add(source)) {
            for (EventListener<T> listener : listeners) {
                source.addEventListener(listener);
            }
            return true;
        }
        return false;
    }

    /**
     * Remove an event source and unregisters all known listeners from this
     * source
     *
     * @param source an event source
     * @return true if the event source has been removed
     */
    public boolean removeEventSource(EventSource<T> source) {
        if (sources.remove(source)) {
            for (EventListener<T> listener : listeners) {
                source.removeEventListener(listener);
            }
            return true;
        }
        return false;
    }

    /**
     * Add an event listener and registers it to all known sources
     *
     * @param listener an event listener
     * @return true if the event listener has been added
     */
    public boolean addEventListener(EventListener<T> listener) {
        if (listeners.add(listener)) {
            for (EventSource<T> source : sources) {
                source.addEventListener(listener);
            }
            return true;
        }
        return false;
    }

    /**
     * Remove an event listener and unregisters it from all known sources
     *
     * @param listener an event listener
     * @return true if the event listener has been removed
     */
    public boolean removeEventListener(EventListener<T> listener) {
        if (listeners.remove(listener)) {
            for (EventSource<T> source : sources) {
                source.removeEventListener(listener);
            }
            return true;
        }
        return false;
    }

}
