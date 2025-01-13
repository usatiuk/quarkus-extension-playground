package org.acme.quarkus.extension.playground.it;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.quarkus.extension.playground.runtime.HelloGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class HelloWorldTest {
    @Inject
    HelloGenerator helloGenerator;

    @Test
    void testHello() {
        String hello = helloGenerator.generate("World");
        Assertions.assertEquals("Hello World", hello);
    }
}
