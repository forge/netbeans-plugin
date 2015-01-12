/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.netbeans.ui.wizard.component;

import java.util.ServiceLoader;
import org.jboss.forge.addon.ui.input.InputComponent;

/**
 * A factory for {@link ComponentBuilder} instances.
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public enum ComponentBuilderRegistry {

    INSTANCE;

    private final ServiceLoader<ComponentBuilder> componentBuilders;

    private ComponentBuilderRegistry() {
        this.componentBuilders = ServiceLoader.load(ComponentBuilder.class);
    }

    public ComponentBuilder getBuilderFor(InputComponent<?, ?> input) {
        for (ComponentBuilder builder : componentBuilders) {
            if (builder.handles(input)) {
                return builder;
            }
        }
        return new FallbackComponentBuilder();
    }
}
