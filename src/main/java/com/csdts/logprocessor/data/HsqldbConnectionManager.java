package com.csdts.logprocessor.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HsqldbConnectionManager implements ConnectionManager {

    private static final String DRIVER_NAME = "org.hsqldb.jdbcDriver";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String PASSWORD = "";
    private static final String URL = "jdbc:hsqldb:file:";
    private static final String USER_NAME = "sa";
    private static Connection connection;

    @Override
    public Connection getConnection() {
        try {
            Class.forName(DRIVER_NAME);
            String workingDirectory = Paths.get("").toAbsolutePath().toString().replace("\\", "/")+"/";
            try {
                connection = DriverManager.getConnection(URL+workingDirectory, USER_NAME, PASSWORD);
                LOGGER.info("Connection to DB established");
            } catch (SQLException ex) {
                LOGGER.error("Failed to create the database connection.");
            }
        } catch (ClassNotFoundException ex) {
            LOGGER.error("DB driver not found.");
        }
        return connection;
    }
}
