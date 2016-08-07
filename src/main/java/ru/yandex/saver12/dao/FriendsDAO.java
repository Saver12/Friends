package ru.yandex.saver12.dao;

import ru.yandex.saver12.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Defines methods for all FriendsDAO implementations.
 *
 * @author Sergey Ermushin
 */

public interface FriendsDAO {

    /**
     * Creates Friend Request.
     *
     * @param id   Requested User's id
     * @param myId Authorized User's id
     * @throws SQLException if a database access error occurs
     */
    void addFriend(int id, int myId) throws SQLException;

    /**
     * Deletes Friend.
     *
     * @param id   Requested User's id
     * @param myId Authorized User's id
     * @throws SQLException if a database access error occurs
     */
    void deleteFriend(int id, int myId) throws SQLException;

    /**
     * Declines Friend Request.
     *
     * @param id   Requested User's id
     * @param myId Authorized User's id
     * @throws SQLException if a database access error occurs
     */
    void declineRequest(int id, int myId) throws SQLException;

    /**
     * Accepts Friend Request.
     *
     * @param id   Requested User's id
     * @param myId Authorized User's id
     * @throws SQLException if a database access error occurs
     */
    void acceptRequest(int id, int myId) throws SQLException;

    /**
     * Gets list of User's friends.
     *
     * @param id User's id
     * @return list of User's friends
     * @throws SQLException if a database access error occurs
     */
    List<User> getFriendsList(int id) throws SQLException;

    /**
     * Gets list of User's requests.
     *
     * @param id User's id
     * @return list of User's requests
     * @throws SQLException if a database access error occurs
     */
    List<User> getRequestsList(int id) throws SQLException;
}
