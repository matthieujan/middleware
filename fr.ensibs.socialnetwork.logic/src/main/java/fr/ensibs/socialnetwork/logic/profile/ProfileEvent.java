package fr.ensibs.socialnetwork.logic.profile;

import fr.ensibs.socialnetwork.core.Profile;

/**
 * An event related to changes in users' profiles
 *
 * @author Pascale Launay
 */
public class ProfileEvent {

    public static final int PSEUDO_CHANGED = 1;
    public static final int INTEREST_CHANGED = 2;
    public static final int BOTH_CHANGED = 3;

    private final int type; // the event type (PSEUDO_CHANGED|INTEREST_CHANGED)
    private final Profile profile; // the user's profile

    /**
     * Constructor
     *
     * @param type the event type (PSEUDO_CHANGED|INTEREST_CHANGED)
     * @param profile the user's profile
     */
    public ProfileEvent(int type, Profile profile) {
        this.type = type;
        this.profile = profile;
    }

    /**
     * Give the event type (PSEUDO_CHANGED|INTEREST_CHANGED)
     *
     * @return the event type (PSEUDO_CHANGED|INTEREST_CHANGED)
     */
    public int getType() {
        return type;
    }

    /**
     * Give the user's profile
     *
     * @return the user's profile
     */
    public Profile getProfile() {
        return profile;
    }

    @Override
    public String toString() {
        return "{ type: " + typeToString(type)
                + ", profile: " + profile + " }";
    }

    /**
     * Give a string representation of the given type
     *
     * @param type the type
     * @return the string representation of the type
     */
    private String typeToString(int type) {
        switch (type) {
            case PSEUDO_CHANGED:
                return "PSEUDO_CHANGED";
            case INTEREST_CHANGED:
                return "INTEREST_CHANGED";
            case BOTH_CHANGED:
                return "BOTH_CHANGED";
        }
        return "UNKNOWN";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ProfileEvent) {
            return hashCode() == o.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
