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
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.fife.ui.autocomplete.AutoCompletion;
import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.convert.ConverterFactory;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.HasCompleter;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.input.UICompleter;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.util.InputComponents;
import org.jboss.forge.netbeans.ui.wizard.component.completion.ForgeAutoCompletionProvider;
import org.openide.util.ChangeSupport;
import org.openide.util.lookup.ServiceProvider;

/**
 * Generates a JTextField with a JLabel
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@ServiceProvider(position = 40, service = ComponentBuilder.class)
public class TextboxComponentBuilder extends ComponentBuilder<JTextField> {

    @Override
    public JTextField build(final Container container, final InputComponent<?, Object> input, final CommandController controller, final ChangeSupport changeSupport) {
        JLabel label = new JLabel(InputComponents.getLabelFor(input, true));
        final JTextField txt = new JTextField();
        txt.setEnabled(input.isEnabled());
        txt.setToolTipText(input.getDescription());
        setupAutoCompleteFor(controller.getContext(), input, txt);
        final ConverterFactory converterFactory = getConverterFactory();
        if (converterFactory != null) {
            Object value = InputComponents.getValueFor(input);
            if (value != null) {
                Converter<Object, String> converter = (Converter<Object, String>) converterFactory
                        .getConverter(input.getValueType(), String.class);
                txt.setText(converter.convert(value));
            }
        }

        txt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                controller.setValueFor(input.getName(), txt.getText());
                changeSupport.fireChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                controller.setValueFor(input.getName(), txt.getText());
                changeSupport.fireChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                controller.setValueFor(input.getName(), txt.getText());
                changeSupport.fireChange();
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

    protected void setupAutoCompleteFor(UIContext context, InputComponent<?, Object> input, JTextComponent txt) {
        UICompleter<String> completer = null;
        if (input instanceof HasCompleter) {
            completer = ((HasCompleter) input).getCompleter();
        }

        if (completer != null) {
            ForgeAutoCompletionProvider provider = new ForgeAutoCompletionProvider(context, input, completer);
            AutoCompletion autoCompletion = new AutoCompletion(provider);
            autoCompletion.install(txt);
        }
    }
}
