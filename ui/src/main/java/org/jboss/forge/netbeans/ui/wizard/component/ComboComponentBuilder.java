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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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
@ServiceProvider(position = 20, service = ComponentBuilder.class)
public class ComboComponentBuilder extends ComponentBuilder<JComboBox> {

    @Override
    public JComboBox build(final Container container,
            final InputComponent<?, Object> input,
            final CommandController controller,
            final ChangeSupport changeSupport) {
        final DefaultComboBoxModel model = new DefaultComboBoxModel();
        final JComboBox combo = new JComboBox(model);
        combo.setEnabled(input.isEnabled());
        combo.setToolTipText(input.getDescription());
        final UISelectOne<Object> selectOne = (UISelectOne) input;
        Converter<Object, String> converter = InputComponents.getItemLabelConverter(getConverterFactory(), selectOne);
        updateComboModel(combo, selectOne);
        combo.setSelectedItem(converter.convert(selectOne.getValue()));
        combo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // To prevent nullifying input's value when model is cleared
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    controller.setValueFor(input.getName(), combo.getSelectedItem());
                    changeSupport.fireChange();
                }
            }
        });
        container.add(new JLabel(InputComponents.getLabelFor(input, true)));
        container.add(combo);
        return combo;
    }

    @Override
    public void updateState(JComboBox combo, InputComponent<?, ?> input) {
        super.updateState(combo, input);
        updateComboModel(combo, (UISelectOne<Object>) input);
    }

    private void updateComboModel(JComboBox combo, UISelectOne<Object> selectOne) {
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) combo.getModel();
        String[] choices = getChoices(model);
        String[] values = getValueChoices(selectOne);
        if (!Arrays.equals(choices, values)) {
            model.removeAllElements();
            for (String valueChoice : values) {
                model.addElement(valueChoice);
            }
        }
    }

    private String[] getValueChoices(UISelectOne<Object> selectOne) {
        Converter<Object, String> converter = InputComponents.getItemLabelConverter(getConverterFactory(), selectOne);
        List<String> result = new ArrayList<>();
        for (Object obj : selectOne.getValueChoices()) {
            result.add(converter.convert(obj));
        }
        return result.toArray(new String[result.size()]);
    }

    private String[] getChoices(DefaultComboBoxModel<String> model) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) {
            result.add(model.getElementAt(i));
        }
        return result.toArray(new String[result.size()]);
    }

    @Override
    protected Class<Object> getProducedType() {
        return Object.class;
    }

    @Override
    protected String getSupportedInputType() {
        return InputType.DROPDOWN;
    }

    @Override
    protected Class<?>[] getSupportedInputComponentTypes() {
        return new Class<?>[]{UISelectOne.class};
    }
}
