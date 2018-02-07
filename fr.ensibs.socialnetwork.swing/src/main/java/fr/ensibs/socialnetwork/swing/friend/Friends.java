package fr.ensibs.socialnetwork.swing.friend;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A class representing the friends displayed on the friends panel
 *
 * @author Pascale Launay
 */
public class Friends {

    private final Map<String, Set<String>> friends;

    /**
     * Constructor
     */
    public Friends() {
        friends = new HashMap<>();
    }

    /**
     * Initialize the friends of the given user
     *
     * @param user a user
     * @param friends his friends' emails addresses
     */
    public void setFriends(String user, Set<String> friends) {
        this.friends.put(user, new HashSet<>());
        for (Set<String> set : this.friends.values()) {
            set.remove(user);
        }
        if (friends != null) {
            for (String friend : friends) {
                addFriend(user, friend);
                addFriend(friend, user);
            }
        }
    }

    /**
     * Add a friendship link between two users
     *
     * @param friend1 a user's email
     * @param friend2 another user's email
     * @return true if the link has been added
     */
    public boolean addFriendship(String friend1, String friend2) {
        if (addFriend(friend1, friend2)) {
            return addFriend(friend2, friend1);
        }
        return false;
    }

    /**
     * Remove a friendship link between two users
     *
     * @param friend1 a user's email
     * @param friend2 another user's email
     * @return true if the link has been removed
     */
    public boolean removeFriendship(String friend1, String friend2) {
        if (removeFriend(friend1, friend2)) {
            return removeFriend(friend2, friend1);
        }
        return false;
    }

    /**
     * Check whether two users are friends
     *
     * @param friend1 a user's email
     * @param friend2 another user's email
     * @return true if the users are friends
     */
    public boolean areFriends(String friend1, String friend2) {
        Set<String> set = friends.get(friend1);
        if (set != null) {
            return set.contains(friend2);
        }
        return false;
    }

    /**
     * Give the friends of a user
     *
     * @param user a user's email
     * @return the user's friends emails
     */
    public Set<String> getFriends(String user) {
        return friends.get(user);
    }

    /**
     * Add a friend
     *
     * @param friend1 a friend in the list
     * @param friend2 the new friend to be added to friend1
     * @return true if the friend has been added
     */
    private boolean addFriend(String friend1, String friend2) {
        Set<String> set = friends.get(friend1);
        if (set == null) {
            set = new HashSet<>();
            friends.put(friend1, set);
        }
        return set.add(friend2);
    }

    /**
     * Remove a friend
     *
     * @param friend1 a friend in the list
     * @param friend2 the friend to be removed from friend1
     * @return true if the friend has been removed
     */
    private boolean removeFriend(String friend1, String friend2) {
        Set<String> set = friends.get(friend1);
        if (set != null) {
            return set.remove(friend2);
        }
        return false;
    }
}
