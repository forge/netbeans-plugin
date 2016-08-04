/* 
 * Copyright (c) 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    <a href="mailto:ggastald@redhat.com">George Gastaldi</a> - initial API and implementation and/or initial documentation
 */
package org.jboss.forge.netbeans.ui.wizard;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Optional;
import javax.swing.JEditorPane;
import javax.swing.text.StyledDocument;
import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.context.UIRegion;
import org.jboss.forge.addon.ui.context.UISelection;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.controller.CommandControllerFactory;
import org.jboss.forge.addon.ui.controller.WizardCommandController;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.netbeans.runtime.FurnaceService;
import org.jboss.forge.netbeans.ui.NbUIRuntime;
import org.jboss.forge.netbeans.ui.context.NbUIContext;
import org.jboss.forge.netbeans.ui.wizard.util.NotificationHelper;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;

/**
 * A Runnable suited to run a Forge command
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class RunForgeWizardRunnable implements Runnable {

    private final String commandName;

    public RunForgeWizardRunnable(String commandName) {
        this.commandName = commandName;
    }

    @Override
    public void run() {
        try (NbUIContext context = new NbUIContext()) {
            CommandFactory commandFactory = FurnaceService.INSTANCE.getCommandFactory();
            UICommand command = commandFactory.getNewCommandByName(context, commandName);
            CommandControllerFactory controllerFactory = FurnaceService.INSTANCE.getCommandControllerFactory();
            CommandController controller = controllerFactory.createController(context, NbUIRuntime.INSTANCE, command);
            UICommandMetadata metadata = controller.getMetadata();
            Result result = null;
            controller.initialize();
            if (controller.getInputs().isEmpty() && controller.canExecute()) {
                // Execute directly
                result = controller.execute();
            } else {
                WizardDescriptor wizDescriptor;
                if (controller instanceof WizardCommandController) {
                    // Multi-step wizard
                    ForgeWizardIterator iterator = new ForgeWizardIterator((WizardCommandController) controller);
                    wizDescriptor = new WizardDescriptor(iterator);
                    setDefaultWizardDescriptorValues(wizDescriptor, context, metadata);
                    if (DialogDisplayer.getDefault().notify(wizDescriptor) == WizardDescriptor.FINISH_OPTION) {
                        result = iterator.getExecutionResult();
                        openSelectedFiles(context);
                    }
                } else {
                    // Single-step command
                    ForgeWizardPanel panel = new ForgeWizardPanel(controller);
                    wizDescriptor = new WizardDescriptor(new WizardDescriptor.Panel[]{panel});
                    panel.setWizardDescriptor(wizDescriptor);
                    setDefaultWizardDescriptorValues(wizDescriptor, context, metadata);
                    wizDescriptor.putProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, Boolean.FALSE);
                    if (DialogDisplayer.getDefault().notify(wizDescriptor) == WizardDescriptor.FINISH_OPTION) {
                        result = controller.execute();
                        openSelectedFiles(context);
                    }
                }
            }
            if (result != null) {
                NotificationHelper.displayResult(commandName, result);
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void setDefaultWizardDescriptorValues(WizardDescriptor wizDescriptor, NbUIContext context, UICommandMetadata metadata) {
        wizDescriptor.putProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, Boolean.TRUE);
        wizDescriptor.putProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, Boolean.TRUE);
        wizDescriptor.putProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, Boolean.TRUE);

        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wizDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizDescriptor.setTitle(metadata.getName() + " [" + context.getInitialSelection().get() + "]");
    }

    /**
     * Opens the files that were selected after the command execution
     *
     * @param context
     * @throws IOException
     */
    private void openSelectedFiles(NbUIContext context) throws IOException {
        UISelection<Object> selection = context.getSelection();
        Optional<UIRegion<Object>> region = Optional.empty();
        boolean first = true;
        for (Object resource : selection) {
            if (first) {
                region = selection.getRegion();
                first = false;
            }
            try {
                //Using reflection due to classloader issues
                Method method = resource.getClass().getMethod(
                        "getUnderlyingResourceObject");
                Object underlyingResourceObject = method.invoke(resource);
                if (underlyingResourceObject instanceof File) {
                    File file = (File) underlyingResourceObject;
                    if (file.exists() && !file.isDirectory()) {
                        DataObject dObj = DataObject.find(FileUtil.toFileObject(file));
                        EditorCookie editorCookie = dObj.getLookup().lookup(EditorCookie.class);
                        editorCookie.open();
                        region.ifPresent(r -> {
                            JEditorPane[] openedPanes = editorCookie.getOpenedPanes();
                            if (openedPanes != null) {
                                StyledDocument doc;
                                try {
                                    doc = editorCookie.openDocument();
                                    int startLineOffset = NbDocument.findLineOffset(doc, r.getStartLine() - 1);
                                    int endLineOffset = NbDocument.findLineOffset(doc, r.getEndLine() - 1);
                                    for (JEditorPane editor : openedPanes) {
                                        int start = startLineOffset + r.getStartPosition();
                                        int end = endLineOffset + r.getEndPosition();
                                        editor.select(start, end);
                                    }
                                } catch (IOException ex) {
                                    Exceptions.printStackTrace(ex);
                                }
                            }
                        });
                    }
                }
            } catch (NoSuchMethodException ex) {
                // ignore
            } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
