package org.jboss.forge.netbeans.ui.wizard.component;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.convert.ConverterFactory;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.util.InputComponents;
import org.openide.util.ChangeSupport;

/**
 * Represents a component with a Browse... button next to a {@link JTextField}
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public abstract class AbstractTextButtonComponentBuilder extends ComponentBuilder<JTextField> {

    @Override
    public JTextField build(Container container,
            final InputComponent<?, Object> input,
            final CommandController controller,
            final ChangeSupport changeSupport) {
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
        JButton btn = new JButton("Browse...");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseButtonPressed(txt, input, controller, changeSupport);
            }
        });
        // Add components to container
        container.add(label);
        container.add(txt,"split 2");
        container.add(btn,"width ::85");
        return txt;
    }

    @Override
    protected void setEnabled(JTextField txf, boolean enabled) {
        JPanel panel = (JPanel) txf.getParent();
        for (Component component : panel.getComponents()) {
            component.setEnabled(enabled);
        }
    }

    protected abstract void browseButtonPressed(
            final JTextField textField,
            final InputComponent<?, Object> input,
            final CommandController controller,
            final ChangeSupport changeSupport);

}
