package org.acme.quarkus.extension.playground.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class QuarkusExtensionPlaygroundProcessor {

    private static final String FEATURE = "quarkus-extension-playground";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }
}
