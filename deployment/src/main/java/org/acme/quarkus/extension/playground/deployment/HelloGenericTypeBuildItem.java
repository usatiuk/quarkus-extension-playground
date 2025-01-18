package org.acme.quarkus.extension.playground.deployment;

import io.quarkus.builder.item.MultiBuildItem;
import org.jboss.jandex.ClassInfo;

public final class HelloGenericTypeBuildItem extends MultiBuildItem {
    public final ClassInfo klass;

    public HelloGenericTypeBuildItem(ClassInfo klass) {
        this.klass = klass;
    }
}
