/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
