package ru.yandex.saver12.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Manager that provides Connections from Tomcat Connection Pool.
 *
 * @author Sergey Ermushin
 */

public class DBConnectionManager {

    /**
     * A factory for connections to the physical data source that this
     * {@code DataSource} object represents.
     */
    private static DataSource ds;

    static {
        try {
            ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/userdb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets Connection from {@code DataSource} object.
     *
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}