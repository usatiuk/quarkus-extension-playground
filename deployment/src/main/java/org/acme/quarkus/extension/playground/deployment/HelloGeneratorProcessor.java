package org.acme.quarkus.extension.playground.deployment;

import io.quarkus.arc.deployment.GeneratedBeanBuildItem;
import io.quarkus.arc.deployment.GeneratedBeanGizmoAdaptor;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import jakarta.inject.Singleton;
import org.acme.quarkus.extension.playground.runtime.HelloGenerator;

public class HelloGeneratorProcessor {
    @BuildStep
    void generateImplementation(BuildProducer<GeneratedBeanBuildItem> generatedBean) {
        var gizmoAdapter = new GeneratedBeanGizmoAdaptor(generatedBean);

        try (ClassCreator classCreator = ClassCreator.builder()
                .className("org.acme.quarkus.extension.playground.deployment.HelloGeneratorImpl")
                .interfaces(HelloGenerator.class)
                .classOutput(gizmoAdapter)
                .build()) {

            classCreator.addAnnotation(Singleton.class);

            try (MethodCreator methodCreator = classCreator.getMethodCreator("generate", String.class, String.class)) {
                var method = MethodDescriptor.ofMethod(String.class, "concat", String.class, String.class);
                var name = methodCreator.getMethodParam(0);
                var prefix = methodCreator.load("Hello ");
                var concatenated = methodCreator.invokeVirtualMethod(method,
                        prefix, name);
                methodCreator.returnValue(concatenated);
            }
        }
    }
}
