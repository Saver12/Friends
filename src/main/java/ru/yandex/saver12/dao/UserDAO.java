package ru.yandex.saver12.dao;

import ru.yandex.saver12.model.User;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

/**
 * Defines methods for all UserDAO implementations.
 *
 * @author Sergey Ermushin
 */

public interface UserDAO {

    /**
     * Gets Authorized User by email and password.
     *
     * @param email    Email
     * @param password Password
     * @return Authorized User object
     * @throws SQLException if a database access error occurs
     */
    User getUser(String email, String password) throws SQLException;

    /**
     * Gets User by id.
     *
     * @param request      HttpServletRequest object
     * @param id           User's id
     * @param loggedUserId Authorized User's id
     * @return User object
     * @throws SQLException if a database access error occurs
     */
    User getUserById(HttpServletRequest request, int id, int loggedUserId) throws SQLException;

    /**
     * Creates new User object and saves it to database.
     *
     * @param name     Name
     * @param surname  Surname
     * @param email    Email
     * @param country  Country
     * @param password Password
     * @return User object
     * @throws SQLException if a database access error occurs
     */
    User createUser(String name, String surname, String email, String country, String password) throws SQLException;

    /**
     * Gets list of users by name.
     *
     * @param name Name
     * @return list of users
     * @throws SQLException if a database access error occurs
     */
    List<User> getUsersByName(String name) throws SQLException;

    /**
     * Updates Authorized User in database.
     *
     * @param user Authorized User object
     * @throws SQLException if a database access error occurs
     */
    void updateUser(User user) throws SQLException;
}
