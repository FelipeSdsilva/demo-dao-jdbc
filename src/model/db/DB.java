package model.db;

import model.exceptions.DbException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {
    private static Connection conn = null;
    private Statement st = null;
    private ResultSet rt = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                conn = DriverManager.getConnection(url, props);
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rt) {
        if (rt != null) {
            try {
                rt.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    private static Properties loadProperties() {
        try (FileInputStream fl = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fl);
            return props;
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        }
    }
}
