package ru.yandex.saver12.dao.impl;

import ru.yandex.saver12.dao.FriendsDAO;
import ru.yandex.saver12.model.User;
import ru.yandex.saver12.util.DBConnectionManager;
import ru.yandex.saver12.util.UtilClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic JDBC FriendsDAO implementation.
 *
 * @author Sergey Ermushin
 */

public class FriendsDAOImpl implements FriendsDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFriend(int id, int myId) throws SQLException {
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementAdd(con, id, myId)) {
            ps.executeUpdate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFriend(int id, int myId) throws SQLException {
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementDelete(con, id, myId)) {
            ps.executeUpdate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void declineRequest(int id, int myId) throws SQLException {
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementDecline(con, id, myId)) {
            ps.executeUpdate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acceptRequest(int id, int myId) throws SQLException {
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementAccept(con, id, myId)) {
            ps.executeUpdate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getFriendsList(int id) throws SQLException {
        List<User> list = new ArrayList<>();
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementFriends(con, id);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = UtilClass.newUser(rs);
                list.add(user);
            }
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getRequestsList(int id) throws SQLException {
        List<User> list = new ArrayList<>();
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementRequests(con, id);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = UtilClass.newUser(rs);
                list.add(user);
            }
        }
        return list;
    }

    /**
     * Creates PreparedStatement for new Friend Request.
     *
     * @param con    Connection to MySQL database
     * @param userId Requested User's id
     * @param myId   Authorized User's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementAdd(Connection con, int userId, int myId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO friends (firstFriend, secondFriend, request) VALUES (?, ?, 0)");
        ps.setInt(1, myId);
        ps.setInt(2, userId);
        return ps;
    }

    /**
     * Creates PreparedStatement for deleting Friend.
     *
     * @param con    Connection to MySQL database
     * @param userId Requested User's id
     * @param myId   Authorized User's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementDelete(Connection con, int userId, int myId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("DELETE FROM friends WHERE (firstFriend=? AND secondFriend=?) " +
                "OR (firstFriend=? AND secondFriend=?)");
        ps.setInt(1, myId);
        ps.setInt(2, userId);
        ps.setInt(3, userId);
        ps.setInt(4, myId);
        return ps;
    }

    /**
     * Creates PreparedStatement for declining Friend Request.
     *
     * @param con    Connection to MySQL database
     * @param userId Requested User's id
     * @param myId   Authorized User's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementDecline(Connection con, int userId, int myId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("DELETE FROM friends WHERE firstFriend=? AND secondFriend=?");
        ps.setInt(1, userId);
        ps.setInt(2, myId);
        return ps;
    }

    /**
     * Creates PreparedStatement for accepting Friend Request.
     *
     * @param con    Connection to MySQL database
     * @param userId Requested User's id
     * @param myId   Authorized User's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementAccept(Connection con, int userId, int myId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("UPDATE friends SET request=1 WHERE firstFriend=? AND secondFriend=?");
        ps.setInt(1, userId);
        ps.setInt(2, myId);
        return ps;
    }

    /**
     * Creates PreparedStatement for list of User's friends.
     *
     * @param con Connection to MySQL database
     * @param id  User's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementFriends(Connection con, int id) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT f.firstFriend AS userId, u.name, u.surname," +
                " u.email, u.country, u.photoId, u.job, u.education, u.relationship, u.birthdate FROM friends f\n" +
                "JOIN users u ON f.firstFriend = u.id\n" +
                "WHERE f.secondFriend=? AND f.request=1\n" +
                "UNION\n" +
                "SELECT f.secondFriend AS userId, u.name, u.surname, u.email, u.country, u.photoId, u.job," +
                " u.education, u.relationship, u.birthdate FROM friends f\n" +
                "JOIN users u ON f.secondFriend = u.id\n" +
                "WHERE f.firstFriend=? AND f.request=1\n" +
                "ORDER BY name");
        ps.setInt(1, id);
        ps.setInt(2, id);
        return ps;
    }

    /**
     * Creates PreparedStatement for list of User's requests.
     *
     * @param con Connection to MySQL database
     * @param id  User's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementRequests(Connection con, int id) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT f.firstFriend AS userId, u.name, u.surname, u.email, u.country," +
                "u.photoId, u.job, u.education, u.relationship, u.birthdate FROM friends f\n" +
                "JOIN users u ON f.firstFriend = u.id\n" +
                "WHERE f.secondFriend=? AND f.request=0\n" +
                "ORDER BY name");
        ps.setInt(1, id);
        return ps;
    }
}