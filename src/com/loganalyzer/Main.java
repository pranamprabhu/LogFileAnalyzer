package com.loganalyzer;

import java.io.IOException;

/**
 * Entry point for the Log File Analyzer application.
 */
public class Main {

    public static void main(String[] args) {
        String inputPath = (args.length > 0) ? args[0] : "data/app.log";
        String outputPath = (args.length > 1) ? args[1] : "data/analysis_report.txt";

        System.out.println("=================================================");
        System.out.println("          LOG FILE ANALYZER APPLICATION          ");
        System.out.println("=================================================");
        System.out.println("Input Log File : " + inputPath);
        System.out.println("Output Report  : " + outputPath);
        System.out.println("-------------------------------------------------");
        System.out.println("Processing log file...\n");

        LogAnalyzer analyzer = new LogAnalyzer();

        try {
            analyzer.analyze(inputPath);

            ReportGenerator generator = new ReportGenerator();
            String report = generator.buildReport(analyzer);

            // Print report to standard output
            System.out.println(report);

            // Save report to disk
            generator.saveReportToFile(report, outputPath);
            System.out.println("[SUCCESS] Report saved to: " + outputPath);

        } catch (IOException e) {
            System.err.println("[ERROR] Failed to process log file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
