package ru.yandex.saver12.dao;

import ru.yandex.saver12.model.Photo;

import java.sql.SQLException;
import java.util.List;

/**
 * Defines methods for all PhotoDAO implementations.
 *
 * @author Sergey Ermushin
 */

public interface PhotoDAO {

    /**
     * Gets list of User's photos.
     *
     * @param id User's id
     * @return list of User's photos
     * @throws SQLException if a database access error occurs
     */
    List<Photo> getPhotosList(int id) throws SQLException;

    /**
     * Creates new Photo object and saves it to database.
     *
     * @param name   Photo's description
     * @param userId Authorized User's id
     * @return Photo object
     * @throws SQLException if a database access error occurs
     */
    Photo createPhoto(String name, int userId) throws SQLException;

    /**
     * Deletes photo from database.
     *
     * @param id Authorized User's id
     * @throws SQLException if a database access error occurs
     */
    void deletePhoto(int id) throws SQLException;

    /**
     * Updates photo's description in database.
     *
     * @param id   Photo's id
     * @param name Photo's description
     * @throws SQLException if a database access error occurs
     */
    void updateDescription(int id, String name) throws SQLException;

    /**
     * Changes Profile Photo of Authorized User.
     *
     * @param id      Authorized User's id
     * @param photoId Photo's id
     * @throws SQLException if a database access error occurs
     */
    void changeProfilePhoto(int id, int photoId) throws SQLException;
}
