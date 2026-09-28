# Java Repository Analyzer

Dependency-free Java command-line tool for cloning a Git repository and calculating lightweight source-code metrics for Java files.

## Features

- Clone a repository into an empty destination.
- Discover Java files recursively.
- Count total lines, code lines, comments, and Javadoc.
- Estimate method counts and comment deviation.
- Keep the cloned repository after analysis.

## Architecture

`RepositoryCloner` handles Git, `RepositoryAnalyzer` discovers files, `JavaSourceAnalyzer` calculates metrics, `AnalysisReport` stores results, and `ConsoleApplication` handles interaction.

## Run

```bash
mvn clean test
mvn package
java -jar target/java-repository-analyzer-1.0.0.jar
```

Requires JDK 8 or newer and Git. Method detection is regex-based and intended for educational metrics.