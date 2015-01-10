/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.netbeans.ui.wizard.component;

import org.jboss.forge.addon.ui.input.InputComponent;

/**
 * A factory for {@link ComponentBuilder} instances.
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public enum ComponentBuilderRegistry {

    INSTANCE;

    private final ComponentBuilder[] componentBuilders = {
        new CheckboxComponentBuilder(),
        new ComboComponentBuilder(),
        new TextboxComponentBuilder(),
        new PasswordComponentBuilder(),
        new FallbackComponentBuilder()
    };

    public ComponentBuilder getBuilderFor(InputComponent<?, ?> input) {
        for (ComponentBuilder builder : componentBuilders) {
            if (builder.handles(input)) {
                return builder;
            }
        }
        throw new IllegalArgumentException(
                "No UI component found for input type of "
                + input.getValueType()
        );
    }
}
