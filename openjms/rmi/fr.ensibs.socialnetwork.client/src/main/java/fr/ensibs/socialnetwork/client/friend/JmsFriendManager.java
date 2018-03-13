package fr.ensibs.socialnetwork.client.friend;

import fr.ensibs.socialnetwork.client.OpenJmsTalker;
import fr.ensibs.socialnetwork.common.JmsType;
import fr.ensibs.socialnetwork.configuration.ConfigurationManager;
import fr.ensibs.socialnetwork.logic.friend.FriendManager;

import javax.jms.*;
import javax.naming.NamingException;
import java.io.IOException;

public class JmsFriendManager implements FriendManager {

    public void requestFriend(String source, String target) throws Exception {
        sendMessageTo(source,target,JmsType.FRIEND_REQUEST);
    }

    public void acceptFriend(String source, String target) throws Exception {
        sendMessageTo(source,target,JmsType.FRIEND_RESPONSE);
    }

    public void banFriend(String source, String target) throws Exception {
        sendMessageTo(source,target,JmsType.FRIEND_BAN);
    }

    public void close() throws IOException {
    }

    private void sendMessageTo(String source,String target,int jmsType) throws Exception {
        OpenJmsTalker server = OpenJmsTalker.getInstance();
        Destination targetPriv = server.getDestination(target+"priv");
        Destination sourcePriv = server.getDestination(source+"priv");
        Session session = OpenJmsTalker.getInstance().getSession();

        TextMessage message = session.createTextMessage();
        message.setStringProperty(JmsType.SOURCE,source);
        message.setStringProperty(JmsType.TARGET,target);
        message.setIntProperty(JmsType.MESSAGE_TYPE, jmsType);
        MessageProducer sender = session.createProducer(targetPriv);
        sender.send(message);
    }
}
