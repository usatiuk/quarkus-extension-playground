package org.acme.quarkus.extension.playground.runtime;

import io.quarkus.arc.Unremovable;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Unremovable
public class Wrapper {
    public void start() {
        Log.info("Start called");
    }

    public void commit() {
        Log.info("Commit called");
    }

    public void revert() {
        Log.info("Revert called");
    }
}
