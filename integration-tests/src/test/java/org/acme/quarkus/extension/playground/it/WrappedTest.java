package org.acme.quarkus.extension.playground.it;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.quarkus.extension.playground.DemoWrappedClass;
import org.acme.quarkus.extension.playground.runtime.Wrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class WrappedTest {
    @Inject
    DemoWrappedClass demoWrappedClass;

    @InjectMock
    Wrapper wrapper;

    @Test
    void testReturn() {
        Assertions.assertEquals("Hello", demoWrappedClass.returnString());
        Mockito.verify(wrapper, Mockito.times(1)).start();
        Mockito.verify(wrapper, Mockito.times(1)).commit();
        Mockito.verify(wrapper, Mockito.times(0)).revert();
    }

    @Test
    void testEmpty() {
        demoWrappedClass.empty();
        Mockito.verify(wrapper, Mockito.times(1)).start();
        Mockito.verify(wrapper, Mockito.times(1)).commit();
        Mockito.verify(wrapper, Mockito.times(0)).revert();
    }

    @Test
    void testException() {
        Assertions.assertThrows(RuntimeException.class, () -> demoWrappedClass.throwException());
        Mockito.verify(wrapper, Mockito.times(1)).start();
        Mockito.verify(wrapper, Mockito.times(0)).commit();
        Mockito.verify(wrapper, Mockito.times(1)).revert();
    }
}

