package com.csdts.logprocessor;

import com.csdts.logprocessor.data.ProcessedLogsRepository;
import com.csdts.logprocessor.data.ProcessedLogsRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LogProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String PROVIDE_FILE_NAME = "Please, provide the filename";
    private static File file;
    private static HashMap<String, LogEntry> logEntries = new HashMap<>();
    private static List<ProcessedLogEntry> processedLogEntries = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length != 0) {
            file = new File(args[0]);
        } else {
            System.out.println(PROVIDE_FILE_NAME);
            return;
        }
        LOGGER.info("Start parsing logs from "+file.getName());
        LogParser logParser = new LogParserImpl(file);
        processedLogEntries = logParser.parseLog(logEntries);
        LOGGER.info("Parsing logs succeed. Start writing to DB");
        ProcessedLogsRepository processedLogsRepository = new ProcessedLogsRepositoryImpl();
        processedLogsRepository.saveProcessedLogs(processedLogEntries);
        LOGGER.info("Finished writing to DB");
        processedLogsRepository.shutdown();
    }

}
