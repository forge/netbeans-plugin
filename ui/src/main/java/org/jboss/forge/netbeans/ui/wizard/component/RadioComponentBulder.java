/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.wizard.component;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import net.miginfocom.swing.MigLayout;
import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.util.InputComponents;
import org.openide.util.ChangeSupport;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@ServiceProvider(position = 30, service = ComponentBuilder.class)
public class RadioComponentBulder extends ComponentBuilder<JPanel> {

    @Override
    public JPanel build(final Container container,
            final InputComponent<?, Object> input,
            final CommandController controller,
            final ChangeSupport changeSupport) {
        JPanel panel = new JPanel(new MigLayout("left"));
        UISelectOne<Object> selectOne = (UISelectOne<Object>) input;
        Converter<Object, String> converter = InputComponents
                .getItemLabelConverter(getConverterFactory(), selectOne);
        final ButtonGroup buttonGroup = new ButtonGroup();
        String selectedValue = converter.convert(selectOne.getValue());
        for (Object choice : selectOne.getValueChoices()) {
            final String text = converter.convert(choice);
            boolean selected = text.equals(selectedValue);
            JRadioButton radio = new JRadioButton(text, selected);
            radio.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.setValueFor(input.getName(), text);
                    changeSupport.fireChange();
                }
            });
            buttonGroup.add(radio);
            panel.add(radio);
        }
        setEnabled(panel, input.isEnabled());
        container.add(new JLabel(InputComponents.getLabelFor(input, true)));
        container.add(panel);
        return panel;
    }

    @Override
    protected Class<Object> getProducedType() {
        return Object.class;
    }

    @Override
    protected String getSupportedInputType() {
        return InputType.RADIO;
    }

    @Override
    protected Class<?>[] getSupportedInputComponentTypes() {
        return new Class<?>[]{UISelectOne.class};
    }
}
