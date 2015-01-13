/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.wizard.component.many;

import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.netbeans.ui.wizard.component.ComponentBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 * ComponentBuilder implementation for multiple Java Package picker
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@ServiceProvider(position = 150, service = ComponentBuilder.class)
public class JavaPackageChooserMultipleComponentBuilder extends TextboxMultipleComponentBuilder {

    @Override
    protected String getSupportedInputType() {
        return InputType.JAVA_PACKAGE_PICKER;
    }
}
