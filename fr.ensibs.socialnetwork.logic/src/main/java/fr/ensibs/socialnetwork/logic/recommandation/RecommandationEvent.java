package fr.ensibs.socialnetwork.logic.recommandation;

/**
 * An event fired when another user with common friends or interests is found
 *
 * @author Pascale Launay
 */
public class RecommandationEvent {

    public static final int COMMON_FRIENDS = 1;
    public static final int COMMON_INTERESTS = 2;

    private final int type; // the event type (REQUEST|ACCEPT|BAN)
    private final String friend1; // the user source of this event
    private final String friend2; // the user target of this event

    /**
     * Constructor
     *
     * @param type the event type (REQUEST|ACCEPT|BAN)
     * @param friend1 the user source of this event
     * @param friend2 the user target of this event
     */
    public RecommandationEvent(int type, String friend1, String friend2) {
        this.type = type;
        this.friend1 = friend1;
        this.friend2 = friend2;
    }

    /**
     * Give the event type
     *
     * @return the event type (REQUEST|ACCEPT|BAN)
     */
    public int getType() {
        return type;
    }

    /**
     * Give the user source of this event
     *
     * @return the user source of this event
     */
    public String getFriend1() {
        return friend1;
    }

    /**
     * Give the user target of this event
     *
     * @return the user target of this event
     */
    public String getFriend2() {
        return friend2;
    }

    @Override
    public String toString() {
        return "{ type: " + typeToString(type)
                + ", friend1: " + friend1
                + ", friend2: " + friend2 + " }";
    }

    private String typeToString(int type) {
        switch (type) {
            case COMMON_FRIENDS:
                return "COMMON_FRIENDS";
            case COMMON_INTERESTS:
                return "COMMON_INTERESTS";
        }
        return "UNKNOWN";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RecommandationEvent) {
            return hashCode() == o.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
