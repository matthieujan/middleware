package fr.ensibs.socialnetwork.client.event;

import fr.ensibs.socialnetwork.client.RiverTalker;
import fr.ensibs.socialnetwork.client.recommandation.ListEntry;
import fr.ensibs.socialnetwork.common.RiverType;
import fr.ensibs.socialnetwork.events.EventSource;
import fr.ensibs.socialnetwork.logic.recommandation.RecommandationEvent;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

public class RecommandationEventCaller extends EventSource<RecommandationEvent> implements RemoteEventListener {

    public RecommandationEventCaller(){

    }

    @Override
    public void notify(RemoteEvent remoteEvent) throws UnknownEventException, RemoteException {
        try {
            ListEntry handback = (ListEntry) remoteEvent.getRegistrationObject().get();
            String source = handback.source;
            Set<String> myFriends = RiverTalker.getInstance().getList(handback.source,RiverType.FRIEND);
            handback.source = null;
            ListEntry entry = (ListEntry) RiverTalker.getInstance().getSpace().readIfExists(handback,null,Long.MAX_VALUE);
            for(String s : entry.list){
                if(!myFriends.contains(s) && !s.equals(source)){
                    fireEvent(new RecommandationEvent(handback.type,source,s));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RemoteEventListener getStub() throws RemoteException {
        return (RemoteEventListener) UnicastRemoteObject.exportObject(this,0);
    }

    public void fire(RecommandationEvent e){
        this.fireEvent(e);
    }
}
