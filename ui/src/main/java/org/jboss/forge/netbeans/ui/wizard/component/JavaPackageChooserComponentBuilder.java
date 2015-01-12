/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.wizard.component;

import javax.swing.JTextField;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.input.UIInput;
import org.openide.util.ChangeSupport;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
//@ServiceProvider(position = 110, service = ComponentBuilder.class)
public class JavaPackageChooserComponentBuilder extends AbstractTextButtonComponentBuilder {

    @Override
    protected void browseButtonPressed(JTextField textField, InputComponent<?, Object> input, CommandController controller, ChangeSupport changeSupport) {
    }

    @Override
    protected Class<String> getProducedType() {
        return String.class;
    }

    @Override
    protected String getSupportedInputType() {
        return InputType.JAVA_PACKAGE_PICKER;
    }

    @Override
    protected Class<?>[] getSupportedInputComponentTypes() {
        return new Class<?>[]{UIInput.class};
    }

}
