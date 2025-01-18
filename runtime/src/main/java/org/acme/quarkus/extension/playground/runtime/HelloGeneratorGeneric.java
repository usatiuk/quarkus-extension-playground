package org.acme.quarkus.extension.playground.runtime;

public interface HelloGeneratorGeneric<T> {
    String generate(T name);
}
