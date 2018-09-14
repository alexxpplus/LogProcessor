import com.csdts.logprocessor.LogEntry;
import com.csdts.logprocessor.LogParserImpl;
import com.csdts.logprocessor.ProcessedLogEntry;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LogParserImplTest {

    LogParserImpl logParser;
    private HashMap<String, LogEntry> logEntries;
    private List<ProcessedLogEntry> processedLogEntries;

    @Test
    public void parseLogTest() throws IOException {
        File file = File.createTempFile("test", ".json");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write("{\"id\":\"scsmbstgra\", \"state\":\"STARTED\", \"type\":\"APPLICATION_LOG\",\n" +
                             "\"host\":\"12345\", \"timestamp\":1491377495212}\n" +
                             "{\"id\":\"scsmbstgrb\", \"state\":\"STARTED\", \"timestamp\":1491377495213}\n" +
                             "{\"id\":\"scsmbstgrc\", \"state\":\"FINISHED\", \"timestamp\":1491377495218}\n" +
                             "{\"id\":\"scsmbstgra\", \"state\":\"FINISHED\", \"type\":\"APPLICATION_LOG\",\n" +
                             "\"host\":\"12345\", \"timestamp\":1491377495217}\n" +
                             "{\"id\":\"scsmbstgrc\", \"state\":\"STARTED\", \"timestamp\":1491377495210}\n" +
                             "{\"id\":\"scsmbstgrb\", \"state\":\"FINISHED\", \"timestamp\":1491377495216}");
        bufferedWriter.close();
        HashMap<String, LogEntry> logEntries = new HashMap<>();
        List<ProcessedLogEntry> processedLogEntries = new ArrayList<>();
        LogParserImpl logParser = new LogParserImpl(file);
        processedLogEntries = logParser.parseLog(logEntries);
        assertEquals(processedLogEntries.size(), 3);
    }
}
