package com.pdp.analyzer;

import com.pdp.analyzer.cli.ConsoleApplication;
import com.pdp.analyzer.service.JavaSourceAnalyzer;
import com.pdp.analyzer.service.RepositoryAnalyzer;
import com.pdp.analyzer.service.RepositoryCloner;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        ConsoleApplication application = new ConsoleApplication(
                new RepositoryCloner(),
                new RepositoryAnalyzer(new JavaSourceAnalyzer()));
        application.run();
    }
}
