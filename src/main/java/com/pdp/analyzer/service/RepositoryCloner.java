package com.pdp.analyzer.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public final class RepositoryCloner {
    public Path cloneRepository(String repositoryUrl, Path destination) throws IOException, InterruptedException {
        if (repositoryUrl == null || repositoryUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Repository URL cannot be empty.");
        }
        if (destination == null) {
            throw new IllegalArgumentException("Destination cannot be null.");
        }
        Path normalizedDestination = destination.toAbsolutePath().normalize();
        if (Files.exists(normalizedDestination)) {
            try (java.util.stream.Stream<Path> children = Files.list(normalizedDestination)) {
                if (children.findAny().isPresent()) {
                    throw new IOException("Destination must be empty: " + normalizedDestination);
                }
            }
        }
        Files.createDirectories(normalizedDestination);

        Process process = new ProcessBuilder("git", "clone", repositoryUrl.trim(), normalizedDestination.toString())
                .redirectErrorStream(true)
                .start();
        String output = readOutput(process.getInputStream());
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("git clone failed (exit code " + exitCode + "): " + output.trim());
        }
        return normalizedDestination;
    }

    public Path cloneRepository(String repositoryUrl, String destination) throws IOException, InterruptedException {
        return cloneRepository(repositoryUrl, Paths.get(destination));
    }

    private String readOutput(InputStream input) throws IOException {
        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int count;
        while ((count = input.read(buffer)) != -1) {
            output.write(buffer, 0, count);
        }
        return new String(output.toByteArray(), StandardCharsets.UTF_8);
    }
}
