package com.csdts.logprocessor;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.abs;


public class LogParserImpl implements LogParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Integer ALERT_FLAG = 4;
    private static final String ALERT = "true";

    private final JsonFactory jsonFactory = new MappingJsonFactory();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<ProcessedLogEntry> processedLogEntries = new ArrayList<>();

    private JsonParser jsonParser;
    private JsonToken currentToken;

    public LogParserImpl(File filename) {
        initLogParser(filename);
    }

    private void initLogParser(File filename) {
        try {
            jsonParser = jsonFactory.createParser(filename);
            currentToken = jsonParser.nextToken();
            if (currentToken != JsonToken.START_OBJECT) {
                LOGGER.error("Error: root should be object: quiting.");
                return;
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
    @Override
    public List<ProcessedLogEntry> parseLog(HashMap<String, LogEntry> logEntries) {
        LOGGER.info("Enter parseLog");
        try {
            while (currentToken == JsonToken.START_OBJECT) {
                LogEntry logEntry = objectMapper.readValue(jsonParser, LogEntry.class);
                processedLogEntries = processLogs(logEntries, logEntry);
                currentToken = jsonParser.nextToken();
            }
            jsonParser.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        LOGGER.info("Exit parseLog");
        return processedLogEntries;
    }

    private List<ProcessedLogEntry> processLogs(HashMap<String, LogEntry> logEntries, LogEntry logEntry) {
        LOGGER.info("Enter proccessLogs");
        String key = logEntry.getId();
        if (logEntries.containsKey(key)) {
            LogEntry value = logEntries.get(key);
            ProcessedLogEntry processedLogEntry = new ProcessedLogEntry();
            processedLogEntry.setId(key);
            long duration = abs(value.getTimestamp() - logEntry.getTimestamp());
            processedLogEntry.setDuration(duration);
            processedLogEntry.setHost(value.getHost());
            processedLogEntry.setType(value.getType());
            if (duration > ALERT_FLAG) { processedLogEntry.setAlert(ALERT); }
            logEntries.remove(key);
            processedLogEntries.add(processedLogEntry);
        } else {
            logEntries.put(key, logEntry);
        }
        LOGGER.info("Exit proccessLogs");
        return processedLogEntries;
    }

}