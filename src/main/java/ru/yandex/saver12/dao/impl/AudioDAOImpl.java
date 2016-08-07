package ru.yandex.saver12.dao.impl;

import ru.yandex.saver12.dao.AudioDAO;
import ru.yandex.saver12.model.Audio;
import ru.yandex.saver12.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic JDBC AudioDAO implementation.
 *
 * @author Sergey Ermushin
 */

public class AudioDAOImpl implements AudioDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Audio> getAudiosList(int id) throws SQLException {
        List<Audio> list = new ArrayList<>();
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementAudios(con, id);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Audio audio = new Audio(rs.getInt("id"), rs.getInt("userId"), rs.getString("name"));
                list.add(audio);
            }
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Audio createAudio(String name, int userId) throws SQLException {
        Audio audio = null;
        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = createPreparedStatementNewAudio(con, name, userId);
             ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                int key = rs.getInt(1);
                audio = new Audio(key, userId, name);
            }
        }
        return audio;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAudio(int id) throws SQLException {
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
     * Creates PreparedStatement for list of User's audios.
     *
     * @param con Connection to MySQL database
     * @param id  User's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementAudios(Connection con, int id) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM audios WHERE userId=? ORDER BY id DESC");
        ps.setInt(1, id);
        return ps;
    }

    /**
     * Creates PreparedStatement for new Audio object.
     *
     * @param con    Connection to MySQL database
     * @param name   Audio's description
     * @param userId User's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementNewAudio(Connection con, String name, int userId) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO audios(name, userId) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, name);
        ps.setInt(2, userId);
        ps.executeUpdate();
        return ps;
    }

    /**
     * Creates PreparedStatement for deleting Audio.
     *
     * @param con Connection to MySQL database
     * @param id  Audio's id
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementDelete(Connection con, int id) throws SQLException {
        PreparedStatement ps = con.prepareStatement("DELETE FROM audios WHERE id=?");
        ps.setInt(1, id);
        return ps;
    }

    /**
     * Creates PreparedStatement for updating Audio's description.
     *
     * @param con  Connection to MySQL database
     * @param id   Audio's id
     * @param name Audio's description
     * @return PreparedStatement object
     * @throws SQLException if a database access error occurs
     */
    private PreparedStatement createPreparedStatementUpdateName(Connection con, int id, String name) throws SQLException {
        PreparedStatement ps = con.prepareStatement("UPDATE audios SET name=? WHERE id=?");
        ps.setString(1, name);
        ps.setInt(2, id);
        return ps;
    }
}