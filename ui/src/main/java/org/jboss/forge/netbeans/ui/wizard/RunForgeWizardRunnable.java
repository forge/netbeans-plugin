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
import org.jboss.forge.addon.ui.result.CompositeResult;
import org.jboss.forge.addon.ui.result.Failed;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.netbeans.runtime.FurnaceService;
import org.jboss.forge.netbeans.ui.NbUIRuntime;
import org.jboss.forge.netbeans.ui.context.NbUIContext;
import org.netbeans.api.annotations.common.StaticResource;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.NotificationDisplayer;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

/**
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class RunForgeWizardRunnable implements Runnable {

    private final String commandName;

    @StaticResource
    private final String ICON = "org/jboss/forge/netbeans/ui/forge.png";

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
            Result result;
            controller.initialize();
            if (controller.getInputs().isEmpty() && controller.canExecute()) {
                // Execute directly
                result = controller.execute();
            } else {
                WizardDescriptor wizDescriptor;
                if (controller instanceof WizardCommandController) {
                    wizDescriptor = new WizardDescriptor(new ForgeWizardIterator((WizardCommandController) controller));
                } else {
                    WizardDescriptor.Panel[] panels = {new ForgeWizardPanel(controller)};
                    wizDescriptor = new WizardDescriptor(new WizardDescriptor.ArrayIterator(panels));
                }
                wizDescriptor.putProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, Boolean.TRUE); // NOI18N
                wizDescriptor.putProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, Boolean.TRUE); // NOI18N
                wizDescriptor.putProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, Boolean.TRUE); // NOI18N

                // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
                wizDescriptor.setTitleFormat(new MessageFormat("{0}"));
                wizDescriptor.setTitle(metadata.getName());

                if (DialogDisplayer.getDefault().notify(wizDescriptor) == WizardDescriptor.FINISH_OPTION) {
                    result = controller.execute();
                } else {
                    result = null;
                }
            }
            if (result != null) {
                displayResult(result);
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void displayResult(Result result) {
        if (result instanceof CompositeResult) {
            for (Result childResult : ((CompositeResult) result).getResults()) {
                displayResult(childResult);
            }
        } else {
            final NotificationDisplayer notificationDisplayer = NotificationDisplayer.getDefault();
            if (result instanceof Failed) {
                notificationDisplayer.notify(commandName, ImageUtilities.loadImageIcon(ICON, false), result.getMessage(), null);
            } else {
                notificationDisplayer.notify(commandName, ImageUtilities.loadImageIcon(ICON, false), result.getMessage(), null);
            }
        }
    }
}
