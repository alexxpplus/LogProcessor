package com.csdts.logprocessor;

import java.util.HashMap;
import java.util.List;

public interface LogParser {
    List<ProcessedLogEntry> parseLog(HashMap<String, LogEntry> logEntries);
}
