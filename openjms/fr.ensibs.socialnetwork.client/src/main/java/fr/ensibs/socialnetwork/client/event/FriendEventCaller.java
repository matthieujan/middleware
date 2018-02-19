package fr.ensibs.socialnetwork.client.event;

import fr.ensibs.socialnetwork.events.EventSource;
import fr.ensibs.socialnetwork.logic.friend.FriendEvent;

public class FriendEventCaller extends EventSource<FriendEvent> {
    public void fire(FriendEvent event){
        this.fireEvent(event);
    }
}
