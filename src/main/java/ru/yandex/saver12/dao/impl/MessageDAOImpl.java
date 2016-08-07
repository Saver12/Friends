package ru.yandex.saver12.dao.impl;

import ru.yandex.saver12.dao.MessageDAO;
import ru.yandex.saver12.model.Message;
import ru.yandex.saver12.model.User;
import ru.yandex.saver12.util.DBConnectionManager;
import ru.yandex.saver12.util.UtilClass;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.saver12.util.Constants.NAME;
import static ru.yandex.saver12.util.Constants.SURNAME;

/**
 * Basic JDBC MessageDAO implementation.
 *
 * @author Sergey Ermushin
 */

public class MessageDAOImpl implements MessageDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getDialogueList(int id) throws SQLException {
        List<User> list = new ArrayList<>();
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementDialogues(con, id);
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
    public List<Message> getMessageList(HttpServletRequest request, int firstId, int secondId) throws SQLException {
        List<Message> list = new ArrayList<>();
        String name = null;
        String surname = null;
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementMessages(con, firstId, secondId);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                name = rs.getString("name");
                surname = rs.getString("surname");
            }
            rs.beforeFirst();
            while (rs.next()) {
                Message message = new Message(rs.getString("message"), rs.getInt("id"), rs.getInt("firstUserId"), rs.getInt("secondUserId"));
                list.add(message);
            }
        }
        if (name != null && surname != null) {
            request.setAttribute(NAME.toString(), name);
            request.setAttribute(SURNAME.toString(), surname);
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message createMessage(String message, int firstId, int secondId) throws SQLException {
        Message newMessage = null;
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementNewMessage(con, message, firstId, secondId);
             ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                int key = rs.getInt(1);
                newMessage = new Message(message, key, firstId, secondId);
            }
        }
        return newMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDialogue(int firstId, int secondId) throws SQLException {
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementDelete(con, firstId, secondId)) {
            ps.executeUpdate();
        }
    }

    /**
     * Creates PreparedStatement for list of Authorized User's dialogues.
     *
     * @param con    Connection to MySQL database
     * @param userId Authorized User's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementDialogues(Connection con, int userId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT m.firstUserId AS userId, u.name, u.surname, u.email," +
                " u.country, u.photoId, u.job, u.education, u.relationship, u.birthdate FROM messages m\n" +
                "JOIN users u ON m.firstUserId = u.id\n" +
                "WHERE m.secondUserId=?\n" +
                "UNION\n" +
                "SELECT m.secondUserId AS userId, u.name, u.surname, u.email, u.country, u.photoId," +
                "u.job, u.education, u.relationship, u.birthdate FROM messages m\n" +
                "JOIN users u ON m.secondUserId = u.id\n" +
                "WHERE m.firstUserId=? ORDER BY name");
        ps.setInt(1, userId);
        ps.setInt(2, userId);
        return ps;
    }

    /**
     * Creates PreparedStatement for list of Authorized User's messages with other User.
     *
     * @param con      Connection to MySQL database
     * @param firstId  Authorized User's id
     * @param secondId Other User's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementMessages(Connection con, int firstId, int secondId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT m.id, m.message, m.firstUserId, m.secondUserId, u.name, u.surname FROM messages m\n" +
                "JOIN users u ON m.secondUserId = u.id\n" +
                "WHERE m.firstUserId=? AND m.secondUserId=?\n" +
                "UNION\n" +
                "SELECT m.id, m.message, m.firstUserId, m.secondUserId, u.name, u.surname FROM messages m\n" +
                "JOIN users u ON m.firstUserId = u.id\n" +
                "WHERE m.firstUserId=? AND m.secondUserId=? ORDER BY id");
        ps.setInt(1, firstId);
        ps.setInt(2, secondId);
        ps.setInt(3, secondId);
        ps.setInt(4, firstId);
        return ps;
    }

    /**
     * Creates PreparedStatement for new Message object of Authorized User with other User.
     *
     * @param con      Connection to MySQL database
     * @param firstId  Authorized User's id
     * @param secondId Other User's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementNewMessage(Connection con, String message, int firstId, int secondId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO messages(message, firstUserId, secondUserId) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, message);
        ps.setInt(2, firstId);
        ps.setInt(3, secondId);
        ps.executeUpdate();
        return ps;
    }

    /**
     * Creates PreparedStatement for deleting dialogue of Authorized User with other User.
     *
     * @param con      Connection to MySQL database
     * @param firstId  Authorized User's id
     * @param secondId Other User's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementDelete(Connection con, int firstId, int secondId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("DELETE FROM messages\n" +
                "WHERE (firstUserId=? AND secondUserId=?) OR (firstUserId=? AND secondUserId=?)");
        ps.setInt(1, firstId);
        ps.setInt(2, secondId);
        ps.setInt(3, secondId);
        ps.setInt(4, firstId);
        return ps;
    }
}
