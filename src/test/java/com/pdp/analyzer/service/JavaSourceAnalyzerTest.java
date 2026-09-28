package com.pdp.analyzer.service;

import com.pdp.analyzer.model.AnalysisReport;

import java.nio.file.Files;
import java.nio.file.Path;

public final class JavaSourceAnalyzerTest {
    public static void main(String[] args) throws Exception {
        Path source = Files.createTempFile("AnalyzerTest", ".java");
        Files.write(source, java.util.Arrays.asList(
                "public class Sample {",
                "    // comment",
                "    /** docs */",
                "    public void run() {}",
                "}"));

        AnalysisReport report = new JavaSourceAnalyzer().analyze(source);
        assert report.getTotalLines() == 5;
        assert report.getCommentLines() == 2;
        assert report.getCodeLines() == 3;
        assert report.getMethodCount() == 1;
        Files.deleteIfExists(source);
        System.out.println("JavaSourceAnalyzerTest passed");
    }
}
