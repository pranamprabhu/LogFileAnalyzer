# Log File Analyzer

## Description
Read an application log file and generate comprehensive statistics for `INFO`, `WARNING`, and `ERROR` messages.

## Objective
Practice file processing, string parsing, Java collections (`HashMap`, `List`), exception handling, and automated reporting.

## Tools
* Java 21+
* File I/O (`BufferedReader`, `BufferedWriter`)
* Collections Framework (`HashMap`, `LinkedHashMap`, `List`)
* Regular Expressions (`java.util.regex`)

## Deliverables & Features
* **Log Parser:** Robust string and regular expression parser that extracts timestamps, log levels, and messages.
* **Log-Level Counts:** Uses `HashMap` to dynamically compute occurrences and percentage shares for `INFO`, `WARNING`, and `ERROR` entries.
* **Error Summary:** Aggregates error frequencies, identifies unique error patterns, and traces chronological error events with line numbers.
* **Malformed Line Handling:** Resilient line-by-line processing that gracefully logs and skips malformed or corrupted log rows without terminating execution.
* **Report Generation:** Generates a structured analysis report displayed in the terminal and saved to `data/analysis_report.txt`.

## Project Structure
```text
LogFileAnalyzer/
├── data/
│   ├── app.log                 # Sample input log file (contains valid and deliberate malformed rows)
│   └── analysis_report.txt     # Generated analysis report
├── src/
│   └── com/
│       └── loganalyzer/
│           ├── LogEntry.java        # POJO data model for parsed log entries
│           ├── LogParser.java       # String parsing, regex matching & validation logic
│           ├── LogAnalyzer.java     # File processing, HashMap aggregations & statistics
│           ├── ReportGenerator.java # Report formatting and disk output
│           └── Main.java            # Application entry point
├── bin/                             # Compiled Java class files
├── .gitignore
├── REPORT.md                        # Internship project report & documentation
└── README.md
```

## How to Compile and Run

1. **Open a terminal** and navigate to the `LogFileAnalyzer` directory:
   ```bash
   cd LogFileAnalyzer
   ```

2. **Create the `bin` directory** (if it does not already exist):
   ```bash
   mkdir -p bin
   ```

3. **Compile the Java source files:**
   ```bash
   javac -d bin src/com/loganalyzer/*.java
   ```

4. **Run the program:**
   ```bash
   java -cp bin com.loganalyzer.Main
   ```

   *(Optional) You can pass custom input and output file paths as command-line arguments:*
   ```bash
   java -cp bin com.loganalyzer.Main "path/to/custom.log" "path/to/output_report.txt"
   ```

## Sample Output
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
...
[SUCCESS] Report saved to: data/analysis_report.txt
```
