package ru.yandex.saver12.dao;

import ru.yandex.saver12.model.Message;
import ru.yandex.saver12.model.User;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

/**
 * Defines methods for all MessageDAO implementations.
 *
 * @author Sergey Ermushin
 */

public interface MessageDAO {

    /**
     * Gets list of Authorized User's dialogues.
     *
     * @param id Authorized User's id
     * @return list of User's dialogues
     * @throws SQLException if a database access error occurs
     */
    List<User> getDialogueList(int id) throws SQLException;

    /**
     * Gets list of Authorized User's messages with other User.
     *
     * @param firstId  Authorized User's id
     * @param secondId Other User's id
     * @return list of messages
     * @throws SQLException if a database access error occurs
     */
    List<Message> getMessageList(HttpServletRequest request, int firstId, int secondId) throws SQLException;

    /**
     * Creates new Message object of Authorized User with other User. Saves it to database.
     *
     * @param firstId  Authorized User's id
     * @param secondId Other User's id
     * @return Message object
     * @throws SQLException if a database access error occurs
     */
    Message createMessage(String message, int firstId, int secondId) throws SQLException;

    /**
     * Deletes dialogue (all messages) of Authorized User with other User from database.
     *
     * @param firstId  Authorized User's id
     * @param secondId Other User's id
     * @throws SQLException if a database access error occurs
     */
    void deleteDialogue(int firstId, int secondId) throws SQLException;
}
