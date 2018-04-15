package fr.ensibs.socialnetwork.common;

public abstract class JmsType {
    public static final String MESSAGE_TYPE = "message_type";
    public static final String SOURCE = "source";
    public static final String TARGET = "target";
    public static final String DATE = "date";
    public static final String CONTENT = "content";
    public static final String IMG_KEY = "img_key";

    public static final int FRIEND_REQUEST = 1;
    public static final int FRIEND_RESPONSE = 2;
    public static final int FRIEND_BAN = 3;
    public static final int PUB_MESSAGE = 4;
    public static final int PRIV_MESSAGE = 5;


    public static final String SENDER = "sender";
}
