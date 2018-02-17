package fr.ensibs.socialnetwork.client.message;

import fr.ensibs.socialnetwork.client.OpenJmsTalker;
import fr.ensibs.socialnetwork.common.JmsType;
import fr.ensibs.socialnetwork.core.Message;
import fr.ensibs.socialnetwork.core.Publication;
import fr.ensibs.socialnetwork.logic.message.MessageManager;

import javax.jms.*;
import java.io.IOException;

public class JmsMessageManager implements MessageManager {
    public void send(Message mess) throws Exception {
        OpenJmsTalker server = OpenJmsTalker.getInstance();
        Destination targetPriv = server.getDestination(mess.getTarget()+"priv");
        Destination sourcePriv = server.getDestination(mess.getSource()+"priv");
        Session session = OpenJmsTalker.getInstance().getSession();
        TextMessage message = session.createTextMessage();
        message.setStringProperty(JmsType.SOURCE,mess.getSource());
        message.setStringProperty(JmsType.TARGET,mess.getTarget());
        message.setStringProperty(JmsType.CONTENT,mess.getMessage());
        message.setLongProperty(JmsType.DATE,mess.getDate());
        message.setIntProperty(JmsType.MESSAGE_TYPE, JmsType.PRIV_MESSAGE);
        MessageProducer sender = session.createProducer(targetPriv);
        sender.send(message);
    }

    public void publish(Publication publication) throws Exception {
        OpenJmsTalker server = OpenJmsTalker.getInstance();
        Destination sourcePriv = server.getDestination(publication.getSource()+"pub");
        Session session = OpenJmsTalker.getInstance().getSession();
        TextMessage message = session.createTextMessage();
        message.setStringProperty(JmsType.SOURCE,publication.getSource());
        message.setStringProperty(JmsType.CONTENT,publication.getMessage());
        message.setStringProperty(JmsType.IMG_KEY,publication.getImageKey());
        message.setLongProperty(JmsType.DATE,publication.getDate());
        message.setIntProperty(JmsType.MESSAGE_TYPE, JmsType.PUB_MESSAGE);
        MessageProducer sender = session.createProducer(sourcePriv);
        sender.send(message);
    }

    public void subscribe(String subscriber, String source) throws Exception {
        OpenJmsTalker server = OpenJmsTalker.getInstance();
        MessageConsumer c = server.getSession().createConsumer(server.getDestination(source+"pub"));
        server.addSubscribe(source,c);

    }

    public void unsubscribe(String subscriber, String source) throws Exception {
        OpenJmsTalker server = OpenJmsTalker.getInstance();
        server.removeSubscribe(source);
    }

    public void close() throws IOException {

    }
}
