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
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.input.UISelectMany;
import org.jboss.forge.addon.ui.util.InputComponents;
import org.jboss.forge.netbeans.ui.wizard.component.ComponentBuilder;
import org.openide.util.ChangeSupport;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@ServiceProvider(position = 70, service = ComponentBuilder.class)
public class CheckboxTableComponentBuilder extends ComponentBuilder<JTable> {

    @Override
    public JTable build(final Container container,
            final InputComponent<?, Object> input,
            final CommandController controller,
            final ChangeSupport changeSupport) {
        JPanel panel = new JPanel(new MigLayout("fill"));
        final DefaultTableModel model = new BooleanTableModel(0, 2);
        JTable table = new JTable(model);
        // Remove the header
        table.setTableHeader(null);
        UISelectMany<Object> selectMany = (UISelectMany<Object>) input;
        table.setEnabled(input.isEnabled());
        table.setToolTipText(input.getDescription());
        List<String> value = getValue(selectMany);
        List<String> valueChoices = getValueChoices(selectMany);
        table.getColumnModel().getColumn(0).setMaxWidth(30);
        for (String valueChoice : valueChoices) {
            model.addRow(new Object[]{value.contains(valueChoice), valueChoice});
        }
        // Adding titled label
        TitledBorder panelBorder
                = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                        InputComponents.getLabelFor(input, false),
                        TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION);
        panel.setBorder(panelBorder);
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                controller.setValueFor(input.getName(), getSelectedValuesFromModel(model));
                changeSupport.fireChange();
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, "grow,height :150:300");
        container.add(panel, "span 2,growx");
        return table;
    }

    private List<String> getValue(UISelectMany<Object> selectMany) {
        Converter<Object, String> converter = InputComponents.getItemLabelConverter(getConverterFactory(), selectMany);
        List<String> result = new ArrayList<>();
        for (Object obj : selectMany.getValue()) {
            result.add(converter.convert(obj));
        }
        return result;
    }

    private List<String> getValueChoices(UISelectMany<Object> selectMany) {
        Converter<Object, String> converter = InputComponents.getItemLabelConverter(getConverterFactory(), selectMany);
        List<String> result = new ArrayList<>();
        for (Object obj : selectMany.getValueChoices()) {
            result.add(converter.convert(obj));
        }
        return result;
    }

    private List<String> getSelectedValuesFromModel(DefaultTableModel model) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean selected = (Boolean) model.getValueAt(i, 0);
            if (selected) {
                result.add((String) model.getValueAt(i, 1));
            }
        }
        return result;
    }

    @Override
    protected Class<?> getProducedType() {
        return Object.class;
    }

    @Override
    protected String getSupportedInputType() {
        return InputType.CHECKBOX;
    }

    @Override
    protected Class<?>[] getSupportedInputComponentTypes() {
        return new Class<?>[]{UISelectMany.class};
    }

    private class BooleanTableModel extends DefaultTableModel {

        public BooleanTableModel(int rowCount, int columnCount) {
            super(rowCount, columnCount);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Boolean.class;
            }
            return Object.class;
        }

    }
}
