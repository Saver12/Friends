package ru.yandex.saver12.dao.impl;

import ru.yandex.saver12.dao.UserDAO;
import ru.yandex.saver12.model.User;
import ru.yandex.saver12.util.DBConnectionManager;
import ru.yandex.saver12.util.UtilClass;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.*;
import java.util.Date;

import static java.lang.Integer.parseInt;
import static ru.yandex.saver12.util.Constants.REQUEST;
import static ru.yandex.saver12.util.Constants.SENDER_ID;

/**
 * Basic JDBC UserDAO implementation.
 *
 * @author Sergey Ermushin
 */

public class UserDAOImpl implements UserDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser(String email, String password) throws SQLException {
        User user = null;
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementEmailAndPassword(con, email, password);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                user = UtilClass.newUser(rs);
            }
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserById(HttpServletRequest request, int id, int loggedUserId) throws SQLException {
        User user = null;
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementId(con, id, loggedUserId);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                user = UtilClass.newUser(rs);
                String req = rs.getString("request");
                if (req != null) {
                    int attr = parseInt(req);
                    request.setAttribute(REQUEST.toString(), attr);
                    if (attr == 0) {
                        request.setAttribute(SENDER_ID.toString(), parseInt(rs.getString("firstFriend")));
                    }
                }
            }
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getUsersByName(String name) throws SQLException {
        List<User> list = new ArrayList<>();
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementUsers(con, name);
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
    public User createUser(String name, String surname, String email, String country, String password) throws SQLException {
        User user = null;
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementNewUser(con, name, surname, email, country, password);
             ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                int key = rs.getInt(1);
                user = new User(name, surname, email, country, null, "", "", "", key, 0);
            }
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUser(User user) throws SQLException {
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementUpdate(con, user)) {
            ps.executeUpdate();
        }
    }

    /**
     * Creates PreparedStatement for getting Authorized User.
     *
     * @param con      Connection to MySQL database
     * @param email    Email
     * @param password Password
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementEmailAndPassword(Connection con, String email, String password) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT id AS userId, name, surname, email, country, photoId," +
                "job, education, relationship, birthdate FROM Users WHERE email=? AND password=? LIMIT 1");
        ps.setString(1, email);
        ps.setString(2, password);
        return ps;
    }

    /**
     * Creates PreparedStatement for getting User by id.
     *
     * @param con          Connection to MySQL database
     * @param id           User's id
     * @param loggedUserId Authorized User's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementId(Connection con, int id, int loggedUserId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT u.id AS userId, u.name, u.surname, u.email, u.country, u.photoId," +
                "u.job, u.education, u.relationship, u.birthdate, f.request, f.firstFriend FROM users u\n" +
                "LEFT JOIN friends f ON (f.secondFriend=u.id AND f.firstFriend=?) OR (f.secondFriend=? AND f.firstFriend=u.id)\n" +
                "WHERE u.id=?");
        ps.setInt(1, loggedUserId);
        ps.setInt(2, loggedUserId);
        ps.setInt(3, id);
        return ps;
    }

    /**
     * Creates PreparedStatement for getting users by name.
     *
     * @param con  Connection to MySQL database
     * @param name Name
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementUsers(Connection con, String name) throws SQLException {
        name = name
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_")
                .replace("[", "![");
        PreparedStatement ps = con.prepareStatement("SELECT id AS userId, name, surname, email, country," +
                "photoId, job, education, relationship, birthdate " +
                "FROM Users WHERE CONCAT(name, ' ', surname) LIKE ? " +
                "OR CONCAT(surname, ' ', name) LIKE ? ESCAPE '!'");
        ps.setString(1, "%" + name + "%");
        ps.setString(2, "%" + name + "%");
        return ps;
    }

    /**
     * Creates PreparedStatement for creating new User.
     *
     * @param con  Connection to MySQL database
     * @param name Name
     * @param surname Surname
     * @param email Email
     * @param country Country
     * @param password Password
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementNewUser(Connection con, String name, String surname, String email, String country, String password) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO Users(name,surname,email,country, password, photoId) VALUES (?,?,?,?,?,0)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, name);
        ps.setString(2, surname);
        ps.setString(3, email);
        ps.setString(4, country);
        ps.setString(5, password);
        ps.executeUpdate();
        return ps;
    }

    /**
     * Creates PreparedStatement for updating Authorized User.
     *
     * @param con  Connection to MySQL database
     * @param user Authorized User object
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementUpdate(Connection con, User user) throws SQLException {
        PreparedStatement ps = con.prepareStatement("UPDATE users SET name=?, surname=?, birthdate=?," +
                "job=?, education=?, relationship=?, country=? WHERE id=?");
        ps.setString(1, user.getName());
        ps.setString(2, user.getSurname());
        Date date = user.getBirthdate();
        ps.setDate(3, date != null ? new java.sql.Date(date.getTime()) : null);
        ps.setString(4, user.getJob());
        ps.setString(5, user.getEducation());
        ps.setString(6, user.getRelationship());
        ps.setString(7, user.getCountry());
        ps.setInt(8, user.getId());
        return ps;
    }
}