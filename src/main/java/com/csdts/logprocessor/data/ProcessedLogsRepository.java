package com.csdts.logprocessor.data;

import com.csdts.logprocessor.ProcessedLogEntry;

import java.util.List;

public interface ProcessedLogsRepository {
    void saveProcessedLogs(List<ProcessedLogEntry> processedLogEntries);
    void shutdown();
}
