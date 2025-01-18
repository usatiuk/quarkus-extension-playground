package org.acme.quarkus.extension.playground.deployment;

import io.quarkus.arc.deployment.GeneratedBeanBuildItem;
import io.quarkus.arc.deployment.GeneratedBeanGizmoAdaptor;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationIndexBuildItem;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.SignatureBuilder;
import io.quarkus.gizmo.Type;
import jakarta.inject.Singleton;
import org.acme.quarkus.extension.playground.runtime.HelloGeneratorGeneric;
import org.acme.quarkus.extension.playground.runtime.HelloTarget;
import org.jboss.jandex.AnnotationTarget;

import java.util.List;

public class HelloGenericGeneratorProcessor {
    @BuildStep
    void findAnnotations(ApplicationIndexBuildItem jandex, BuildProducer<HelloGenericTypeBuildItem> producer) {
        var annot = jandex.getIndex().getAnnotations(HelloTarget.class);
        for (var a : annot) {
            if (a.target().kind() != AnnotationTarget.Kind.CLASS) {
                continue;
            }
            producer.produce(new HelloGenericTypeBuildItem(a.target().asClass()));
        }
    }

    @BuildStep
    void generateImplementations(BuildProducer<GeneratedBeanBuildItem> generatedBeans, List<HelloGenericTypeBuildItem> types) {
        for (var type : types) {
            var gizmoAdapter = new GeneratedBeanGizmoAdaptor(generatedBeans);

            var targetType = Type.classType(type.klass.name());
            var implType = Type.ParameterizedType.parameterizedType(
                    Type.classType(HelloGeneratorGeneric.class),
                    targetType);

            try (ClassCreator classCreator = ClassCreator.builder()
                    .className("org.acme.quarkus.extension.playground.deployment." + type.klass.simpleName() + "GeneratorImpl")
                    .signature(SignatureBuilder.forClass().addInterface(implType))
                    .classOutput(gizmoAdapter)
                    .setFinal(true)
                    .build()) {
                classCreator.addAnnotation(Singleton.class);

                try (var methodCreator = classCreator.getMethodCreator("generate", String.class, Object.class)) {
                    var arg = methodCreator.getMethodParam(0);
                    var prefix = methodCreator.load("Hello,");
                    var strConcat = MethodDescriptor.ofMethod(String.class, "concat", String.class, String.class);
                    var toStringMethod = MethodDescriptor.ofMethod(Object.class, "toString", String.class);
                    var concatenated = prefix;
                    for (var field : type.klass.fields()) {
                        var fieldVal = methodCreator.readInstanceField(field, arg);
                        concatenated = methodCreator.invokeVirtualMethod(strConcat,
                                concatenated, methodCreator.load("\n" + field.name() + ": "));
                        var fieldStringified = methodCreator.invokeVirtualMethod(toStringMethod, fieldVal);
                        concatenated = methodCreator.invokeVirtualMethod(strConcat, concatenated, fieldStringified);
                    }

                    methodCreator.returnValue(concatenated);
                }
            }
        }
    }
}
