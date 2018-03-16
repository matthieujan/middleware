package fr.ensibs.socialnetwork.client.recommandation;

import fr.ensibs.socialnetwork.logic.recommandation.RecommandationManager;

import java.io.IOException;
import java.util.Set;

public class JSRecommandationManager implements RecommandationManager {

    public JSRecommandationManager(){
    }

    @Override
    public void registerFriends(String user, Set<String> friendList) throws Exception {

    }

    @Override
    public void registerInterests(String user, Set<String> interestList) throws Exception {
    }

    @Override
    public Set<String> getFriends(String s) throws Exception {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
