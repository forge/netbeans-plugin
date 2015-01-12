/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.wizard.component;

import java.io.File;
import javax.swing.JTextField;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UISelection;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.InputComponent;
import org.jboss.forge.addon.ui.input.UIInput;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.ChangeSupport;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation for UIInput<DirectoryResource> components
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@ServiceProvider(position = 90,service = ComponentBuilder.class)
public class DirectoryChooserComponentBuilder extends AbstractTextButtonComponentBuilder {

    @Override
    protected void browseButtonPressed(JTextField textField, InputComponent<?, Object> input, CommandController controller, ChangeSupport changeSupport) {
        UIContext context = controller.getContext();
        UISelection<Resource<?>> initialSelection = context.getInitialSelection();
        File file = (File) initialSelection.get().getUnderlyingResourceObject();
        FileChooserBuilder builder = new FileChooserBuilder("jboss-forge");
        builder.setDefaultWorkingDirectory(file);
        builder.setDirectoriesOnly(true);
        File openFile = builder.showOpenDialog();
        if (openFile != null) {
            textField.setText(openFile.toString());
        }
    }

    @Override
    protected Class<File> getProducedType() {
        return File.class;
    }

    @Override
    protected String getSupportedInputType() {
        return InputType.DIRECTORY_PICKER;
    }

    @Override
    protected Class<?>[] getSupportedInputComponentTypes() {
        return new Class<?>[]{UIInput.class};
    }
}