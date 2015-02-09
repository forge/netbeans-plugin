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
