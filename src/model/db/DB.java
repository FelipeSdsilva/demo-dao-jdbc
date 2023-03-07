package model.db;

import model.exceptions.DbException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class DB {
    private Connection conn = null;


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
