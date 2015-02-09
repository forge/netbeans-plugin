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
//@ServiceProvider(position = 100, service = ComponentBuilder.class)
public class JavaClassChooserComponentBuilder extends AbstractTextButtonComponentBuilder {

    @Override
    protected void browseButtonPressed(JTextField textField, InputComponent<?, Object> input, CommandController controller, ChangeSupport changeSupport) {
//        JavaTemplates
    }

    @Override
    protected Class<String> getProducedType() {
        return String.class;
    }

    @Override
    protected String getSupportedInputType() {
        return InputType.JAVA_CLASS_PICKER;
    }

    @Override
    protected Class<?>[] getSupportedInputComponentTypes() {
        return new Class<?>[]{UIInput.class};
    }

}
