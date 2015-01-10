/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.wizard;

import java.text.MessageFormat;
import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.addon.ui.command.UICommand;
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
import org.openide.util.Exceptions;

/**
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
                    setDefaultWizardDescriptorValues(wizDescriptor, metadata);
                    if (DialogDisplayer.getDefault().notify(wizDescriptor) == WizardDescriptor.FINISH_OPTION) {
                        result = iterator.getExecutionResult();
                    }
                } else {
                    // Single-step command
                    ForgeWizardPanel panel = new ForgeWizardPanel(controller);
                    wizDescriptor = new WizardDescriptor(new WizardDescriptor.Panel[]{panel});
                    panel.setWizardDescriptor(wizDescriptor);
                    setDefaultWizardDescriptorValues(wizDescriptor, metadata);
                    if (DialogDisplayer.getDefault().notify(wizDescriptor) == WizardDescriptor.FINISH_OPTION) {
                        result = controller.execute();
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

    private void setDefaultWizardDescriptorValues(WizardDescriptor wizDescriptor, UICommandMetadata metadata) {
        wizDescriptor.putProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, Boolean.TRUE); // NOI18N
        wizDescriptor.putProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, Boolean.FALSE); // NOI18N
        wizDescriptor.putProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, Boolean.TRUE); // NOI18N

        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wizDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizDescriptor.setTitle(metadata.getName());
    }

}
