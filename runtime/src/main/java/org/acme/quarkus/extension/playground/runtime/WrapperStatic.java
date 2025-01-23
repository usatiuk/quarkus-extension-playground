package org.acme.quarkus.extension.playground.runtime;

import jakarta.enterprise.inject.spi.CDI;

public class WrapperStatic {
    public static void start() {
        CDI.current().select(Wrapper.class).get().start();
    }

    public static void commit() {
        CDI.current().select(Wrapper.class).get().commit();
    }

    public static void revert() {
        CDI.current().select(Wrapper.class).get().revert();
    }
}
