package ru.yandex.saver12.dao;

import ru.yandex.saver12.model.Audio;

import java.sql.SQLException;
import java.util.List;

/**
 * Defines methods for all AudioDAO implementations.
 *
 * @author Sergey Ermushin
 */

public interface AudioDAO {

    /**
     * Gets list of User's audios.
     *
     * @param id User's id
     * @return list of User's audios
     * @throws SQLException if a database access error occurs
     */
    List<Audio> getAudiosList(int id) throws SQLException;

    /**
     * Creates new Audio object and saves it to database.
     *
     * @param name   Audio's description
     * @param userId User's id
     * @return Audio object
     * @throws SQLException if a database access error occurs
     */
    Audio createAudio(String name, int userId) throws SQLException;

    /**
     * Deletes audio from database.
     *
     * @param id Audio's id
     * @throws SQLException if a database access error occurs
     */
    void deleteAudio(int id) throws SQLException;

    /**
     * Updates audio's description in database.
     *
     * @param id   Audio's id
     * @param name Audio's description
     * @throws SQLException if a database access error occurs
     */
    void updateDescription(int id, String name) throws SQLException;
}
