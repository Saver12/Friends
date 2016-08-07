package ru.yandex.saver12.dao.impl;

import ru.yandex.saver12.dao.PhotoDAO;
import ru.yandex.saver12.model.Photo;
import ru.yandex.saver12.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic JDBC PhotoDAO implementation.
 *
 * @author Sergey Ermushin
 */

public class PhotoDAOImpl implements PhotoDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Photo> getPhotosList(int id) throws SQLException {
        List<Photo> list = new ArrayList<>();
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementPhotos(con, id);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Photo photo = new Photo(rs.getInt("id"), rs.getInt("userId"), rs.getString("name"));
                list.add(photo);
            }
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Photo createPhoto(String name, int userId) throws SQLException {
        Photo photo = null;
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementNewPhoto(con, name, userId);
             ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                int key = rs.getInt(1);
                photo = new Photo(key, userId, name);
            }
        }
        return photo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePhoto(int id) throws SQLException {
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementDelete(con, id)) {
            ps.executeUpdate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDescription(int id, String name) throws SQLException {
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementUpdateName(con, id, name)) {
            ps.executeUpdate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeProfilePhoto(int id, int photoId) throws SQLException {
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementChangePhoto(con, id, photoId)) {
            ps.executeUpdate();
        }
    }

    /**
     * Creates PreparedStatement for list of User's photos.
     *
     * @param con Connection to MySQL database
     * @param id  User's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementPhotos(Connection con, int id) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM photos WHERE userId=? ORDER BY id DESC");
        ps.setInt(1, id);
        return ps;
    }

    /**
     * Creates PreparedStatement for new Photo object.
     *
     * @param con    Connection to MySQL database
     * @param name   Photo's description
     * @param userId Authorized User's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementNewPhoto(Connection con, String name, int userId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO photos(name, userId) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, name);
        ps.setInt(2, userId);
        ps.executeUpdate();
        return ps;
    }

    /**
     * Creates PreparedStatement for deleting photo.
     *
     * @param con Connection to MySQL database
     * @param id  Photo's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementDelete(Connection con, int id) throws SQLException {
        PreparedStatement ps = con.prepareStatement("DELETE FROM photos WHERE id=?");
        ps.setInt(1, id);
        return ps;
    }

    /**
     * Creates PreparedStatement for updating photo's description.
     *
     * @param con  Connection to MySQL database
     * @param id   Photo's id
     * @param name Photo's description
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementUpdateName(Connection con, int id, String name) throws SQLException {
        PreparedStatement ps = con.prepareStatement("UPDATE photos SET name=? WHERE id=?");
        ps.setString(1, name);
        ps.setInt(2, id);
        return ps;
    }

    /**
     * Creates PreparedStatement for changing Profile Photo.
     *
     * @param con     Connection to MySQL database
     * @param id      Authorized User's id
     * @param photoId Photo's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementChangePhoto(Connection con, int id, int photoId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("UPDATE users SET photoId=? WHERE id=?");
        ps.setInt(1, photoId);
        ps.setInt(2, id);
        return ps;
    }
}