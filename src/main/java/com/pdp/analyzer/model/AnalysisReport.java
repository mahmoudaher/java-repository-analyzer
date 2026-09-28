package com.pdp.analyzer.model;

import java.nio.file.Path;

/** Metrics collected for one Java source file. */
public final class AnalysisReport {
    private final Path file;
    private final int totalLines;
    private final int codeLines;
    private final int commentLines;
    private final int javadocLines;
    private final int methodCount;

    public AnalysisReport(Path file, int totalLines, int codeLines, int commentLines,
                          int javadocLines, int methodCount) {
        this.file = file;
        this.totalLines = totalLines;
        this.codeLines = codeLines;
        this.commentLines = commentLines;
        this.javadocLines = javadocLines;
        this.methodCount = methodCount;
    }

    public Path getFile() { return file; }
    public int getTotalLines() { return totalLines; }
    public int getCodeLines() { return codeLines; }
    public int getCommentLines() { return commentLines; }
    public int getJavadocLines() { return javadocLines; }
    public int getMethodCount() { return methodCount; }

    public double getCommentDeviationPercentage() {
        if (methodCount == 0 || codeLines == 0) {
            return 0.0;
        }
        double commentAverage = ((javadocLines + commentLines) * 0.8) / methodCount;
        double codeAverage = (codeLines * 0.3) / methodCount;
        return ((commentAverage / codeAverage) * 100.0) - 100.0;
    }
}
