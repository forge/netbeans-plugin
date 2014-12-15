/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.forge.netbeans.ui.wizard;

import org.jboss.forge.addon.ui.command.CommandFactory;
import org.jboss.forge.addon.ui.command.UICommand;
import org.jboss.forge.addon.ui.controller.CommandController;
import org.jboss.forge.addon.ui.controller.CommandControllerFactory;
import org.jboss.forge.addon.ui.controller.WizardCommandController;
import org.jboss.forge.netbeans.runtime.FurnaceService;
import org.jboss.forge.netbeans.ui.NbUIRuntime;
import org.jboss.forge.netbeans.ui.context.NbUIContext;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.WizardDescriptor;
import org.openide.loaders.TemplateWizard;

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
            WizardDescriptor wizDescriptor;
            if (controller instanceof WizardCommandController) {
                wizDescriptor = new WizardDescriptor(new ForgeWizardIterator((WizardCommandController) controller));
            } else {
//               wizDescriptor = new WizardDescriptor(new ForgeSingleCommandIterator(controller));
            }
            
            System.out.println(DialogDisplayer.getDefault().notify(new TemplateWizard()));
        }
    }

}
