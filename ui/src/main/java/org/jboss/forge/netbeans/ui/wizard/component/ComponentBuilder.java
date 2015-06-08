/* 
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    <a href="mailto:ggastald@redhat.com">George Gastaldi</a> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui.wizard.component;

import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jboss.forge.addon.convert.ConverterFactory;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.util.InputComponents;
import org.jboss.forge.furnace.util.Strings;
import org.jboss.forge.netbeans.runtime.FurnaceService;
import org.openide.util.ChangeSupport;

public abstract class ComponentBuilder<C extends JComponent> {

    private static final String NOTE_CLIENT_PROPERTY_KEY = "forge.note";

    /**
     * Builds a UI Component object based on the input
     *
     * @param input
     * @return
     */
    public abstract C build(
            final Container container,
            final InputComponent<?, Object> input,
            final CommandController controller,
            final ChangeSupport changeSupport);

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
                handles = inputTypeHint.equals(getSupportedInputType());
            } else {
                // Fallback to standard type
                handles = getProducedType().isAssignableFrom(
                        input.getValueType());
            }
        }

        return handles;
    }

    protected ConverterFactory getConverterFactory() {
        return FurnaceService.INSTANCE.getConverterFactory();
    }

    public void setupNote(JPanel parent, C component, InputComponent<?, ?> input) {
        final JLabel noteLabel = new JLabel();
        // Hide empty labels
        noteLabel.addPropertyChangeListener("text", new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                noteLabel.setVisible(!Strings.isNullOrEmpty(noteLabel.getText()));
            }
        });
        
        String note = input.getNote();
        if (!Strings.isNullOrEmpty(note)) {
            noteLabel.setText(note);
        }
        parent.add(noteLabel,"skip 1,hidemode 2");
        component.putClientProperty(NOTE_CLIENT_PROPERTY_KEY, noteLabel);
    }

    /**
     * Called when any change in the produced {@link JComponent} happens
     *
     * @param jc
     * @param input
     */
    public void updateState(C component, InputComponent<?, ?> input) {
        setEnabled(component, input.isEnabled());
        setTooltip(component, input.getDescription());
        setNote(component, input.getNote());
    }

    protected void setEnabled(C component, boolean enabled) {
        component.setEnabled(enabled);
    }

    protected void setTooltip(C component, String text) {
        component.setToolTipText(text);
    }

    protected void setNote(C component, String note) {
        JLabel noteLabel = (JLabel) component.getClientProperty(NOTE_CLIENT_PROPERTY_KEY);
        if (noteLabel != null) {
            noteLabel.setText(note);
        }
    }
}
