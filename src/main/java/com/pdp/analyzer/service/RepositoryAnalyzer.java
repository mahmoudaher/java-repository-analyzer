package com.pdp.analyzer.service;

import com.pdp.analyzer.model.AnalysisReport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class RepositoryAnalyzer {
    private final JavaSourceAnalyzer sourceAnalyzer;

    public RepositoryAnalyzer(JavaSourceAnalyzer sourceAnalyzer) {
        this.sourceAnalyzer = sourceAnalyzer;
    }

    public List<AnalysisReport> analyze(Path repository) throws IOException {
        if (repository == null || !Files.isDirectory(repository)) {
            throw new IOException("Repository directory does not exist: " + repository);
        }
        try (Stream<Path> files = Files.walk(repository)) {
            return files.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .sorted(Comparator.naturalOrder())
                    .map(this::analyzeSafely)
                    .filter(report -> report != null)
                    .collect(Collectors.toList());
        }
    }

    private AnalysisReport analyzeSafely(Path file) {
        try {
            return sourceAnalyzer.analyze(file);
        } catch (IOException error) {
            return null;
        }
    }
}
