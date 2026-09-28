package com.pdp.analyzer.cli;

import com.pdp.analyzer.model.AnalysisReport;
import com.pdp.analyzer.service.JavaSourceAnalyzer;
import com.pdp.analyzer.service.RepositoryAnalyzer;
import com.pdp.analyzer.service.RepositoryCloner;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public final class ConsoleApplication {
    private final RepositoryCloner cloner;
    private final RepositoryAnalyzer analyzer;

    public ConsoleApplication(RepositoryCloner cloner, RepositoryAnalyzer analyzer) {
        this.cloner = cloner;
        this.analyzer = analyzer;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Java Repository Analyzer");
        System.out.println("-----------------------");
        System.out.print("Git repository URL: ");
        String url = scanner.nextLine().trim();
        System.out.print("Clone destination path: ");
        Path destination = Paths.get(scanner.nextLine().trim());

        try {
            Path repository = cloner.cloneRepository(url, destination);
            List<AnalysisReport> reports = analyzer.analyze(repository);
            printReports(reports);
            System.out.println("Analysis completed. Repository kept at: " + repository);
        } catch (IllegalArgumentException | IOException error) {
            System.err.println("Error: " + error.getMessage());
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            System.err.println("Operation interrupted.");
        }
    }

    private void printReports(List<AnalysisReport> reports) {
        if (reports.isEmpty()) {
            System.out.println("No Java source files were found.");
            return;
        }
        System.out.println();
        for (AnalysisReport report : reports) {
            System.out.println("File: " + report.getFile());
            System.out.println("  Total lines: " + report.getTotalLines());
            System.out.println("  Code lines: " + report.getCodeLines());
            System.out.println("  Comment lines: " + report.getCommentLines());
            System.out.println("  Javadoc lines: " + report.getJavadocLines());
            System.out.println("  Methods: " + report.getMethodCount());
            System.out.printf(Locale.ROOT, "  Comment deviation: %.2f%%%n", report.getCommentDeviationPercentage());
            System.out.println();
        }
        System.out.println("Files analyzed: " + reports.size());
    }
}
