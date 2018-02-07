package fr.ensibs.socialnetwork.logic.friend;

/**
 * An event fired when a user request an other as friend (REQUEST), when a user
 * accepts the other as friend (ACCEPT) or when a user bans the other from his
 * friends (BAN)
 *
 * @author Pascale Launay
 */
public class FriendEvent {

    public static final int REQUEST = 1;
    public static final int ACCEPT = 2;
    public static final int BAN = 3;

    private final int type; // the event type (REQUEST|ACCEPT|BAN)
    private final String source; // the email address of the user source of this event
    private final String target; // the email address of the user target of this event

    /**
     * Constructor
     *
     * @param type the event type (REQUEST|ACCEPT|BAN)
     * @param source the email address of the user source of this event
     * @param target the email address of the user target of this event
     */
    public FriendEvent(int type, String source, String target) {
        this.type = type;
        this.source = source;
        this.target = target;
    }

    /**
     * Give the event type (REQUEST|ACCEPT|BAN)
     *
     * @return the event type (REQUEST|ACCEPT|BAN)
     */
    public int getType() {
        return type;
    }

    /**
     * Give the email address of the user source of this event
     *
     * @return the email address of the user source of this event
     */
    public String getSource() {
        return source;
    }

    /**
     * Give the email address of the user source of this event
     *
     * @return the email address of the user source of this event
     */
    public String getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "{ type: " + typeToString(type)
                + ", source: " + source
                + ", target: " + target + " }";
    }

    /**
     * Give a string representation of the given type
     *
     * @param type the type
     * @return the string representation of the type
     */
    private String typeToString(int type) {
        switch (type) {
            case ACCEPT:
                return "ACCEPT";
            case REQUEST:
                return "REQUEST";
            case BAN:
                return "BAN";
        }
        return "UNKNOWN";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FriendEvent) {
            return hashCode() == o.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
