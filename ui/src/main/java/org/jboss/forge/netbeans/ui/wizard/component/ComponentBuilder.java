/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.netbeans.ui.wizard.component;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JComponent;
import org.jboss.forge.addon.convert.ConverterFactory;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.util.InputComponents;
import org.jboss.forge.furnace.proxy.Proxies;
import org.jboss.forge.netbeans.runtime.FurnaceService;

public abstract class ComponentBuilder {

    /**
     * Builds a UI Component object based on the input
     *
     * @param input
     * @return
     */
    public abstract JComponent build(Container container, InputComponent<?, Object> input, CommandController controller);

    /**
     * Returns the supported type this control may produce
     *
     * @return
     */
    protected abstract Class<?> getProducedType();

    /**
     * Returns the supported input type for this component
     *
     * @return
     */
    protected abstract String getSupportedInputType();

    /**
     * Returns the subclasses of {@link InputComponent}
     *
     * @return
     */
    protected abstract Class<?>[] getSupportedInputComponentTypes();

    /**
     * Tests if this builder may handle this specific input
     *
     * @param input
     * @return
     */
    public boolean handles(InputComponent<?, ?> input) {
        boolean handles = false;
        String inputTypeHint = InputComponents.getInputType(input);

        for (Class<?> inputType : getSupportedInputComponentTypes()) {
            if (inputType.isAssignableFrom(input.getClass())) {
                handles = true;
                break;
            }
        }

        if (handles) {
            if (inputTypeHint != null && !InputType.DEFAULT.equals(inputTypeHint)) {
                handles = Proxies.areEquivalent(inputTypeHint,
                        getSupportedInputType());
            } else {
                // Fallback to standard type
                handles = getProducedType().isAssignableFrom(
                        input.getValueType());
            }
        }

        return handles;
    }

    public void setEnabled(JComponent component, boolean enabled) {
        if (component instanceof Container) {
            for (Component c : ((Container) component).getComponents()) {
                c.setEnabled(enabled);
            }
        } else {
            component.setEnabled(enabled);
        }
    }

    protected ConverterFactory getConverterFactory() {
        return FurnaceService.INSTANCE.getConverterFactory();
    }

}
