package fr.ensibs.socialnetwork.client;

import fr.ensibs.socialnetwork.common.RMICallback;
import fr.ensibs.socialnetwork.core.Profile;
import fr.ensibs.socialnetwork.events.EventController;
import fr.ensibs.socialnetwork.events.EventControllerFactory;
import fr.ensibs.socialnetwork.events.EventSource;
import fr.ensibs.socialnetwork.logic.profile.ProfileEvent;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class implements the callback remote object used to propagate profile modification.
 */
public class RMICallbackImpl extends UnicastRemoteObject implements RMICallback {

    //Our custom eventSource to fire events
    private final CallbackEventSource eventSource;

    /**
     * Simple constructor to create our event source
     */
    public RMICallbackImpl() throws RemoteException {
        super();
        eventSource = new CallbackEventSource();
    }

    /**
     * Fire a ProfileEvent
     * @param type the profile event type (PSEUDO_CHANGED|INTEREST_CHANGED|BOTH_CHANGED)
     * @param profile the new profile
     * @throws RemoteException
     */
    public void fireEvent(int type, Profile profile) throws RemoteException {
        //A bit ugly : we add the event source to the profile event controller, fire the event, then remove it from the list
        EventController<ProfileEvent> profileEventController = EventControllerFactory.getInstance().makeProfileEventController();
        profileEventController.addEventSource(eventSource);
        eventSource.fireEvent(new ProfileEvent(type,profile));
        profileEventController.removeEventSource(eventSource);
    }

    /**
     * Getter for the eventSource
     * @return the event source
     */
    public EventSource<ProfileEvent> getEventSource(){
        return eventSource;
    }

    //Custom class definition for the event source to be able to fire ProfileEvents
    private class CallbackEventSource extends EventSource<ProfileEvent>{
        @Override
        protected void fireEvent(ProfileEvent event) {
            super.fireEvent(event);
        }
    }
}
