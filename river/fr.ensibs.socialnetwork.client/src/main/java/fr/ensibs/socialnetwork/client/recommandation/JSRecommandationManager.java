package fr.ensibs.socialnetwork.client.recommandation;

import fr.ensibs.socialnetwork.client.RiverTalker;
import fr.ensibs.socialnetwork.client.event.RecommandationEventCaller;
import fr.ensibs.socialnetwork.common.RiverType;
import fr.ensibs.socialnetwork.events.EventControllerFactory;
import fr.ensibs.socialnetwork.logic.recommandation.RecommandationEvent;
import fr.ensibs.socialnetwork.logic.recommandation.RecommandationManager;

import java.io.IOException;
import java.rmi.MarshalledObject;
import java.util.*;

public class JSRecommandationManager implements RecommandationManager {

    public JSRecommandationManager() {
    }

    @Override
    public void registerFriends(String source, Set<String> set) throws Exception {
        int type = RecommandationEvent.COMMON_FRIENDS;
        Set<String> oldFriends = getFriends(source);
        for(String f : set){
            if(!oldFriends.contains(f)){
               ListEntry template = new ListEntry();
               template.value = f;
               template.type = RiverType.FRIEND;
               template.source = source;

               MarshalledObject<ListEntry> handback = new MarshalledObject<>(template);
               template.source = null;
               RecommandationEventCaller rec = new RecommandationEventCaller();
               EventControllerFactory.getInstance().makeRecommendationEventController().addEventSource(rec);

               RiverTalker.getInstance().getSpace().notify(template,null,rec.getStub(),Long.MAX_VALUE,handback);
            }
        }

        RiverTalker.getInstance().updateList(source, set, type);
    }

    @Override
    public void registerInterests(String source, Set<String> set) throws Exception {
        int type = RecommandationEvent.COMMON_INTERESTS;

        Set<String> alone = new HashSet<>();

        ListEntry template = new ListEntry();
        template.value = source;
        template.type = RiverType.INTER_LIST;
        ListEntry entry = (ListEntry) RiverTalker.getInstance().getSpace().takeIfExists(template, null, Long.MAX_VALUE);
        if (entry == null) {
            template.list = new String[0];
            entry = template;
        }

        Set<String> oldInter = RiverTalker.getInstance().arrayToSet(entry.list);

        for (String interest : set) {
            //On s'abonne ...
            if (!oldInter.contains(interest)) {
                // D'abord on s'ajoute dans la liste
                ListEntry newTemplate = new ListEntry();
                newTemplate.value = interest;
                newTemplate.type = RecommandationEvent.COMMON_INTERESTS;
                ListEntry newEntry = (ListEntry) RiverTalker.getInstance().getSpace().takeIfExists(newTemplate, null, Long.MAX_VALUE);
                if (newEntry == null) { //Si l'entry est nulle, la liste n'a jamais été crée, donc on initialise avec un tableau vide
                    newEntry = newTemplate;
                    newEntry.list = new String[0];
                }

                Set<String> subs = RiverTalker.getInstance().arrayToSet(newEntry.list);
                subs.add(source);

                newEntry.list = new String[subs.size()];
                int i = 0;
                for (String s : subs) {
                    newEntry.list[i] = s;
                    i++;
                }

                RiverTalker.getInstance().getSpace().write(newEntry, null, Long.MAX_VALUE);

                // Ensuite on set le notify;
                newTemplate.list = null;
                newTemplate.source = source;
                MarshalledObject<ListEntry> mo = new MarshalledObject<>(newTemplate);
                newTemplate.source = null;

                RecommandationEventCaller rec = new RecommandationEventCaller();
                EventControllerFactory.getInstance().makeRecommendationEventController().addEventSource(rec);


                RiverTalker.getInstance().getSpace().notify(newTemplate, null, rec.getStub(), Long.MAX_VALUE, mo);
                RiverTalker.getInstance().updateList(source, set, RiverType.INTER_LIST);
            }
        }

    }

    @Override
    public Set<String> getFriends(String s) throws Exception {
        return RiverTalker.getInstance().getList(s, RiverType.FRIEND);
    }

    @Override
    public void close() throws IOException {

    }
}