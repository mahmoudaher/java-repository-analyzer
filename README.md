# Java Repository Analyzer

A small command-line tool that clones a Git repository and analyzes its Java source files.
It reports source size, code lines, comments, Javadoc, methods, and comment deviation.

## Features

- Clone a repository into a user-selected directory.
- Recursively discover `.java` files.
- Count total, code, comment, and Javadoc lines.
- Estimate the number of methods in each source file.
- Keep the cloned repository after analysis so the result is not destructive.
- Separate cloning, source analysis, reporting, and the command-line interface.

## Project Structure

```text
src/
├── main/java/com/pdp/analyzer/
│   ├── Main.java
│   ├── cli/ConsoleApplication.java
│   ├── model/AnalysisReport.java
│   └── service/
│       ├── JavaSourceAnalyzer.java
│       ├── RepositoryAnalyzer.java
│       └── RepositoryCloner.java
└── test/java/com/pdp/analyzer/service/
	└── JavaSourceAnalyzerTest.java
```

## Requirements

- Java Development Kit 8 or newer
- Git installed and available in `PATH`
- Maven 3.8 or newer (recommended)

## Run With Maven

```bash
mvn clean test
mvn package
java -jar target/java-repository-analyzer-1.0.0.jar
```

The application asks for a Git repository URL and an empty destination directory.

## Run Without Maven

```bash
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -cp out com.pdp.analyzer.Main
```

On Windows PowerShell:

```powershell
$sources = Get-ChildItem src/main/java -Recurse -Filter *.java
javac -encoding UTF-8 -d out $sources.FullName
java -cp out com.pdp.analyzer.Main
```

## Design Notes

The application is intentionally dependency-free. `RepositoryCloner` owns Git process
execution, `RepositoryAnalyzer` discovers source files, `JavaSourceAnalyzer` calculates
metrics for one file, and `ConsoleApplication` handles user interaction. This keeps the
core logic testable and makes it possible to add JSON or CSV output later without changing
the analysis engine.

## Limitations

Method detection is regex-based and is intended for lightweight metrics, not full Java
parsing. Strings containing comment markers and unusual language constructs may affect the
counts. For compiler-accurate metrics, the analyzer can later be upgraded to a Java parser.

## License

This project is provided for educational and development use.