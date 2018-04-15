package fr.ensibs.socialnetwork.client.event;

import fr.ensibs.socialnetwork.events.EventSource;
import fr.ensibs.socialnetwork.logic.message.MessageEvent;

public class MessageEventCaller extends EventSource<MessageEvent>{
    public void fire(MessageEvent event){
        this.fireEvent(event);
    }
}
