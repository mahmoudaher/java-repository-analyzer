package com.pdp.analyzer.service;

import com.pdp.analyzer.model.AnalysisReport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public final class JavaSourceAnalyzer {
    private static final Pattern TYPE_DECLARATION = Pattern.compile("\\b(class|interface|enum|record)\\b");
    private static final Pattern METHOD_DECLARATION = Pattern.compile(
            "(?:public|protected|private|static|final|abstract|synchronized|native|default|\\s)+"
                    + "[\\w<>\\[\\], ?]+\\s+\\w+\\s*\\([^;{}]*\\)\\s*(?:throws [^{]+)?\\{?");

    public AnalysisReport analyze(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file);
        int comments = 0;
        int javadocs = 0;
        int code = 0;
        boolean inBlockComment = false;
        boolean inJavadoc = false;
        int methods = 0;

        for (String rawLine : lines) {
            String line = rawLine.trim();
            boolean hadComment = false;
            boolean hadCode = false;
            String remaining = line;

            while (!remaining.isEmpty()) {
                if (inBlockComment) {
                    hadComment = true;
                    int end = remaining.indexOf("*/");
                    if (end < 0) {
                        remaining = "";
                    } else {
                        if (inJavadoc) {
                            javadocs++;
                        }
                        inBlockComment = false;
                        inJavadoc = false;
                        remaining = remaining.substring(end + 2).trim();
                    }
                } else if (remaining.startsWith("//")) {
                    hadComment = true;
                    remaining = "";
                } else if (remaining.startsWith("/*")) {
                    hadComment = true;
                    inBlockComment = true;
                    inJavadoc = remaining.startsWith("/**");
                    remaining = remaining.substring(2).trim();
                } else {
                    int lineComment = remaining.indexOf("//");
                    int blockComment = remaining.indexOf("/*");
                    int commentStart = firstCommentIndex(lineComment, blockComment);
                    String codePart = commentStart < 0 ? remaining : remaining.substring(0, commentStart);
                    if (!codePart.trim().isEmpty()) {
                        hadCode = true;
                    }
                    if (commentStart < 0) {
                        remaining = "";
                    } else {
                        remaining = remaining.substring(commentStart).trim();
                    }
                }
            }

            if (hadComment) {
                comments++;
            }
            if (hadCode) {
                code++;
                if (METHOD_DECLARATION.matcher(line).matches()
                        && !TYPE_DECLARATION.matcher(line).find()) {
                    methods++;
                }
            }
            if (inJavadoc && hadComment && !line.startsWith("/**") && !line.equals("*/")) {
                javadocs++;
            }
        }
        return new AnalysisReport(file, lines.size(), code, comments, javadocs, methods);
    }

    private int firstCommentIndex(int lineComment, int blockComment) {
        if (lineComment < 0) return blockComment;
        if (blockComment < 0) return lineComment;
        return Math.min(lineComment, blockComment);
    }
}
