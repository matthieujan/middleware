package fr.ensibs.socialnetwork.client;

import fr.ensibs.socialnetwork.client.event.FriendEventCaller;
import fr.ensibs.socialnetwork.client.event.MessageEventCaller;
import fr.ensibs.socialnetwork.common.JmsType;
import fr.ensibs.socialnetwork.configuration.ConfigurationManager;
import fr.ensibs.socialnetwork.events.EventControllerFactory;
import fr.ensibs.socialnetwork.logic.friend.FriendEvent;
import fr.ensibs.socialnetwork.logic.message.MessageEvent;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class OpenJmsTalker{

    private static OpenJmsTalker instance = null;
    private String server_host;
    private int jms_port;

    public static OpenJmsTalker getInstance() throws NamingException {
        if(instance == null){
            instance = new OpenJmsTalker();
        }
        return instance;
    }


    private ConfigurationManager conf;
    private Context context;
    private Hashtable properties;
    private Connection connection;
    private Session session;
    private Hashtable<String,MessageConsumer> consumers;

    private FriendEventCaller friendEventCaller;
    private MessageEventCaller messageEventCaller;

    private OpenJmsTalker() throws NamingException {
        conf = ConfigurationManager.getInstance();
        server_host = conf.getProperty(ConfigurationManager.SERVER_HOST,"localhost");
        jms_port = conf.getIntegerProperty(ConfigurationManager.JMS_PORT,5001);
        properties = new Hashtable();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
        properties.put(Context.PROVIDER_URL, "tcp://"+server_host+":"+jms_port+"/");
        context = new InitialContext(properties);
        consumers = new Hashtable<String, MessageConsumer>();
        EventControllerFactory ecf = EventControllerFactory.getInstance();
        friendEventCaller = new FriendEventCaller();
        messageEventCaller = new MessageEventCaller();
        ecf.makeFriendEventController().addEventSource(friendEventCaller);
        ecf.makeMessageEventController().addEventSource(messageEventCaller);
    }

    public FriendEventCaller getFriendEventCaller() {
        return friendEventCaller;
    }

    public MessageEventCaller getMessageEventCaller(){
        return messageEventCaller;
    }

    public boolean isLogged(){
        return !(connection == null);
    }

    public boolean login(String email, String password){
        if(!isLogged()){
            ConnectionFactory cf = null;
            try {
                cf = (ConnectionFactory) context.lookup("ConnectionFactory");
                connection = cf.createConnection(email,password);
                session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
                connection.start();
                MessageConsumer consumer = session.createConsumer(getDestination(email+"priv"));
                consumer.setMessageListener(new MessageListener() {
                    public void onMessage(Message message) {
                        try {
                            OpenJmsTalker.handleMessage(message);
                        } catch (JMSException e) {
                            e.printStackTrace();
                        } catch (NamingException e) {
                            e.printStackTrace();
                        }
                    }
                });
                consumers.put(email,consumer);
            } catch (NamingException e) {
                e.printStackTrace();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("Already logged");
        }
        return isLogged();
    }


    public boolean logout(){
        try {
            session.close();
            connection.stop();
            connection.close();
            session = null;
            connection = null;
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return isLogged();
    }

    public Connection getConnection() {
        return connection;
    }

    public Session getSession() {
        return session;
    }

    public Destination getDestination(String destination) throws NamingException {
        return (Destination) context.lookup(destination);
    }

    private static void handleMessage(Message message) throws JMSException, NamingException {
        int type = message.getIntProperty(JmsType.MESSAGE_TYPE);
        String source;
        String target;
        long date;
        String content;
        switch (type){
            case JmsType.FRIEND_REQUEST:
                source = message.getStringProperty(JmsType.SOURCE);
                target = message.getStringProperty(JmsType.TARGET);
                FriendEvent feReq = new FriendEvent(FriendEvent.REQUEST,source,target);
                OpenJmsTalker.getInstance().getFriendEventCaller().fire(feReq);
                break;
            case JmsType.FRIEND_RESPONSE:
                source = message.getStringProperty(JmsType.SOURCE);
                target = message.getStringProperty(JmsType.TARGET);
                FriendEvent feResp = new FriendEvent(FriendEvent.ACCEPT,source,target);
                OpenJmsTalker.getInstance().getFriendEventCaller().fire(feResp);
                break;
            case JmsType.FRIEND_BAN:
                source = message.getStringProperty(JmsType.SOURCE);
                target = message.getStringProperty(JmsType.TARGET);
                FriendEvent feBan = new FriendEvent(FriendEvent.BAN,source,target);
                OpenJmsTalker.getInstance().getFriendEventCaller().fire(feBan);
                break;
            case JmsType.PRIV_MESSAGE:
                source = message.getStringProperty(JmsType.SOURCE);
                target = message.getStringProperty(JmsType.TARGET);
                content = message.getStringProperty(JmsType.CONTENT);
                date = message.getLongProperty(JmsType.DATE);
                fr.ensibs.socialnetwork.core.Message mess = new fr.ensibs.socialnetwork.core.Message(date,source,target,content);
                MessageEvent privMess = new MessageEvent(mess);
                OpenJmsTalker.getInstance().getMessageEventCaller().fire(privMess);
                break;
            case JmsType.PUB_MESSAGE:
                source = message.getStringProperty(JmsType.SOURCE);
                content = message.getStringProperty(JmsType.CONTENT);
                date = message.getLongProperty(JmsType.DATE);
                fr.ensibs.socialnetwork.core.Publication pub = new fr.ensibs.socialnetwork.core.Publication(date,source,content,message.getStringProperty(JmsType.IMG_KEY));
                MessageEvent pubMess = new MessageEvent(pub);
                OpenJmsTalker.getInstance().getMessageEventCaller().fire(pubMess);
                break;
            default:
                System.out.println("Type non reconnu");
                break;
        }
    }

    public void addSubscribe(String source, MessageConsumer consumer) throws JMSException {
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                try {
                    OpenJmsTalker.handleMessage(message);
                } catch (JMSException e) {
                    e.printStackTrace();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        });
        consumers.put(source,consumer);
    }

    public void removeSubscribe(String source) throws JMSException {
        MessageConsumer c =consumers.get(source);
        c.close();
        consumers.remove(source);

    }
}
