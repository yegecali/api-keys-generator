package com.yegecali.keysgenerator;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.jboss.logging.Logger;

@QuarkusMain
public class Main implements QuarkusApplication {

    private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String... args) {
        // Start Quarkus using this class as the QuarkusApplication
        Quarkus.run(Main.class, args);
    }

    @Override
    public int run(String... args) {
        LOG.info("Application started with custom QuarkusMain");
        // Optional bootstrap logic (migrations, warmups, etc.) can be placed here

        // Wait for the application to exit (prevents run() from returning immediately)
        Quarkus.waitForExit();
        return 0;
    }
}
