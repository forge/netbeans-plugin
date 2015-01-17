/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.wizard.component;

import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.convert.ConverterFactory;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.util.InputComponents;
import org.openide.util.ChangeSupport;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@ServiceProvider(position = 10, service = ComponentBuilder.class)
public class CheckboxComponentBuilder extends ComponentBuilder<JCheckBox> {

    @Override
    public JCheckBox build(final Container container, final InputComponent<?, Object> input, final CommandController controller, final ChangeSupport changeSupport) {
        final JCheckBox checkbox = new JCheckBox(InputComponents.getLabelFor(input, false));
        checkbox.setEnabled(input.isEnabled());
        checkbox.setToolTipText(input.getDescription());
        final ConverterFactory converterFactory = getConverterFactory();
        if (converterFactory != null) {
            Converter<Object, Boolean> converter = converterFactory.getConverter(input.getValueType(), Boolean.class);
            final Boolean value = converter.convert(input.getValue());
            checkbox.setSelected(value == null ? false : value);
        }
        checkbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                controller.setValueFor(input.getName(), checkbox.isSelected());
                changeSupport.fireChange();
            }
        });
        container.add(new JLabel());
        container.add(checkbox);
        return checkbox;
    }

    @Override
    protected void setEnabled(JCheckBox component, boolean enabled) {
        component.setEnabled(enabled);
    }

    @Override
    protected Class<?> getProducedType() {
        return Boolean.class;
    }

    @Override
    protected String getSupportedInputType() {
        return InputType.CHECKBOX;
    }

    @Override
    protected Class<?>[] getSupportedInputComponentTypes() {
        return new Class<?>[]{UIInput.class};
    }

}
