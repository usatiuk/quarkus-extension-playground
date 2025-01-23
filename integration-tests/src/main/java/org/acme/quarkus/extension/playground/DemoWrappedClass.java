package org.acme.quarkus.extension.playground;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.quarkus.extension.playground.runtime.Wrapped;

@ApplicationScoped
public class DemoWrappedClass {
    @Wrapped
    public void empty() {
    }

    @Wrapped
    public String returnString() {
        return "Hello";
    }

    @Wrapped
    public void throwException() {
        throw new RuntimeException("Exception");
    }
}
