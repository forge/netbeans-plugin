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

import java.io.File;
import javax.swing.DefaultListModel;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UISelection;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.netbeans.ui.wizard.component.ComponentBuilder;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.ChangeSupport;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@ServiceProvider(position = 140, service = ComponentBuilder.class)
public class DirectoryChooserMultipleComponentBuilder extends AbstractListButtonComponentBuilder {

    @Override
    protected void addButtonPressed(DefaultListModel<String> model, InputComponent<?, Object> input, CommandController controller, ChangeSupport changeSupport) {
        UIContext context = controller.getContext();
        UISelection<Resource<?>> initialSelection = context.getInitialSelection();
        File file = (File) initialSelection.get().getUnderlyingResourceObject();
        File openFile = new FileChooserBuilder("jboss-forge")
                .setDefaultWorkingDirectory(file)
                .setDirectoriesOnly(true)
                .showOpenDialog();
        if (openFile != null) {
            model.addElement(openFile.toString());
        }
    }

    @Override
    protected String getSupportedInputType() {
        return InputType.DIRECTORY_PICKER;
    }

}
