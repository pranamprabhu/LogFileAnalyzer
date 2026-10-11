package com.loganalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Analyzes log files line by line, calculating log-level statistics using HashMap,
 * aggregating error summaries, and resiliently recording malformed entries.
 */
public class LogAnalyzer {

    private final LogParser parser;
    private final Map<String, Integer> logLevelCounts;
    private final Map<String, Integer> errorFrequencies;
    private final List<LogEntry> errorEntries;
    private final List<LogEntry> validEntries;
    private final List<String> malformedLines;
    private int totalLinesProcessed;

    public LogAnalyzer() {
        this.parser = new LogParser();
        // Initialize LinkedHashMap with standard log levels to preserve display order
        this.logLevelCounts = new LinkedHashMap<>();
        this.logLevelCounts.put("INFO", 0);
        this.logLevelCounts.put("WARNING", 0);
        this.logLevelCounts.put("ERROR", 0);

        this.errorFrequencies = new HashMap<>();
        this.errorEntries = new ArrayList<>();
        this.validEntries = new ArrayList<>();
        this.malformedLines = new ArrayList<>();
        this.totalLinesProcessed = 0;
    }

    /**
     * Reads and processes the given log file line by line.
     *
     * @param logFilePath Path to the application log file.
     * @throws IOException If file cannot be read.
     */
    public void analyze(String logFilePath) throws IOException {
        File file = new File(logFilePath);
        if (!file.exists()) {
            throw new IOException("Log file not found: " + logFilePath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                totalLinesProcessed++;

                try {
                    LogEntry entry = parser.parseLine(line, lineNumber);
                    validEntries.add(entry);

                    // Update log-level counts using HashMap
                    String level = entry.getLevel();
                    logLevelCounts.put(level, logLevelCounts.getOrDefault(level, 0) + 1);

                    // Track error details
                    if ("ERROR".equalsIgnoreCase(level)) {
                        errorEntries.add(entry);
                        String errMsg = entry.getMessage();
                        errorFrequencies.put(errMsg, errorFrequencies.getOrDefault(errMsg, 0) + 1);
                    }

                } catch (IllegalArgumentException e) {
                    // Record malformed line gracefully without stopping execution
                    malformedLines.add(String.format("Line %d: [%s] -> %s", lineNumber, e.getMessage(), line));
                }
            }
        }
    }

    public Map<String, Integer> getLogLevelCounts() {
        return Collections.unmodifiableMap(logLevelCounts);
    }

    public Map<String, Integer> getErrorFrequencies() {
        return Collections.unmodifiableMap(errorFrequencies);
    }

    public List<LogEntry> getErrorEntries() {
        return Collections.unmodifiableList(errorEntries);
    }

    public List<LogEntry> getValidEntries() {
        return Collections.unmodifiableList(validEntries);
    }

    public List<String> getMalformedLines() {
        return Collections.unmodifiableList(malformedLines);
    }

    public int getTotalLinesProcessed() {
        return totalLinesProcessed;
    }

    public int getValidEntriesCount() {
        return validEntries.size();
    }

    public int getMalformedLinesCount() {
        return malformedLines.size();
    }
}
