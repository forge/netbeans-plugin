/* 
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    <a href="mailto:ggastald@redhat.com">George Gastaldi</a> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui.wizard.component.many;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import net.miginfocom.swing.MigLayout;
import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.input.UIInputMany;
import org.jboss.forge.addon.ui.util.InputComponents;
import org.jboss.forge.netbeans.ui.wizard.component.ComponentBuilder;
import org.openide.util.ChangeSupport;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public abstract class AbstractListButtonComponentBuilder extends ComponentBuilder<JList> {

    @Override
    public JList build(Container container, final InputComponent<?, Object> input, final CommandController controller, final ChangeSupport changeSupport) {
        // Adding titled label
        TitledBorder panelBorder
                = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                        InputComponents.getLabelFor(input, false),
                        TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION);
        JPanel panel = new JPanel(new MigLayout("fill"));
        panel.setBorder(panelBorder);
        final DefaultListModel<String> model = new DefaultListModel<>();
        final JList list = new JList(model);
        list.setEnabled(input.isEnabled());
        list.setToolTipText(input.getDescription());
        UIInputMany<Object> inputMany = (UIInputMany<Object>) input;
        for (String value : getValue(inputMany)) {
            model.addElement(value);
        }
        model.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                controller.setValueFor(input.getName(), getValuesFromModel(model));
                changeSupport.fireChange();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                controller.setValueFor(input.getName(), getValuesFromModel(model));
                changeSupport.fireChange();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                controller.setValueFor(input.getName(), getValuesFromModel(model));
                changeSupport.fireChange();
            }
        });
        JButton addButton = new JButton("Add...");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButtonPressed(model, input, controller, changeSupport);
            }
        });
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedIndices = list.getSelectedIndices();
                for (int i = selectedIndices.length - 1; i > -1; i--) {
                    model.removeElementAt(i);
                }
            }
        });
        panel.add(new JScrollPane(list), "growx,height :150:300, width 80%");
        JPanel panelButtons = new JPanel(new GridLayout(2,1));
        panelButtons.add(addButton);
        panelButtons.add(removeButton);
        panel.add(panelButtons,"right,width ::20%");
        container.add(panel,"growx,span 2");
        
        return list;
    }

    protected abstract void addButtonPressed(DefaultListModel<String> model, InputComponent<?, Object> input, CommandController controller, ChangeSupport changeSupport);

    private List<String> getValue(UIInputMany<Object> inputMany) {
        Converter<Object, String> converter = getConverterFactory().getConverter(inputMany.getValueType(), String.class);
        List<String> result = new ArrayList<>();
        for (Object obj : inputMany.getValue()) {
            result.add(converter.convert(obj));
        }
        return result;
    }

    private List<String> getValuesFromModel(DefaultListModel model) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) {
            result.add((String) model.get(i));
        }
        return result;
    }

    @Override
    protected Class<?> getProducedType() {
        return Object.class;
    }

    @Override
    protected Class<?>[] getSupportedInputComponentTypes() {
        return new Class[]{UIInputMany.class};
    }
}
