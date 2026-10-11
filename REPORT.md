# Project Report: Log File Analyzer

## 1. Introduction
The **Log File Analyzer** is a Java application designed to parse application log files, extract structured telemetry data, compute statistics across log levels (`INFO`, `WARNING`, and `ERROR`), summarize error occurrences, and generate a persistent analytical report. The project emphasizes file processing, string parsing, Java collection structures, resilience against malformed inputs, and clean report generation.

## 2. Project Requirements & Deliverables
In alignment with the assignment guidelines, the following deliverables were implemented:
* **Log Parser:** A parser capable of extracting timestamps, log levels, and log messages from unstructured text lines.
* **Log-Level Counts:** Utilization of `HashMap` data structures to tabulate the distribution of log levels (`INFO`, `WARNING`, and `ERROR`).
* **Error Summary:** Identification and frequency aggregation of error events along with chronological error tracking.
* **Malformed Line Handling:** Resilient line-by-line processing that prevents application crashes when encountering incomplete, corrupt, or unexpected lines.
* **Report Generation:** Automatic generation of a formatted report written to both standard console output and saved to disk (`data/analysis_report.txt`).

## 3. Architecture & Implementation Details

### 3.1 Modular Design
The project adheres to Object-Oriented Programming (OOP) principles and separation of concerns:
* **`LogEntry.java`**: Immutable domain model representing a validated log entry (storing line number, timestamp, log level, and message).
* **`LogParser.java`**: Encapsulates regex validation and string sanitization. It validates expected token boundaries (`[TIMESTAMP] [LEVEL] MESSAGE`), maps variants such as `WARN` to `WARNING`, and rejects invalid levels or corrupted structures with descriptive `IllegalArgumentException` messages.
* **`LogAnalyzer.java`**: Coordinates reading files line by line using `BufferedReader`. It maintains collections including:
  * `Map<String, Integer> logLevelCounts`: A `HashMap` tracking counts per log level.
  * `Map<String, Integer> errorFrequencies`: A `HashMap` counting duplicate error messages to highlight recurrent issues.
  * `List<LogEntry> errorEntries`: A chronological trace of errors with line numbers.
  * `List<String> malformedLines`: A record of rejected lines along with failure reasons.
* **`ReportGenerator.java`**: Formats all metrics into structured, human-readable sections (overview, log level percentages, error frequencies, and malformed line audit) and writes the results via `BufferedWriter`.
* **`Main.java`**: CLI driver and application entry point with customizable input/output path arguments.

### 3.2 Resilience & Malformed Line Handling
Real-world application logs frequently include truncated lines, stack traces, and unformatted system messages. The system implements safe parsing:
* Blank or whitespace lines are rejected gracefully.
* Lines missing brackets or timestamps are caught and logged to an audit trail without stopping the parser.
* Invalid log levels (e.g. `UNKNOWN_LEVEL`) are identified and separated from valid operational metrics.
* All parsing failures are handled within an isolated `try-catch` block during the iteration loop, ensuring uninterrupted processing of remaining records.

## 4. Test Results & Execution Output

### 4.1 Sample Test Dataset (`data/app.log`)
The test log file contains 21 lines consisting of standard lifecycle events, transient network timeouts, warning alerts, and two deliberate anomalies:
1. `CORRUPTED_LINE_WITHOUT_TIMESTAMP_OR_BRACKETS` (Line 9: missing timestamp and level tags).
2. `2026-10-11 08:41:00 [UNKNOWN_LEVEL] Invalid log level format encountered here.` (Line 16: unrecognized log level).

### 4.2 Observed Output
Upon running `java -cp bin com.loganalyzer.Main`, the analyzer successfully produced the following output:

```text
=================================================
          LOG FILE ANALYZER APPLICATION          
=================================================
Input Log File : data/app.log
Output Report  : data/analysis_report.txt
-------------------------------------------------
Processing log file...

================================================================================
                          APPLICATION LOG ANALYSIS REPORT                       
                          Generated: 2026-10-11 20:15:32
================================================================================

1. EXECUTION OVERVIEW
--------------------------------------------------------------------------------
  Total Lines Read       : 21
  Valid Log Entries      : 19 (90.48%)
  Malformed Lines        : 2 (9.52%)

2. LOG-LEVEL COUNTS & DISTRIBUTION
--------------------------------------------------------------------------------
  Log Level       | Count      | Percentage  
  ----------------+------------+-------------
  INFO            | 10         |  52.63%
  WARNING         | 4          |  21.05%
  ERROR           | 5          |  26.32%

3. ERROR SUMMARY & BREAKDOWN
--------------------------------------------------------------------------------
  Total Error Occurrences: 5
  Unique Error Types     : 3

  Distinct Error Messages by Frequency:
    1. [Count: 1] Failed to send invoice email to client@example.com: SMTP 503 error.
    2. [Count: 3] Connection timed out while reaching PaymentGateway API (timeout=5000ms).
    3. [Count: 1] NullPointerException in OrderProcessingService: line 142.

  Chronological Error Trace:
    - [Line 6  ] 2026-10-11 08:33:45: Connection timed out while reaching PaymentGateway API (timeout=5000ms).
    - [Line 8  ] 2026-10-11 08:34:05: Connection timed out while reaching PaymentGateway API (timeout=5000ms).
    - [Line 12 ] 2026-10-11 08:37:05: NullPointerException in OrderProcessingService: line 142.
    - [Line 15 ] 2026-10-11 08:40:22: Failed to send invoice email to client@example.com: SMTP 503 error.
    - [Line 18 ] 2026-10-11 08:43:10: Connection timed out while reaching PaymentGateway API (timeout=5000ms).

4. MALFORMED LINES AUDIT
--------------------------------------------------------------------------------
  Found 2 line(s) that failed formatting validation and were skipped:
    * Line 9: [Malformed syntax - expected timestamp and [LEVEL] tag] -> CORRUPTED_LINE_WITHOUT_TIMESTAMP_OR_BRACKETS
    * Line 16: [Unrecognized log level: UNKNOWN_LEVEL] -> 2026-10-11 08:41:00 [UNKNOWN_LEVEL] Invalid log level format encountered here.

================================================================================
                              END OF ANALYSIS REPORT                            
================================================================================

[SUCCESS] Report saved to: data/analysis_report.txt
```

## 5. Conclusion
All specified objectives and deliverables for the **Log File Analyzer** project have been satisfied:
1. File I/O was implemented using efficient buffered streams (`BufferedReader` and `BufferedWriter`).
2. Robust parsing and input validation were implemented using regular expressions and exception handling.
3. Collections framework (`HashMap` and `LinkedHashMap`) were utilized to aggregate metrics and count log levels.
4. Error handling and summary generation provide actionable operational intelligence.
5. Malformed entries were handled gracefully without causing runtime terminations.
