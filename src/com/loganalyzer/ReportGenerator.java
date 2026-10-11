package com.loganalyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Formats analysis results and writes comprehensive reports to console and disk.
 */
public class ReportGenerator {

    /**
     * Builds a comprehensive textual report from the analyzer statistics.
     *
     * @param analyzer The analyzed dataset.
     * @return Formatted multi-line report string.
     */
    public String buildReport(LogAnalyzer analyzer) {
        StringBuilder sb = new StringBuilder();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        sb.append("================================================================================\n");
        sb.append("                          APPLICATION LOG ANALYSIS REPORT                       \n");
        sb.append("                          Generated: ").append(timestamp).append("\n");
        sb.append("================================================================================\n\n");

        // 1. OVERVIEW
        sb.append("1. EXECUTION OVERVIEW\n");
        sb.append("--------------------------------------------------------------------------------\n");
        int total = analyzer.getTotalLinesProcessed();
        int valid = analyzer.getValidEntriesCount();
        int malformed = analyzer.getMalformedLinesCount();

        sb.append(String.format("  Total Lines Read       : %d\n", total));
        sb.append(String.format("  Valid Log Entries      : %d (%.2f%%)\n", valid, total > 0 ? (valid * 100.0 / total) : 0));
        sb.append(String.format("  Malformed Lines        : %d (%.2f%%)\n", malformed, total > 0 ? (malformed * 100.0 / total) : 0));
        sb.append("\n");

        // 2. LOG-LEVEL DISTRIBUTION
        sb.append("2. LOG-LEVEL COUNTS & DISTRIBUTION\n");
        sb.append("--------------------------------------------------------------------------------\n");
        sb.append(String.format("  %-15s | %-10s | %-12s\n", "Log Level", "Count", "Percentage"));
        sb.append("  ----------------+------------+-------------\n");

        Map<String, Integer> counts = analyzer.getLogLevelCounts();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            String level = entry.getKey();
            int count = entry.getValue();
            double pct = valid > 0 ? (count * 100.0 / valid) : 0.0;
            sb.append(String.format("  %-15s | %-10d | %6.2f%%\n", level, count, pct));
        }
        sb.append("\n");

        // 3. ERROR SUMMARY
        sb.append("3. ERROR SUMMARY & BREAKDOWN\n");
        sb.append("--------------------------------------------------------------------------------\n");
        List<LogEntry> errorEntries = analyzer.getErrorEntries();
        Map<String, Integer> freq = analyzer.getErrorFrequencies();

        sb.append(String.format("  Total Error Occurrences: %d\n", errorEntries.size()));
        sb.append(String.format("  Unique Error Types     : %d\n\n", freq.size()));

        if (freq.isEmpty()) {
            sb.append("  No error events recorded in this log file.\n\n");
        } else {
            sb.append("  Distinct Error Messages by Frequency:\n");
            int rank = 1;
            for (Map.Entry<String, Integer> entry : freq.entrySet()) {
                sb.append(String.format("    %d. [Count: %d] %s\n", rank++, entry.getValue(), entry.getKey()));
            }
            sb.append("\n  Chronological Error Trace:\n");
            for (LogEntry err : errorEntries) {
                sb.append(String.format("    - [Line %-3d] %s: %s\n",
                    err.getLineNumber(), err.getTimestamp(), err.getMessage()));
            }
            sb.append("\n");
        }

        // 4. MALFORMED LINES SUMMARY
        sb.append("4. MALFORMED LINES AUDIT\n");
        sb.append("--------------------------------------------------------------------------------\n");
        List<String> malformedLines = analyzer.getMalformedLines();
        if (malformedLines.isEmpty()) {
            sb.append("  All lines adhered strictly to the expected log format.\n");
        } else {
            sb.append(String.format("  Found %d line(s) that failed formatting validation and were skipped:\n", malformedLines.size()));
            for (String ml : malformedLines) {
                sb.append("    * ").append(ml).append("\n");
            }
        }
        sb.append("\n");

        sb.append("================================================================================\n");
        sb.append("                              END OF ANALYSIS REPORT                            \n");
        sb.append("================================================================================\n");

        return sb.toString();
    }

    /**
     * Saves the generated report to the designated output file.
     *
     * @param reportContent Full report text.
     * @param outputPath    Destination path.
     * @throws IOException If file writing fails.
     */
    public void saveReportToFile(String reportContent, String outputPath) throws IOException {
        File file = new File(outputPath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(reportContent);
        }
    }
}
