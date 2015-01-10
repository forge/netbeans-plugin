/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.wizard.component;

import java.awt.Container;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.util.InputComponents;
import org.openide.util.ChangeSupport;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class SpinnerComponentBuilder extends ComponentBuilder<JSpinner> {

    @Override
    public JSpinner build(Container container, final InputComponent<?, Object> input, final CommandController controller, final ChangeSupport changeSupport) {
        final JSpinner spinner = new JSpinner();
        spinner.setEnabled(input.isEnabled());
        Converter<Object, Integer> converter = getConverterFactory()
                .getConverter(input.getValueType(), Integer.class);
        spinner.setValue(converter.convert(input.getValue()));
        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                controller.setValueFor(input.getName(), spinner.getValue());
                changeSupport.fireChange();
            }
        });
        container.add(new JLabel(InputComponents.getLabelFor(input, true)));
        container.add(spinner);
        return spinner;
    }

    @Override
    protected Class<?> getProducedType() {
        return Integer.class;
    }

    @Override
    protected String getSupportedInputType() {
        return InputType.DEFAULT;
    }

    @Override
    protected Class<?>[] getSupportedInputComponentTypes() {
        return new Class<?>[]{UIInput.class
        };
    }

}
