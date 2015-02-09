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

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.netbeans.ui.wizard.component.ComponentBuilder;
import org.openide.util.ChangeSupport;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@ServiceProvider(position = 120, service = ComponentBuilder.class)
public class TextboxMultipleComponentBuilder extends AbstractListButtonComponentBuilder {

    @Override
    protected void addButtonPressed(DefaultListModel<String> model, InputComponent<?, Object> input, CommandController controller, ChangeSupport changeSupport) {
        String value = JOptionPane.showInputDialog("Enter a value:");
        if (value != null) {
            model.addElement(value);
        }
    }

    @Override
    protected String getSupportedInputType() {
        return InputType.TEXTBOX;
    }

}
