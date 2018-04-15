package fr.ensibs.socialnetwork.client;

import fr.ensibs.river.River;
import fr.ensibs.socialnetwork.client.recommandation.ListEntry;
import fr.ensibs.socialnetwork.configuration.ConfigurationManager;
import net.jini.space.JavaSpace;

import java.io.IOException;
import java.util.*;

public class RiverTalker {
    public static RiverTalker instance;

    public RiverTalker(){};

    public static RiverTalker getInstance(){
        if(instance == null){
            instance = new RiverTalker();
        }
        return instance;
    }

    public void updateList(String user, Set<String> list, int type) throws Exception {
        ListEntry template = new ListEntry();
        template.value = user;
        template.type = type;
        ListEntry entry = (ListEntry) getSpace().takeIfExists(template,null,Long.MAX_VALUE);
        if(entry == null){
            entry = template;
        }
        entry.list = setToArray(list);
        getSpace().write(entry,null,Long.MAX_VALUE);
    }

    public Set<String> getList(String user, int type) throws Exception {
        ListEntry template = new ListEntry();
        template.value = user;
        template.type = type;
        ListEntry result = (ListEntry) getSpace().readIfExists(template,null,Long.MAX_VALUE);

        Set<String> res;
        if(result != null && result.list != null){
            res = arrayToSet(result.list);
        }else{
            res = Collections.EMPTY_SET;
        }
        return res;
    }

    public void close() throws IOException {

    }

    public JavaSpace getSpace() throws Exception {
        String host = ConfigurationManager.getInstance().getProperty("SERVER_HOST","localhost");
        int port = ConfigurationManager.getInstance().getIntegerProperty("RIVER_PORT",5003);

        River r = new River(host,port);
        return r.lookup();
    }

    public Set<String> arrayToSet(String[] list){
        List<String> ret = new ArrayList<String>();
        for(int i = 0;i<list.length;i++){
            if(list[i] != null){
                ret.add(list[i]);
            }
        }
        return new HashSet<>(ret);
    }

    public String[] setToArray(Set<String> set){
        return set.toArray(new String[0]);
    }
}
