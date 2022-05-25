package cz.mff.resler.java.issue_tracking_system;

import java.io.File;
import java.sql.*;

/**
 * Class for connecting to the database.
 */
public class SQLiteConnection {

    /**
     * Tries to connect to the database and returns a Connection instance.
     * @return a Connection instance or null if connection could not be established.
     */
    public static Connection connector() {
        String dbName = "IssueTrackingSystemDatabase.db";

//      Check whether the database file exists, otherwise sqlite would create a new empty database
        File file = new File(dbName);
        if (!file.exists()) {
            return null;
        }

        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            return conn;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
