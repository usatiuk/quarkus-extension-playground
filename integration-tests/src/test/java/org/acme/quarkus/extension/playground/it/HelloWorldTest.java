package org.acme.quarkus.extension.playground.it;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.quarkus.extension.playground.DataType;
import org.acme.quarkus.extension.playground.runtime.HelloGenerator;
import org.acme.quarkus.extension.playground.runtime.HelloGeneratorGeneric;
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

    @Inject
    HelloGeneratorGeneric<DataType> helloGeneratorGeneric;

    @Test
    void testHelloGeneric() {
        DataType dataType = new DataType();
        dataType.name = "World";
        String hello = helloGeneratorGeneric.generate(dataType);
        Assertions.assertEquals("Hello,\nname: World", hello);
    }
}

