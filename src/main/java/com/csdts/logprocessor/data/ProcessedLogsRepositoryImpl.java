package com.csdts.logprocessor.data;

import com.csdts.logprocessor.ProcessedLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ProcessedLogsRepositoryImpl implements ProcessedLogsRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SHUTDOWN = "SHUTDOWN";
    private Connection connection;
    private ConnectionManager hsqldbConnectionManager;

    public ProcessedLogsRepositoryImpl() {
        hsqldbConnectionManager = new HsqldbConnectionManager();
        connection = hsqldbConnectionManager.getConnection();
        createProcessedLogsTable();
    }

    @Override
    public void saveProcessedLogs(List<ProcessedLogEntry> processedLogEntries) {
        try {
            String insertProcessedLogsSql =
                    "INSERT INTO PROCESSED_LOGS(ID, DURATION, TYPE, HOST, ALERT) " + "VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertProcessedLogsSql);
            for (ProcessedLogEntry processedLogEntry : processedLogEntries) {
                preparedStatement.setString(1, processedLogEntry.getId());
                preparedStatement.setString(2, String.valueOf(processedLogEntry.getDuration()));
                preparedStatement.setString(3, processedLogEntry.getType());
                preparedStatement.setString(4, processedLogEntry.getHost());
                preparedStatement.setString(5, processedLogEntry.getAlert());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void shutdown() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(SHUTDOWN);
            connection.close();
            LOGGER.info("Connection to DB closed, DB shut down");
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void createProcessedLogsTable() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS PROCESSED_LOGS (ID VARCHAR(50) NOT NULL, DURATION INTEGER NOT NULL, TYPE VARCHAR(50), HOST VARCHAR(50), ALERT VARCHAR(4))");
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
