package fr.ensibs.socialnetwork.core;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/**
 * A user's profile: pseudo and interests
 *
 * @author Pascale Launay
 */
public class Profile implements Comparable<Profile>, Serializable {

    private final String email; // the user's email address (unique)
    private String pseudo; // the user's nickname
    private final Set<String> interests; // the user's interests (raw strings)

    /**
     * Constructor.
     *
     * @param email the user's email address
     * @param pseudo the user's name
     */
    public Profile(String email, String pseudo) {
        this.email = email;
        this.pseudo = pseudo;
        this.interests = new TreeSet<>();
    }

    /**
     * Give the user's email address
     *
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Give the user's pseudo
     *
     * @return the user's pseudo
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * Change the user's pseudo
     *
     * @param pseudo the new user's pseudo
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * Set the user's interests
     *
     * @param interests set of raw strings representing a user's interests (e.g.
     * music, sport,...)
     * @return true if this interests have been set
     */
    public boolean setInterests(Set<String> interests) {
        boolean ret = true;
        this.interests.clear();
        for (String interest : interests) {
            ret = this.interests.add(interest.toLowerCase()) && ret;
        }
        return ret;
    }

    /**
     * Give the user's interests
     *
     * @return the user's interests
     */
    public Set<String> getInterests() {
        return interests;
    }

    @Override
    public int compareTo(Profile other) {
        int cmp = pseudo.compareTo(other.pseudo);
        if (cmp == 0) {
            cmp = email.compareTo(other.email);
        }
        return cmp;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Profile) {
            return hashCode() == other.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
        return "[" + email + ", " + pseudo + ", " + interests + "]";
    }
}
