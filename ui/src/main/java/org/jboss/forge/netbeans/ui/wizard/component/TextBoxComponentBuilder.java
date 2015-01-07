/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.wizard.component;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.convert.ConverterFactory;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.util.InputComponents;

/**
 * Generates a JTextField with a JLabel
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class TextBoxComponentBuilder extends ComponentBuilder {

    @Override
    public JComponent build(final Container container, final InputComponent<?, Object> input, final CommandController controller) {
        JLabel label = new JLabel(InputComponents.getLabelFor(input, true));
        final JTextField txt = new JTextField();
        txt.setEnabled(input.isEnabled());
        txt.setToolTipText(input.getDescription());

        final ConverterFactory converterFactory = getConverterFactory();
        if (converterFactory != null) {
            Converter<Object, String> converter = (Converter<Object, String>) converterFactory
                    .getConverter(input.getValueType(), String.class);
            String value = converter
                    .convert(InputComponents.getValueFor(input));
            txt.setText(value == null ? "" : value);
        }
        
        txt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    controller.setValueFor(input.getName(), txt.getText());
                } catch (Exception e) {
                    //TODO: log
                    e.printStackTrace();
                }
            }
        });

        // Add components to container
        container.add(label);
        container.add(txt);

        return txt;
    }

    @Override
    protected Class<String> getProducedType() {
        return String.class;
    }

    @Override
    protected String getSupportedInputType() {
        return InputType.TEXTBOX;
    }

    @Override
    protected Class<?>[] getSupportedInputComponentTypes() {
        return new Class<?>[]{UIInput.class};
    }

}