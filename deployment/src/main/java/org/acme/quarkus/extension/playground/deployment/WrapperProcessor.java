package org.acme.quarkus.extension.playground.deployment;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ApplicationIndexBuildItem;
import io.quarkus.deployment.builditem.BytecodeTransformerBuildItem;
import io.quarkus.gizmo.ClassTransformer;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import org.acme.quarkus.extension.playground.runtime.Wrapped;
import org.acme.quarkus.extension.playground.runtime.WrapperStatic;

public class WrapperProcessor {
    @BuildStep
    void transform(ApplicationIndexBuildItem jandex, BuildProducer<BytecodeTransformerBuildItem> bytecodeTransformers) {
        var annot = jandex.getIndex().getAnnotations(Wrapped.class);
        for (var a : annot) {
            var target = a.target().asMethod();
            var targetClass = target.declaringClass();
            ClassTransformer ct = new ClassTransformer(targetClass.name().toString());

            // Rename original method
            ct.modifyMethod(MethodDescriptor.of(target)).rename(target.name() + "$original");

            // Create a wrapper method in place of the original one
            try (var methodCreator = ct.addMethod(MethodDescriptor.of(target))) {
                methodCreator.invokeStaticMethod(MethodDescriptor.ofMethod(WrapperStatic.class, "start", void.class));

                var tryBlock = methodCreator.tryBlock();

                // Wrap original method parameters
                var passedArgs = new ResultHandle[target.parameters().size()];
                for (int i = 0; i < target.parameters().size(); i++) {
                    passedArgs[i] = tryBlock.getMethodParam(i);
                }

                var originalDescriptor = MethodDescriptor.ofMethod(targetClass.name().toString(),
                        target.name() + "$original", target.returnType().name().toString());

                // Call the original method
                var returned = tryBlock.invokeVirtualMethod(originalDescriptor, tryBlock.getThis(), passedArgs);

                tryBlock.invokeStaticMethod(MethodDescriptor.ofMethod(WrapperStatic.class, "commit", void.class));
                tryBlock.returnValue(returned);

                var catchBlock = tryBlock.addCatch(Throwable.class);
                catchBlock.invokeStaticMethod(MethodDescriptor.ofMethod(WrapperStatic.class, "revert", void.class));
                catchBlock.throwException(catchBlock.getCaughtException());
            }

            bytecodeTransformers.produce(new BytecodeTransformerBuildItem.Builder()
                    .setClassToTransform(target.declaringClass().toString())
                    .setVisitorFunction((ignored, visitor) -> ct.applyTo(visitor))
                    .build());
        }
    }
}
